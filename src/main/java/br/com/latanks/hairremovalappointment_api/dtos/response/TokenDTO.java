package br.com.latanks.hairremovalappointment_api.dtos.response;

public record TokenDTO(
        String token,
        String type,
        String email,
        String role
) {
}
