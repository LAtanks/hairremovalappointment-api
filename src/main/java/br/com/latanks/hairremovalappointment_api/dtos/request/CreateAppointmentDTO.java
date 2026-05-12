package br.com.latanks.hairremovalappointment_api.dtos.request;

import br.com.latanks.hairremovalappointment_api.models.enums.Payment;
import br.com.latanks.hairremovalappointment_api.models.enums.Type;
import java.time.LocalDateTime;

public record CreateAppointmentDTO(
        LocalDateTime horary,

        Type type,

        Payment payment
)
{}
