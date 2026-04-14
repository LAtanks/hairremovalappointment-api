package br.com.latanks.cidasdepilacao_api.exceptions;

import java.util.List;

public record ErrorResponse(int status, String type, List<String> errors) {
}
