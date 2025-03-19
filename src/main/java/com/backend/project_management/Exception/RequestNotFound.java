package com.backend.project_management.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class RequestNotFound extends RuntimeException{
        public RequestNotFound(String message){
            super(message);
        }
    }


