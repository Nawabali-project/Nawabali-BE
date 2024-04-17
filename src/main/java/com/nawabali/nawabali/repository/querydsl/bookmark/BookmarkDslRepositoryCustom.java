package com.nawabali.nawabali.repository.querydsl.bookmark;

import com.nawabali.nawabali.domain.User;
import com.nawabali.nawabali.dto.querydsl.BookmarkDslDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BookmarkDslRepositoryCustom {

    Slice<BookmarkDslDto.UserBookmarkDto> getUserBookmarks(User user, Pageable pageable);
}
