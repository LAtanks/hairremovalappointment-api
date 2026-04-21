package br.com.latanks.cidasdepilacao_api.models;


import br.com.latanks.cidasdepilacao_api.models.enums.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
@Getter
@Setter
@ToString(includeFieldNames = true)
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Nome não pode ser vazio")
    @Column(name = "name", length = 100, nullable = false)
    @Size(min = 3, message = "Nome precisa ter no minimo 3 caracteres")
    private String name;

    @NotBlank(message = "Senha não pode ser vazia")
    @Size(min = 8, max = 50, message = "Senha precisa ter 8 digitos no minimo e no maximo 50 caracteres")
    @Column(name = "password", nullable = false)
    private String password;

    @Email(message = "Coloque um email valido")
    @NotBlank(message = "Precisa-se de um e-mail")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "physicsDificultes", nullable = true)
    private Character physicsDificultes;

    @URL
    @Column(name = "picture", nullable = true)
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(name = "roles", columnDefinition = "ENUM('ADMIN', 'USER')")
    private Roles roles = Roles.USER;

    @OneToOne(mappedBy = "user")
    @JoinColumn(name = "appointment_id", nullable = true)
    private Appointment appointment;
}