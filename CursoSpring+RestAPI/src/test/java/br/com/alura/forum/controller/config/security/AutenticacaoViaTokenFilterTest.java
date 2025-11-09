package br.com.alura.forum.controller.config.security;

import br.com.alura.forum.controller.TokenService;
import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AutenticacaoViaTokenFilterTest {
    private TokenService tokenService;
    private UsuarioRepository usuarioRepository;
    private AutenticacaoViaTokenFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        tokenService = Mockito.mock(TokenService.class);
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        filter = new AutenticacaoViaTokenFilter(tokenService, usuarioRepository);
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        filterChain = Mockito.mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    @Test
    void autenticaComTokenValido() throws ServletException, IOException {
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        Mockito.when(tokenService.isValido("token123")).thenReturn(true);
        Mockito.when(tokenService.getIdUsuario("token123")).thenReturn(1L);
        Usuario usuario = Mockito.mock(Usuario.class);
        Mockito.when(usuario.getAuthorities()).thenReturn(null);
        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        filter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void naoAutenticaComTokenInvalido() throws ServletException, IOException {
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        Mockito.when(tokenService.isValido("token123")).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        Mockito.verify(filterChain).doFilter(request, response);
    }

}
