package br.com.latanks.cidasdepilacao_api.dtos.response;

public record TokenDTO(
        String token,
        String type,
        String email,
        String role
) {
}
