# 🏡 동네방네 🏡

![readme_mockup2](https://github.com/kyungmin1221/BaekJoon/assets/105621255/41dd9ef3-00ce-46a9-b4d8-52a7298108c3)


</div>

## 배포 주소
> **개발 버전** : [https://dongnaebangnae.vercel.app/](https://dongnaebangnae.vercel.app/) <br>
> **서비스 서버** : [https://www.dongnaebangnae.com/](https://www.dongnaebangnae.com/)<br>



## 목차
  - [프로젝트 소개](#프로젝트-소개) 
  - [프로젝트 개요](#프로젝트-개요)
  - [팀원 소개](#백엔드-팀원-소개)
  
## 프로젝트 소개


## 프로젝트 개요
> **프로젝트 이름 : 동네방네** <br/>
**개발기간: 2024.04.26 ~ 2024.05.06** <br/>
**언어 : Spring Boot** <br />



## 백엔드 팀원 소개

| **박경민(팀장)** | **유재성** | **김주원** | **이은미** |
| :------: |  :------: | :------: | :------: |
| [<img src="https://github.com/kyungmin1221/BaekJoon/assets/105621255/1d1fd83d-ef01-4144-9d65-9b6056d40a43" height=150 width=150> <br/> @kyungmin](https://github.com/kyungmin1221) | [<img src="" height=150 width=150> <br/> @]() | [<img src="" height=150 width=150> <br/> @]() | [<img src="https://github.com/kyungmin1221/BaekJoon/assets/105621255/2373d997-73e3-47d7-84f9-fdc0c12cfa28" height=150 width=150> <br/> @minnieming](https://github.com/minnieming) |


## 1. 개발 환경

Back-end : Spring Boot
- Front :  React
- 버전 및 이슈관리 : Github, Github Issues, Github Project
- 협업 툴 : Gather, Notion, Slack
- 서비스 배포 환경 : Netlify
- 디자인 : [Figma](https://www.figma.com/file/fAisC2pEKzxTOzet9CfqML/README(oh-my-code)?node-id=39%3A1814)
- [커밋 컨벤션](https://github.com/likelion-project-README/README/wiki/%EC%BB%A4%EB%B0%8B-%EC%BB%A8%EB%B2%A4%EC%85%98)
- [코드 컨벤션](https://github.com/likelion-project-README/README/wiki/%EC%BD%94%EB%93%9C-%EC%BB%A8%EB%B2%A4%EC%85%98)
- [스프라이트](https://github.com/likelion-project-README/README/wiki/%EC%8A%A4%ED%94%84%EB%9D%BC%EC%9D%B4%ED%8A%B8)
<br>

## 2. 채택한 개발 기술과 브랜치 전략

### Spring

- d
    
### d

- f

### d

- 

### 브랜치 전략

- Git-flow 전략을 기반으로 main, develop 브랜치와 feature 보조 브랜치를 운용했습니다.
- main, develop, Feat 브랜치로 나누어 개발을 하였습니다.
    - **main** 브랜치는 배포 단계에서만 사용하는 브랜치입니다.
    - **develop** 브랜치는 개발 단계에서 git-flow의 master 역할을 하는 브랜치입니다.
    - **Feat** 브랜치는 기능 단위로 독립적인 개발 환경을 위하여 사용하고 merge 후 각 브랜치를 삭제해주었습니다.

<br>



## 4. 역할 분담

### 😎 박경민

- **기능**
  - 게시물 관련 기능
    - 게시물 조회 / 무한스크롤
    - QueryDSL 동적 쿼리를 사용한 조회  
  - 북마크 관련 기능
    - 북마크 생성/취소 
  - 이미지 최적화 처리
    - 이미지 리사이즈 처리하여 성능 최적화 
  - 이메일 인증 구현
    - Redis 사용 
  - ElasticSearch 를 사용한 검색 기능
    - 게시물 내용 검색 
  

<br>
    
### 🤩 유재성

- **기능**

<br>

### 😃 김주원

- **기능**

<br>

### 🥳 이은미

- **기능**
  - 댓글 CRUD
  - 좋아요 CRUD
  - 채팅, 알림 기능
    
<br>


## 3. 프로젝트 구조

```
├── README.md
├── .eslintrc.js
├── .gitignore
├── .prettierrc.json
├── package-lock.json
├── package.json
│
├── public
│    └── index.html
└── src
     ├── App.jsx
     ├── index.jsx
     ├── api
     │     └── mandarinAPI.js
     ├── asset
     │     ├── fonts
     │     ├── css_sprites.png
     │     ├── logo-404.svg
     │     └── logo-home.svg
     │          .
     │          .
     │          .
     ├── atoms
     │     ├── LoginData.js
     │     └── LoginState.js
     ├── common
     │     ├── alert
     │     │     ├── Alert.jsx
     │     │     └── Alert.Style.jsx
     │     ├── button
     │     ├── comment
     │     ├── inputBox
     │     ├── post
     │     ├── postModal
     │     ├── product
     │     ├── tabMenu
     │     ├── topBanner
     │     └── userBanner
     ├── pages
     │     ├── addProduct
     │     │     ├── AddProduct.jsx
     │     │     └── AddProduct.Style.jsx
     │     ├── chatList
     │     ├── chatRoom
     │     ├── emailLogin
     │     ├── followerList
     │     ├── followingList
     │     ├── home
     │     ├── join
     │     ├── page404
     │     ├── postDetail
     │     ├── postEdit
     │     ├── postUpload
     │     ├── productEdit
     │     ├── profile
     │     ├── profileEdit
     │     ├── profileSetting
     │     ├── search
     │     ├── snsLogin
     │     └── splash
     ├── routes
     │     ├── privateRoutes.jsx
     │     └── privateRoutesRev.jsx  
     └── styles
           └── Globalstyled.jsx
```
