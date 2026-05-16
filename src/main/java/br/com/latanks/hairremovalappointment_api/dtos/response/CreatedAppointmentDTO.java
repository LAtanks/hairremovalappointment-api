package br.com.latanks.hairremovalappointment_api.dtos.response;

import br.com.latanks.hairremovalappointment_api.models.enums.Payment;
import br.com.latanks.hairremovalappointment_api.models.enums.Status;
import br.com.latanks.hairremovalappointment_api.models.enums.Areas;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreatedAppointmentDTO(
        UUID id,
        LocalDateTime horary,
        Areas areas,
        Payment payment,
        Status status
)
{}
