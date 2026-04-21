package br.com.latanks.cidasdepilacao_api.dtos.request;

import java.time.LocalDate;

public record UpdateUserDTO(
        String name,
        String email,
        String phoneNumber,
        String picture,
        Character physicsDificultes
) {
}
