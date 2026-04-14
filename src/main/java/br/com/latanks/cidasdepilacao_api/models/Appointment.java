package br.com.latanks.cidasdepilacao_api.models;

import br.com.latanks.cidasdepilacao_api.models.enums.Payment;
import br.com.latanks.cidasdepilacao_api.models.enums.Situation;
import br.com.latanks.cidasdepilacao_api.models.enums.Type;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_appointments")
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @DateTimeFormat
    @Column(name = "horary", nullable = false)
    private OffsetDateTime horary;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment", nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "situation", nullable = false)
    private Situation situation;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

}
