package br.com.latanks.cidasdepilacao_api.dtos.response;

import java.time.LocalDate;
import java.util.UUID;

public record CreatedUserDTO(
        UUID id,
        String name,
        String email,
        String phoneNumber,
        LocalDate birthday
) {
}
