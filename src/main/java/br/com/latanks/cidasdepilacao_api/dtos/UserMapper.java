package br.com.latanks.cidasdepilacao_api.dtos;

import br.com.latanks.cidasdepilacao_api.dtos.request.CreateUserDTO;
import br.com.latanks.cidasdepilacao_api.dtos.request.UpdateUserDTO;
import br.com.latanks.cidasdepilacao_api.dtos.response.CreatedUserDTO;
import br.com.latanks.cidasdepilacao_api.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "appointment", ignore = true)
    @Mapping(target = "picture", ignore = true)
    @Mapping(target = "physicsDificultes", ignore = true)
    User toEntity(CreateUserDTO dto);

    CreatedUserDTO toResponseDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "appointment", ignore = true)
    User toEntity(UpdateUserDTO dto);

}
