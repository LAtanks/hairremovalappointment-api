package br.com.latanks.hairremovalappointment_api.controllers;


import br.com.latanks.hairremovalappointment_api.dtos.request.UpdateUserDTO;
import br.com.latanks.hairremovalappointment_api.dtos.response.CreatedUserDTO;
import br.com.latanks.hairremovalappointment_api.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.base-url}/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<CreatedUserDTO> getUserByName(@PathVariable String name){
        return ResponseEntity.ok(this.userService.getUserByName(name));
    }

    @GetMapping("/my")
    public ResponseEntity<CreatedUserDTO> getMy() {
        return ResponseEntity.ok(this.userService.getMy());
    }

    @GetMapping
    public ResponseEntity<List<CreatedUserDTO>> getRegistredUsers(){
        return ResponseEntity.ok(this.userService.getRegisteredsUsers());
    }

    @DeleteMapping("/")
    public ResponseEntity delete(@RequestParam(required = false) UUID id, @RequestParam(required = false) String name){
        this.userService.delete(id, name);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreatedUserDTO> update(@PathVariable UUID id, @RequestBody UpdateUserDTO user){
        return ResponseEntity.ok(this.userService.update(id, user));
    }

    @PatchMapping("/{id}/promote")
    public ResponseEntity<CreatedUserDTO> promote(@PathVariable UUID id) {
        return ResponseEntity.ok(this.userService.promoteToAdmin(id));
    }
}
