package br.com.latanks.cidasdepilacao_api.controllers;

import br.com.latanks.cidasdepilacao_api.exceptions.impl.InvalidCredentialsException;
import br.com.latanks.cidasdepilacao_api.exceptions.impl.InvalidPhoneNumberException;
import br.com.latanks.cidasdepilacao_api.models.Appointment;
import br.com.latanks.cidasdepilacao_api.models.User;
import br.com.latanks.cidasdepilacao_api.models.enums.Roles;
import br.com.latanks.cidasdepilacao_api.repositories.IAppointmentRepository;
import br.com.latanks.cidasdepilacao_api.repositories.IUserRepository;
import br.com.latanks.cidasdepilacao_api.utils.PhoneValidator;
import br.com.latanks.cidasdepilacao_api.utils.Utils;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IAppointmentRepository appointmentRepository;

    @PostMapping("/")
    public ResponseEntity<User> create(@RequestBody @Valid User user){

        Integer age = (user.getBirthday().getYear() - LocalDateTime.now().getYear());
        if(Math.abs(age)  <= 14 )
            throw new InvalidCredentialsException("Idade precisa ser maior que 12 anos");
        if(Utils.hasAnyNumber(user.getName()))
            throw new InvalidCredentialsException("Nome não pode conter numeros");

        if(userRepository.findByEmail(user.getEmail()).isPresent())
            throw new InvalidCredentialsException("Email ja cadastrado no sistema");

        if(!PhoneValidator.isValid(user.getPhoneNumber())
                || userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()
        )
            throw new InvalidPhoneNumberException("Formato de numero invalido ou numero ja cadastrado no sistema");

        var newUser = this.userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping("/{name}")
    public ResponseEntity<User> getUserByName(@PathVariable String name){
        User user = userRepository.findByName(name).orElseThrow(
                () -> new InvalidCredentialsException("Usuario não encontrado")
        );

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
    }

    @GetMapping
    public ResponseEntity<List> getRegistredUsers(){
        List allUsers = this.userRepository.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }

    @DeleteMapping("/")
    public ResponseEntity delete(@RequestParam(required = false) UUID id, @RequestParam(required = false) String name){
        User toDeleteUser = null;
        if(id != null) toDeleteUser = this.userRepository.findById(id).orElseThrow(
                () -> new InvalidCredentialsException("Usuario não cadastrado")
        );
        if(name != null || !name.isEmpty()) toDeleteUser = this.userRepository.findByName(name).orElseThrow(
                () -> new InvalidCredentialsException("Usuario não cadastrado")
        );

        this.userRepository.delete(toDeleteUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable UUID id, @RequestBody User user){
        var existingUser = this.userRepository.findById(id).orElseThrow(
                () -> new InvalidCredentialsException("Usuario não cadastrado no sistema.")
        );

        BeanUtils.copyProperties(user, existingUser, Utils.getNullPropertyNames(user));

        var newUser = this.userRepository.save(existingUser);
        newUser.setId(existingUser.getId());
        System.out.println(existingUser.toString());
        System.out.println(newUser.toString());
        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }
}
