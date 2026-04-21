package br.com.latanks.cidasdepilacao_api.dtos.request;

import java.time.LocalDate;
import java.util.Date;

public record CreateUserDTO(
        String name,
        String password,
        String email,
        String phoneNumber,
        LocalDate birthday) {
}
