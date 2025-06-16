package com.example.federacao_develop.exception;

public class NotFoundException extends RuntimeException {
    private final MensagemExceptionEnum mensagemEnum;

    public NotFoundException(MensagemExceptionEnum msg) {
        super(msg.getMessage());
        this.mensagemEnum = msg;
    }
    public MensagemExceptionEnum getMensagemEnum() {
        return mensagemEnum;
    }
}