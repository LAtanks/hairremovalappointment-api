package br.com.latanks.hairremovalappointment_api.dtos.request;

public record UpdateUserDTO(
        String name,
        String email,
        String phoneNumber,
        String picture,
        Character physicsDificultes
) {
}
