package br.com.latanks.cidasdepilacao_api.controllers;

import br.com.latanks.cidasdepilacao_api.dtos.request.CreateAppointmentDTO;
import br.com.latanks.cidasdepilacao_api.dtos.request.UpdateAppointmentDTO;
import br.com.latanks.cidasdepilacao_api.dtos.response.CreatedAppointmentDTO;
import br.com.latanks.cidasdepilacao_api.services.AppointmentService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.base-url}/appointment")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CreatedAppointmentDTO> create(@RequestBody @Valid CreateAppointmentDTO appointment){
        var newAppointment = this.service.create(appointment);

        return ResponseEntity.status(HttpStatus.CREATED).body(newAppointment);
    }
    
    @GetMapping("/my")
    public ResponseEntity<CreatedAppointmentDTO> getMy() {
        return ResponseEntity.ok(this.service.getMy());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreatedAppointmentDTO> getById(@PathVariable UUID id){
        return ResponseEntity.ok(this.service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CreatedAppointmentDTO>> getRegisteredsAppointments(){
        return ResponseEntity.ok(this.service.getRegisteredsAppointments());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreatedAppointmentDTO> update(@PathVariable("id") UUID id, @RequestBody UpdateAppointmentDTO appointment){
        return ResponseEntity.ok(this.service.update(id, appointment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
