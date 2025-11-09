package br.com.alura.forum.controller.config.security;

import br.com.alura.forum.controller.TokenService;
import br.com.alura.forum.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigurationsTest {

    private TokenService tokenService;
    private UsuarioRepository usuarioRepository;
    private SecurityConfigurations configurations;

    @BeforeEach
    void setUp() {
        tokenService = Mockito.mock(TokenService.class);
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        configurations = new SecurityConfigurations(tokenService, usuarioRepository);
    }

    @Test
    void listarChamaRepositoryFindAll() {
        Mockito.when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());
        configurations.listar();
        Mockito.verify(usuarioRepository, Mockito.times(1)).findAll();
    }

    @Test
    void passwordEncoderIsBCrypt() {
        Object encoder = configurations.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
        BCryptPasswordEncoder bcrypt = (BCryptPasswordEncoder) encoder;
        // deepcode ignore HardcodedPassword/test: this is a test scenario
        String raw = "senha123";
        String encoded = bcrypt.encode(raw);
        assertTrue(bcrypt.matches(raw, encoded));
    }

    @Test
    void authenticationManagerDelegatesToConfiguration() throws Exception {
        AuthenticationManager mockManager = Mockito.mock(AuthenticationManager.class);
        AuthenticationConfiguration authConfig = Mockito.mock(AuthenticationConfiguration.class);
        Mockito.when(authConfig.getAuthenticationManager()).thenReturn(mockManager);

        AuthenticationManager result = configurations.authenticationManager(authConfig);
        assertSame(mockManager, result);
    }
}
