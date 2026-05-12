package br.com.latanks.hairremovalappointment_api.dtos.response;

import br.com.latanks.hairremovalappointment_api.models.enums.Roles;

import java.time.LocalDate;
import java.util.UUID;

public record CreatedUserDTO(
        UUID id,
        String name,
        String email,
        String phoneNumber,
        LocalDate birthday,
        Roles roles
) {
}
