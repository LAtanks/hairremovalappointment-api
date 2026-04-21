package br.com.latanks.cidasdepilacao_api.dtos.response;

import br.com.latanks.cidasdepilacao_api.models.enums.Payment;
import br.com.latanks.cidasdepilacao_api.models.enums.Type;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CreatedAppointmentDTO(
        UUID id,
        LocalDateTime horary,

        Type type,

        Payment payment
)
{}
