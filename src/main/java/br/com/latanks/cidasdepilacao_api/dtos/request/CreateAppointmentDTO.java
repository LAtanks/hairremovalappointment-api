package br.com.latanks.cidasdepilacao_api.dtos.request;


import br.com.latanks.cidasdepilacao_api.models.User;
import br.com.latanks.cidasdepilacao_api.models.enums.Payment;

import br.com.latanks.cidasdepilacao_api.models.enums.Type;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;


public record CreateAppointmentDTO(
        LocalDateTime horary,

        Type type,

        Payment payment,

        UUID userId
)
{}
