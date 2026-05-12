package br.com.latanks.hairremovalappointment_api.services;

import br.com.latanks.hairremovalappointment_api.dtos.UserMapper;
import br.com.latanks.hairremovalappointment_api.dtos.request.CreateUserDTO;
import br.com.latanks.hairremovalappointment_api.dtos.request.UpdateUserDTO;
import br.com.latanks.hairremovalappointment_api.dtos.response.CreatedUserDTO;
import br.com.latanks.hairremovalappointment_api.exceptions.impl.InvalidCredentialsException;
import br.com.latanks.hairremovalappointment_api.exceptions.impl.InvalidPhoneNumberException;
import br.com.latanks.hairremovalappointment_api.models.User;
import br.com.latanks.hairremovalappointment_api.models.enums.Roles;
import br.com.latanks.hairremovalappointment_api.repositories.IAppointmentRepository;
import br.com.latanks.hairremovalappointment_api.repositories.IUserRepository;
import br.com.latanks.hairremovalappointment_api.utils.AuthUtils;
import br.com.latanks.hairremovalappointment_api.utils.PhoneValidator;
import br.com.latanks.hairremovalappointment_api.utils.Utils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserMapper mapper;
    private final IUserRepository userRepository;
    private final IAppointmentRepository appointmentRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper mapper, IUserRepository userRepository, IAppointmentRepository appointmentRepository, PasswordEncoder passwordEncoder){
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public CreatedUserDTO getMy() {
        String email = AuthUtils.getAuthenticatedEmail();
        var user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Usuário não encontrado"));
        return this.mapper.toResponseDTO(user);
    }

    @Transactional
    public CreatedUserDTO create(CreateUserDTO dto){
        Period age = Period.between(dto.birthday(), LocalDate.now());

        if(age.getYears() <= 14)
            throw new InvalidCredentialsException("Idade precisa ser maior que 12 anos");
        if(Utils.hasAnyNumber(dto.name()))
            throw new InvalidCredentialsException("Nome não pode conter numeros");

        if(userRepository.findByEmail(dto.email()).isPresent())
            throw new InvalidCredentialsException("Email ja cadastrado no sistema");

        if(!PhoneValidator.isValid(dto.phoneNumber())
                || userRepository.findByPhoneNumber(dto.phoneNumber()).isPresent()
        )
            throw new InvalidPhoneNumberException("Formato de numero invalido ou numero ja cadastrado no sistema");

        User user = this.mapper.toEntity(dto);

        user.setPassword(passwordEncoder.encode(dto.password()));

        User saved = this.userRepository.save(user);

        return mapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public CreatedUserDTO getUserByName(String name){
        User user = this.userRepository.findByName(name).orElseThrow(
                () -> new InvalidCredentialsException("Usuario não encontrado")
        );

        return this.mapper.toResponseDTO(user);
    }

    @Transactional(readOnly = true)
    public List<CreatedUserDTO> getRegisteredsUsers(){
        return this.userRepository.findAll()
                .stream()
                .map(this.mapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public void delete(UUID id, String name){
        User toDeleteUser = null;

        if(id != null) toDeleteUser = this.userRepository.findById(id).orElseThrow(
                () -> new InvalidCredentialsException("Usuario não cadastrado")
        );
        if(name != null && !name.isEmpty()) toDeleteUser = this.userRepository.findByName(name).orElseThrow(
                () -> new InvalidCredentialsException("Usuario não cadastrado")
        );

        this.userRepository.delete(toDeleteUser);
    }

    @Transactional
    public CreatedUserDTO update(UUID id, UpdateUserDTO user){
        var existingUser = this.userRepository.findById(id).orElseThrow(
                () -> new InvalidCredentialsException("Usuario não cadastrado no sistema.")
        );

        if(Utils.hasAnyNumber(user.name()))
            throw new InvalidCredentialsException("Nome de usuario não poderá conter numeros");

        Utils.copyProperties(user, existingUser);

        return this.mapper.toResponseDTO(this.userRepository.save(existingUser));
    }


    @Transactional
    public CreatedUserDTO promoteToAdmin(UUID id) {
        var user = this.userRepository.findById(id)
                .orElseThrow(() -> new InvalidCredentialsException("Usuário não cadastrado"));

        if (user.getRoles() == Roles.ADMIN)
            throw new InvalidCredentialsException("Usuário já é administrador");

        user.setRoles(Roles.ADMIN);

        return this.mapper.toResponseDTO(this.userRepository.save(user));
    }
}
