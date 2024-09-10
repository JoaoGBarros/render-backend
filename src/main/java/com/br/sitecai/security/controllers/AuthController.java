package com.br.sitecai.security.controllers;

import com.br.sitecai.security.dto.AuthDTO;
import com.br.sitecai.security.dto.AuthResponseDTO;
import com.br.sitecai.security.jwt.JwtGenerator;
import com.br.sitecai.user.entity.Usuario;
import com.br.sitecai.user.enums.UserRole;
import com.br.sitecai.user.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;

    private PasswordEncoder passowordEncoder;

    private JwtGenerator jwtGenerator;

    @Autowired
    public AuthController(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passowordEncoder,
                          JwtGenerator jwtGenerator) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passowordEncoder = passowordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthDTO authDto){
        if(userRepository.existsByUsername(authDto.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario ja existe!");
        }


        Usuario user = new Usuario();
        BeanUtils.copyProperties(authDto, user);
        user.setPassword(passowordEncoder.encode(authDto.getPassword()));
        user.setUserRole(UserRole.ADMIN);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario criado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthDTO authDto){
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);

        String token = jwtGenerator.generateToken(auth);

        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponseDTO(token));
    }
}
