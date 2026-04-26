package br.com.latanks.cidasdepilacao_api.services;

import br.com.latanks.cidasdepilacao_api.dtos.request.LoginDTO;
import br.com.latanks.cidasdepilacao_api.dtos.response.TokenDTO;
import br.com.latanks.cidasdepilacao_api.exceptions.impl.InvalidCredentialsException;
import br.com.latanks.cidasdepilacao_api.repositories.IUserRepository;
import br.com.latanks.cidasdepilacao_api.security.JWTService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final IUserRepository userRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(IUserRepository userRepository,
                       JWTService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenDTO login(LoginDTO dto) {
        var user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new InvalidCredentialsException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword()))
            throw new InvalidCredentialsException("Email ou senha inválidos");

        String token = jwtService.generateToken(user.getEmail());
        System.out.println("Bearer " + token);
        System.out.println("é valido: " + jwtService.isValid(token));
        return new TokenDTO(token, "Bearer", user.getEmail(), user.getRoles().name());
    }
}
