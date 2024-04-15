package com.blog.blogger.exception;

import org.springframework.http.HttpStatus;

public class BlogAPIException extends Throwable {
    public BlogAPIException(HttpStatus badRequest, String invalidJwtSignature) {
    }
}
