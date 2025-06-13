package com.example.federacao_develop.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(MensagemExceptionEnum msg) {
        super(msg.getMessage());
    }
}