package br.com.alura.forum.controller.config.security;

import br.com.alura.forum.controller.TokenService;
import br.com.alura.forum.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.security.web.DefaultSecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("prod")
class SecurityConfigurationsIntegrationTest {

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Autowired
    private org.springframework.security.web.SecurityFilterChain securityFilterChain;

    @Test
    void securityFilterChainContainsAutenticacaoViaTokenFilter() {
        assertNotNull(securityFilterChain);
        assertTrue(securityFilterChain instanceof DefaultSecurityFilterChain,
                "Expected DefaultSecurityFilterChain implementation");
        DefaultSecurityFilterChain chain = (DefaultSecurityFilterChain) securityFilterChain;
        boolean has = chain.getFilters().stream().anyMatch(f -> f instanceof AutenticacaoViaTokenFilter);
        assertTrue(has, "AutenticacaoViaTokenFilter should be present in the filter chain");
    }
}
