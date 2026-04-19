package br.com.latanks.cidasdepilacao_api.controllers;

import br.com.latanks.cidasdepilacao_api.exceptions.impl.InvalidCredentialsException;
import br.com.latanks.cidasdepilacao_api.models.Appointment;
import br.com.latanks.cidasdepilacao_api.models.User;
import br.com.latanks.cidasdepilacao_api.repositories.IAppointmentRepository;
import br.com.latanks.cidasdepilacao_api.repositories.IUserRepository;
import br.com.latanks.cidasdepilacao_api.utils.Utils;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private final IAppointmentRepository repository;
    @Autowired
    private final IUserRepository userRepository;


    public AppointmentController(IAppointmentRepository repository, IUserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Appointment> create(@RequestBody @Valid Appointment appointment){
        var Existingappointment = this.repository.findByUserId(appointment.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Este usuario ja tem um horario marcado")
        );

        var newAppointment = this.repository.save(appointment);
        if(newAppointment.getHorary().isBefore(OffsetDateTime.now()))
            throw new InvalidCredentialsException("Horario precisa depois do horario atual");
        if(newAppointment.getHorary().isAfter(OffsetDateTime.now().plusMonths(3)))
            throw new InvalidCredentialsException("Precisa ser menos de 3 meses");

        var user = this.userRepository.findById(newAppointment.getUser().getId());
        user.get().setAppointment(newAppointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAppointment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getById(@PathVariable UUID id){
        Appointment appointment = repository.findById(id).orElseThrow(
                () -> new InvalidCredentialsException("Usuario não encontrado")
        );

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(appointment);
    }

    @GetMapping
    public ResponseEntity<List> getAllAppointment(){
        List allAppointment = this.repository.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(allAppointment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> update(@PathVariable UUID id, @RequestBody Appointment appointment){
        var existingAppointment = this.repository.findById(id).orElseThrow(
                () -> new InvalidCredentialsException("Horario não cadastrado no sistema.")
        );

        BeanUtils.copyProperties(appointment, existingAppointment, Utils.getNullPropertyNames(appointment));

        var newUser = this.repository.save(existingAppointment);
        newUser.setId(existingAppointment.getId());
        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        Appointment toDeleteUser = null;
        if(id != null) toDeleteUser = this.repository.findById(id).orElseThrow(
                () -> new InvalidCredentialsException("Horario não cadastrado")
        );

        this.repository.delete(toDeleteUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
