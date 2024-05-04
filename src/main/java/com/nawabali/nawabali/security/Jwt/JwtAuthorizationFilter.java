package com.nawabali.nawabali.security.Jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nawabali.nawabali.constant.UserRoleEnum;
import com.nawabali.nawabali.domain.User;
import com.nawabali.nawabali.exception.CustomException;
import com.nawabali.nawabali.exception.ErrorCode;
import com.nawabali.nawabali.global.tool.redis.RedisTool;
import com.nawabali.nawabali.repository.UserRepository;
import com.nawabali.nawabali.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
@Order(2)
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RedisTool redisTool;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.getTokenFromCookieAndName(req, JwtUtil.AUTHORIZATION_HEADER);
//        String accessToken = jwtUtil.getJwtFromHeader(req);
        log.info("accessToken : "+ accessToken);
        if(StringUtils.hasText(accessToken)){
            // 토큰 유무 확인
            accessToken = jwtUtil.substringToken(accessToken);
            String refreshToken =redisTool.getValues(accessToken);
            log.info("저장된 refreshToken :" + refreshToken);

            // 로그아웃 된 accessToken이라면 Exception발생
            if(refreshToken.equals("logout")) {
                log.info("logout 된 access token으로 접근");
                throw new CustomException(ErrorCode.INVALID_AUTH_TOKEN);
            }
            if(!jwtUtil.validateToken(accessToken)){
                // refresh 토큰 재발행
                if(!refreshToken.equals("false")){
                    log.info("refresh 토큰 존재. accessToken 재발급 진행");

                    Claims info = jwtUtil.getUserInfoFromToken(refreshToken);
                    String email = info.getSubject();
                    User user = userRepository.findByEmail(email).orElseThrow(
                            ()-> new CustomException(ErrorCode.USER_NOT_FOUND));
                    UserRoleEnum role = user.getRole();
                    // 새로운 access, refresh Token 발행
                    String newAccessToken = jwtUtil.createAccessToken(email, role);
                    String newRefreshToken = jwtUtil.createRefreshToken(email);
                    Cookie newAcessCookie = jwtUtil.createAccessCookie(newAccessToken);
                    log.info("발급한 유저의 email : " + email);

                    res.addHeader("Set-Cookie", String.format("%s; Secure; ameSite=None;",newAccessToken));

                    redisTool.deleteValues(accessToken);
                    log.info("기존 refreshToken 삭제 key :" + accessToken );
                    redisTool.setValues(jwtUtil.substringToken(newAccessToken), newRefreshToken, Duration.ofMillis(jwtUtil.REFRESH_EXPIRATION_TIME));
                    log.info("refreshToken 재발급 완료 key : " + jwtUtil.substringToken(newAccessToken));

                    try{
                        setAuthentication(info.getSubject());
                    } catch(Exception e){
                        log.error(e.getMessage());
                        return;
                    }
                }else{
                    log.info("refreshToken 존재하지 않음");
//                     쿠키 삭제
                    Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    res.addCookie(cookie);
                    res.addHeader(JwtUtil.AUTHORIZATION_HEADER, null);
                    throw new CustomException(ErrorCode.EXPIRED_JWT);
                }
            }
            else{
                Claims info = jwtUtil.getUserInfoFromToken(accessToken);

                try{
                    setAuthentication(info.getSubject());
                } catch(Exception e){
                    log.error(e.getMessage());
                    return;
                }
            }

        }
        filterChain.doFilter(req, res);

    }

    public void setAuthentication(String email){
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
        log.info(email + " 인증 완료");
    }

    private Authentication createAuthentication(String email){
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
