package com.example.federacao_develop.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse(int status, String message, LocalDateTime timestamp) {}