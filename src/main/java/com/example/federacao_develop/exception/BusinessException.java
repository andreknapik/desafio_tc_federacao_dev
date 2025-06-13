package com.example.federacao_develop.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(MensagemExceptionEnum msg) {
        super(msg.getMessage());
    }
}