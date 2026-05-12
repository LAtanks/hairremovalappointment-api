package br.com.latanks.hairremovalappointment_api.dtos.request;

import br.com.latanks.hairremovalappointment_api.models.enums.Status;

public record UpdateStatusApptDTO(
        Status status
) {
}
