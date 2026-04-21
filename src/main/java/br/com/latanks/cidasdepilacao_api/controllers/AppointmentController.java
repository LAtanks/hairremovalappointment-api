package br.com.latanks.cidasdepilacao_api.controllers;

import br.com.latanks.cidasdepilacao_api.dtos.request.CreateAppointmentDTO;
import br.com.latanks.cidasdepilacao_api.dtos.request.UpdateAppointmentDTO;
import br.com.latanks.cidasdepilacao_api.dtos.response.CreatedAppointmentDTO;
import br.com.latanks.cidasdepilacao_api.exceptions.impl.InvalidCredentialsException;
import br.com.latanks.cidasdepilacao_api.models.Appointment;
import br.com.latanks.cidasdepilacao_api.models.User;
import br.com.latanks.cidasdepilacao_api.repositories.IAppointmentRepository;
import br.com.latanks.cidasdepilacao_api.repositories.IUserRepository;
import br.com.latanks.cidasdepilacao_api.services.AppointmentService;
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
    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CreatedAppointmentDTO> create(@RequestBody @Valid CreateAppointmentDTO appointment){
        var newAppointment = this.service.create(appointment);

        return ResponseEntity.status(HttpStatus.CREATED).body(newAppointment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreatedAppointmentDTO> getById(@PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CreatedAppointmentDTO>> getRegisteredsAppointments(){
        return ResponseEntity.status(HttpStatus.OK).body(this.service.getRegisteredsAppointments());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreatedAppointmentDTO> update(@PathVariable("id") UUID id, @RequestBody UpdateAppointmentDTO appointment){
        var newAppointment = this.service.update(id, appointment);
        return ResponseEntity.status(HttpStatus.OK).body(newAppointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        this.service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
