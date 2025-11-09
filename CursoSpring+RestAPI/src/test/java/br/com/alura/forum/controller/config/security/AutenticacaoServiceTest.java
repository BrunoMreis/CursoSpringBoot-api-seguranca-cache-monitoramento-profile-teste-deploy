package br.com.alura.forum.controller.config.security;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AutenticacaoServiceTest {
    private UsuarioRepository repository;
    private AutenticacaoService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(UsuarioRepository.class);
        service = new AutenticacaoService(repository);
    }

    @Test
    void retornaUsuarioQuandoEmailExiste() {
        Usuario usuario = Mockito.mock(Usuario.class);
        Mockito.when(repository.findByEmail("email@email.com")).thenReturn(Optional.of(usuario));
        UserDetails result = service.loadUserByUsername("email@email.com");
        assertNotNull(result);
        assertEquals(usuario, result);
    }

    @Test
    void lancaExcecaoQuandoEmailNaoExiste() {
        Mockito.when(repository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("naoexiste@email.com"));
    }
}
