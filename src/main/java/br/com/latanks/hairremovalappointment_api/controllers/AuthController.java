package br.com.latanks.hairremovalappointment_api.controllers;

import br.com.latanks.hairremovalappointment_api.dtos.request.CreateUserDTO;
import br.com.latanks.hairremovalappointment_api.dtos.request.LoginDTO;
import br.com.latanks.hairremovalappointment_api.dtos.response.CreatedUserDTO;
import br.com.latanks.hairremovalappointment_api.dtos.response.TokenDTO;
import br.com.latanks.hairremovalappointment_api.repositories.IUserRepository;
import br.com.latanks.hairremovalappointment_api.security.JWTService;
import br.com.latanks.hairremovalappointment_api.services.AuthService;
import br.com.latanks.hairremovalappointment_api.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base-url}/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final IUserRepository userRepository;
    private final JWTService jwtService;

    public AuthController(AuthService authService, UserService userService, IUserRepository userRepository, JWTService jwtService) {
        this.authService = authService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<CreatedUserDTO> register(@RequestBody @Valid CreateUserDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
