package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.TokenDTO;
import br.com.alura.forum.controller.form.FormLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;

class AutenticacaoControllerTest {

    private AuthenticationManager authenticationManager;
    private TokenService tokenService;
    private AutenticacaoController controller;

    @BeforeEach
    void setUp() {
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        tokenService = Mockito.mock(TokenService.class);
        controller = new AutenticacaoController(authenticationManager, tokenService);
    }

    @Test
    void autenticaComSucesso() {
        FormLogin form = Mockito.mock(FormLogin.class);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("user", "pass");
        Mockito.when(form.converter()).thenReturn(token);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(token)).thenReturn(authentication);
        Mockito.when(tokenService.gerarToken(authentication)).thenReturn("meu-token");

        ResponseEntity<TokenDTO> response = controller.atenticacao(form);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("meu-token", response.getBody().getToken());
        assertEquals("Bearer", response.getBody().getTipoAutenticacao());
    }

    @Test
    void autenticaComErro() {
        FormLogin form = Mockito.mock(FormLogin.class);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("user", "pass");
        Mockito.when(form.converter()).thenReturn(token);
        Mockito.when(authenticationManager.authenticate(token)).thenThrow(Mockito.mock(AuthenticationException.class));

        ResponseEntity<TokenDTO> response = controller.atenticacao(form);
        assertEquals(400, response.getStatusCode().value());
        assertNull(response.getBody());
    }
}
