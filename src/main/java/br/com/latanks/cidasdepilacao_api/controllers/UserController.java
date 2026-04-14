package br.com.latanks.cidasdepilacao_api.controllers;

import br.com.latanks.cidasdepilacao_api.exceptions.impl.InvalidCredentialsException;
import br.com.latanks.cidasdepilacao_api.models.User;
import br.com.latanks.cidasdepilacao_api.repositories.IUserRepository;
import br.com.latanks.cidasdepilacao_api.utils.Utils;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyAccessorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private final IUserRepository userRepository;

    public UserController(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/")
    public ResponseEntity<User> create(@RequestBody @Valid User user){
        this.userRepository.findByName(user.getName()).ifPresent(u -> {
                    throw new RuntimeException("Este usuario ja é existe " + user.getName());
                });

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
