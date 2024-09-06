package com.luciano.oauth.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;



@ControllerAdvice
public class AuthenticationExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetail> handleThrowedException (Exception ex) {
                
        if (ex instanceof BadCredentialsException) 
        {
            ExceptionDetail exceptionDetail = new ExceptionDetail();
            exceptionDetail.setHttpStatusCode(401);
            exceptionDetail.setMessage("access denied");
            exceptionDetail.setDetail("bad credential");

            return new ResponseEntity<ExceptionDetail>(exceptionDetail, HttpStatusCode.valueOf(401));

        }
        else if (ex instanceof AccessDeniedException) 
        {
            ExceptionDetail exceptionDetail = new ExceptionDetail();
            exceptionDetail.setHttpStatusCode(401);
            exceptionDetail.setMessage("access denied");
            exceptionDetail.setDetail("not authorized");

            return new ResponseEntity<ExceptionDetail>(exceptionDetail, HttpStatusCode.valueOf(401));

        } 
        else if (ex instanceof SignatureException) 
        {
            ExceptionDetail exceptionDetail = new ExceptionDetail();
            exceptionDetail.setHttpStatusCode(403);
            exceptionDetail.setMessage("access denied");
            exceptionDetail.setDetail("invalid token");

            return new ResponseEntity<ExceptionDetail>(exceptionDetail, HttpStatusCode.valueOf(403));

        }
        else if (ex instanceof ExpiredJwtException) 
        {
            ExceptionDetail exceptionDetail = new ExceptionDetail();
            exceptionDetail.setHttpStatusCode(403);
            exceptionDetail.setMessage("access denied");
            exceptionDetail.setDetail("expired token");

            return new ResponseEntity<ExceptionDetail>(exceptionDetail, HttpStatusCode.valueOf(403));

        } 
        else {
           return null; 
        }
       

    }


    
}
