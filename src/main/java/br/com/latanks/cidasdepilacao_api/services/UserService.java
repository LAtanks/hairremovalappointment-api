package br.com.latanks.cidasdepilacao_api.services;

import br.com.latanks.cidasdepilacao_api.dtos.UserMapper;
import br.com.latanks.cidasdepilacao_api.dtos.request.CreateUserDTO;
import br.com.latanks.cidasdepilacao_api.dtos.request.UpdateUserDTO;
import br.com.latanks.cidasdepilacao_api.dtos.response.CreatedUserDTO;
import br.com.latanks.cidasdepilacao_api.exceptions.impl.InvalidCredentialsException;
import br.com.latanks.cidasdepilacao_api.exceptions.impl.InvalidPhoneNumberException;
import br.com.latanks.cidasdepilacao_api.models.User;
import br.com.latanks.cidasdepilacao_api.repositories.IAppointmentRepository;
import br.com.latanks.cidasdepilacao_api.repositories.IUserRepository;
import br.com.latanks.cidasdepilacao_api.utils.PhoneValidator;
import br.com.latanks.cidasdepilacao_api.utils.Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserMapper mapper;
    private final IUserRepository userRepository;
    private final IAppointmentRepository appointmentRepository;

    public UserService(UserMapper mapper, IUserRepository userRepository, IAppointmentRepository appointmentRepository){
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public CreatedUserDTO create(CreateUserDTO dto){
        int age = Math.abs(dto.birthday().getYear() - LocalDateTime.now().getYear());

        if(age  <= 14 )
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
}
