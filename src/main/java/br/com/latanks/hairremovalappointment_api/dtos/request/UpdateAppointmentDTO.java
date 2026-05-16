package br.com.latanks.hairremovalappointment_api.dtos.request;

import br.com.latanks.hairremovalappointment_api.models.enums.Payment;
import br.com.latanks.hairremovalappointment_api.models.enums.Areas;

import java.time.LocalDateTime;

public record UpdateAppointmentDTO(
        LocalDateTime horary,

        Areas areas,

        Payment payment
)
{}