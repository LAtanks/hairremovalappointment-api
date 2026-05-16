package br.com.latanks.hairremovalappointment_api.models;

import br.com.latanks.hairremovalappointment_api.models.enums.Payment;
import br.com.latanks.hairremovalappointment_api.models.enums.Status;
import br.com.latanks.hairremovalappointment_api.models.enums.Areas;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
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
    private LocalDateTime horary;

    @Enumerated(EnumType.STRING)
    @Column(name = "areas", nullable = false)
    private Areas areas;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment", nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.PROGRESS;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

}
