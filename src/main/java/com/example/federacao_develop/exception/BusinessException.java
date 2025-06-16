package com.example.federacao_develop.exception;

public class BusinessException extends RuntimeException {
    private final MensagemExceptionEnum mensagemEnum;

    public BusinessException(MensagemExceptionEnum msg) {
        super(msg.getMessage());
        this.mensagemEnum = msg;
    }

    public MensagemExceptionEnum getMensagemEnum() {
        return mensagemEnum;
    }
}