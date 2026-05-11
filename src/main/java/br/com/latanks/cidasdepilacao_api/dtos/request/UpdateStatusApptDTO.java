package br.com.latanks.cidasdepilacao_api.dtos.request;

import br.com.latanks.cidasdepilacao_api.models.enums.Status;

public record UpdateStatusApptDTO(
        Status status
) {
}
