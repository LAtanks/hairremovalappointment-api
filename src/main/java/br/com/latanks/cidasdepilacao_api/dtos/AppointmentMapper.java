package br.com.latanks.cidasdepilacao_api.dtos;

import br.com.latanks.cidasdepilacao_api.dtos.request.CreateAppointmentDTO;
import br.com.latanks.cidasdepilacao_api.dtos.request.UpdateAppointmentDTO;
import br.com.latanks.cidasdepilacao_api.dtos.response.CreatedAppointmentDTO;
import br.com.latanks.cidasdepilacao_api.models.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Appointment toEntity(CreateAppointmentDTO dto);

    CreatedAppointmentDTO toResponseDTO(Appointment appointment);

    @Mapping(target = "id", ignore = true)

    @Mapping(target = "user", ignore = true)
    Appointment toEntity(UpdateAppointmentDTO dto);

}