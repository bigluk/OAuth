package com.luciano.oauth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExceptionDetail {
    
    private int httpStatusCode;

    private String message;

    private String detail;

}
