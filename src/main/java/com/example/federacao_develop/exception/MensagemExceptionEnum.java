package com.example.federacao_develop.exception;

public enum MensagemExceptionEnum {
    CLUBE_NAO_ENCONTRADO("Clube não encontrado"),
    ESTADIO_NAO_ENCONTRADO("Estádio não encontrado"),
    PARTIDA_NAO_ENCONTRADA("Partida não encontrada"),
    CLUBE_MANDANTE_NAO_ENCONTRADO("Clube mandante não encontrado"),
    CLUBE_VISITANTE_NAO_ENCONTRADO("Clube visitante não encontrado"),
    ESTADIO_COM_CLUBES("Não é possível deletar o estádio pois existe pelo menos um clube vinculado a ele. Para deletar este estádio, você precisa primeiro deletar os clubes associados a ele."),
    ESTADIO_COM_PARTIDAS("Não é possível deletar o estádio pois existem pelo menos uma partida marcada para ele. Para deletar este estádio, você precisa primeiro deletar as partidas associadas a ele.");

    private final String message;
    MensagemExceptionEnum(String message) { this.message = message; }
    public String getMessage() { return message; }
}