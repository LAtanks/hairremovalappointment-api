package br.com.latanks.hairremovalappointment_api.exceptions;

import java.util.List;

public record ErrorResponse(int status, String type, List<String> errors) {
}
