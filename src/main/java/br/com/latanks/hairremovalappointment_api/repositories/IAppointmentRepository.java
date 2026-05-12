package br.com.latanks.hairremovalappointment_api.repositories;

import br.com.latanks.hairremovalappointment_api.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IAppointmentRepository extends JpaRepository<Appointment, UUID> {
    Optional<Appointment> findByUserId(UUID userId);
}
