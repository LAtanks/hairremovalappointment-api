package br.com.latanks.cidasdepilacao_api.services;

import br.com.latanks.cidasdepilacao_api.dtos.AppointmentMapper;
import br.com.latanks.cidasdepilacao_api.dtos.request.CreateAppointmentDTO;
import br.com.latanks.cidasdepilacao_api.dtos.request.UpdateAppointmentDTO;
import br.com.latanks.cidasdepilacao_api.dtos.response.CreatedAppointmentDTO;
import br.com.latanks.cidasdepilacao_api.exceptions.impl.InvalidCredentialsException;
import br.com.latanks.cidasdepilacao_api.models.Appointment;
import br.com.latanks.cidasdepilacao_api.repositories.IAppointmentRepository;
import br.com.latanks.cidasdepilacao_api.repositories.IUserRepository;
import br.com.latanks.cidasdepilacao_api.utils.Utils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {


    private final AppointmentMapper mapper;
    private final IUserRepository userRepository;
    private final IAppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentMapper mapper, IUserRepository userRepository, IAppointmentRepository appointmentRepository){
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public CreatedAppointmentDTO create(CreateAppointmentDTO appointment){
        if(this.appointmentRepository.findByUserId(appointment.userId()).isPresent())
            throw new DataIntegrityViolationException("Este usuario ja tem um horario");

        if(appointment.horary().isBefore(LocalDateTime.now()))
            throw new InvalidCredentialsException("Horario precisa depois do horario atual");
        if(appointment.horary().isAfter(LocalDateTime.now().plusMonths(3)))
            throw new InvalidCredentialsException("Precisa ser menos de 3 meses");

        var user = this.userRepository.findById(appointment.userId())
                .orElseThrow(() -> new InvalidCredentialsException("Usuario não encontrado"));

        var entity = this.mapper.toEntity(appointment);

        entity.setUser(user);
        user.setAppointment(entity);

        return this.mapper.toResponseDTO(this.appointmentRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public CreatedAppointmentDTO getById(UUID id){
        var entity = this.appointmentRepository.findByUserId(id)
                .orElseThrow(() -> new InvalidCredentialsException("Horario não encontrado"));
        return this.mapper.toResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<CreatedAppointmentDTO> getRegisteredsAppointments(){
        return this.appointmentRepository.findAll()
                .stream()
                .map(this.mapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public void delete(UUID id){
        Appointment toDelete = null;
        if(id != null) toDelete = this.appointmentRepository.findById(id).orElseThrow(
                () -> new InvalidCredentialsException("Horario não cadastrado")
        );

        this.appointmentRepository.delete(toDelete);
    }

    @Transactional
    public CreatedAppointmentDTO update(UUID id, UpdateAppointmentDTO appointment){
        var existingAppointment = this.appointmentRepository.findById(id).orElseThrow(
                () -> new InvalidCredentialsException("Horario não cadastrado no sistema.")
        );

        Utils.copyProperties(appointment, existingAppointment);

        return this.mapper.toResponseDTO(this.appointmentRepository.save(existingAppointment));
    }
}
