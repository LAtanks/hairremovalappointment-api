package br.com.latanks.hairremovalappointment_api.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateUserDTO(
        String name,
        @NotBlank(message = "Senha não pode ser vazia")
        @Size(min = 8, max = 50, message = "Senha precisa ter 8 digitos no minimo e no maximo 50 caracteres")
        String password,
        String email,
        String phoneNumber,
        LocalDate birthday) {
}
