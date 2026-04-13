package br.com.latanks.cidasdepilacao_api.repositories;

import br.com.latanks.cidasdepilacao_api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByName(String name);
}
