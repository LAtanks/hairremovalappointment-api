package br.com.latanks.cidasdepilacao_api.repositories;

import br.com.latanks.cidasdepilacao_api.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IAppointmentRepository extends JpaRepository<Appointment, UUID> {
    Optional<Appointment> findByUserId(UUID userId);
}
