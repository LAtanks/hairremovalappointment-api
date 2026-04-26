package br.com.latanks.cidasdepilacao_api.dtos.request;

public record UpdateUserDTO(
        String name,
        String email,
        String phoneNumber,
        String picture,
        Character physicsDificultes
) {
}
