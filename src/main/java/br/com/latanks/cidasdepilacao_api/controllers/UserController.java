package br.com.latanks.cidasdepilacao_api.controllers;

import br.com.latanks.cidasdepilacao_api.dtos.request.CreateUserDTO;
import br.com.latanks.cidasdepilacao_api.dtos.request.UpdateUserDTO;
import br.com.latanks.cidasdepilacao_api.dtos.response.CreatedUserDTO;
import br.com.latanks.cidasdepilacao_api.exceptions.impl.InvalidCredentialsException;
import br.com.latanks.cidasdepilacao_api.exceptions.impl.InvalidPhoneNumberException;
import br.com.latanks.cidasdepilacao_api.models.Appointment;
import br.com.latanks.cidasdepilacao_api.models.User;
import br.com.latanks.cidasdepilacao_api.models.enums.Roles;
import br.com.latanks.cidasdepilacao_api.repositories.IAppointmentRepository;
import br.com.latanks.cidasdepilacao_api.repositories.IUserRepository;
import br.com.latanks.cidasdepilacao_api.services.UserService;
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
    private UserService userService;
    @Autowired
    private IAppointmentRepository appointmentRepository;

    @PostMapping("/")
    public ResponseEntity<CreatedUserDTO> create(@RequestBody @Valid CreateUserDTO user){
        var newUser = this.userService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping("/{name}")
    public ResponseEntity<CreatedUserDTO> getUserByName(@PathVariable String name){
        var user = this.userService.getUserByName(name);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
    }

    @GetMapping
    public ResponseEntity<List> getRegistredUsers(){
        var allUsers = this.userService.getRegisteredsUsers();

        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }

    @DeleteMapping("/")
    public ResponseEntity delete(@RequestParam(required = false) UUID id, @RequestParam(required = false) String name){
        this.userService.delete(id, name);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreatedUserDTO> update(@PathVariable UUID id, @RequestBody UpdateUserDTO user){
        var newUser = this.userService.update(id, user);
        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }
}
