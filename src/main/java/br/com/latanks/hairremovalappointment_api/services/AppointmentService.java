package br.com.latanks.hairremovalappointment_api.services;

import br.com.latanks.hairremovalappointment_api.dtos.AppointmentMapper;
import br.com.latanks.hairremovalappointment_api.dtos.request.CreateAppointmentDTO;
import br.com.latanks.hairremovalappointment_api.dtos.request.UpdateAppointmentDTO;
import br.com.latanks.hairremovalappointment_api.dtos.request.UpdateStatusApptDTO;
import br.com.latanks.hairremovalappointment_api.dtos.response.CreatedAppointmentDTO;
import br.com.latanks.hairremovalappointment_api.exceptions.impl.InvalidCredentialsException;
import br.com.latanks.hairremovalappointment_api.models.Appointment;
import br.com.latanks.hairremovalappointment_api.models.enums.Areas;
import br.com.latanks.hairremovalappointment_api.repositories.IAppointmentRepository;
import br.com.latanks.hairremovalappointment_api.repositories.IUserRepository;
import br.com.latanks.hairremovalappointment_api.utils.AuthUtils;
import br.com.latanks.hairremovalappointment_api.utils.Utils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        String email = AuthUtils.getAuthenticatedEmail();

        var user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Usuario não encontrado"));

        if(this.appointmentRepository.findByUserId(user.getId()).isPresent())
            throw new DataIntegrityViolationException("Este usuario ja tem um horario");

        if(appointment.horary().isBefore(LocalDateTime.now()))
            throw new InvalidCredentialsException("Horario precisa depois do horario atual");
        if(appointment.horary().isAfter(LocalDateTime.now().plusMonths(3)))
            throw new InvalidCredentialsException("Precisa ser menos de 3 meses");



        var entity = this.mapper.toEntity(appointment);

        entity.setUser(user);
        user.setAppointment(entity);

        return this.mapper.toResponseDTO(this.appointmentRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public String getHairRemovalAreasList(){
        var list = Stream.of(Areas.values()).map(Enum::name).collect(Collectors.joining(","));
        return list;
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

    @Transactional(readOnly = true)
    public CreatedAppointmentDTO getMy() {
        String email = AuthUtils.getAuthenticatedEmail();
        var user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Usuário não encontrado"));

        var appointment = this.appointmentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new InvalidCredentialsException("Nenhum agendamento encontrado"));

        return this.mapper.toResponseDTO(appointment);
    }

    @Transactional
    public CreatedAppointmentDTO update(UUID id, UpdateAppointmentDTO appointment){
        var existingAppointment = this.appointmentRepository.findById(id).orElseThrow(
                () -> new InvalidCredentialsException("Horario não cadastrado no sistema.")
        );

        Utils.copyProperties(appointment, existingAppointment);

        return this.mapper.toResponseDTO(this.appointmentRepository.save(existingAppointment));
    }

    @Transactional
    public CreatedAppointmentDTO updateStatus(UUID id, UpdateStatusApptDTO situation){
        var existingAppointment = this.appointmentRepository.findById(id).orElseThrow(
                () -> new InvalidCredentialsException("Horario não cadastrado no sistema.")
        );

        Utils.copyProperties(situation, existingAppointment);

        return this.mapper.toResponseDTO(this.appointmentRepository.save(existingAppointment));
    }
}
