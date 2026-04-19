package br.com.latanks.cidasdepilacao_api.dtos.request;

import br.com.latanks.cidasdepilacao_api.models.Appointment;
import br.com.latanks.cidasdepilacao_api.models.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

public record CreateUserDTO(
     String name,
     String password,
     String email,
     String phoneNumber,
     LocalDate birthday,
     Character physicsDificultes,
     String picture)
{
}
