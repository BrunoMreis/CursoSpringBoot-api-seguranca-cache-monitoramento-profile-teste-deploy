package br.com.alura.forum.controller.config.security;

import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;

@Service
@Profile("prod")
public class AutenticacaoService implements UserDetailsService {

	private UsuarioRepository repository;

	public AutenticacaoService(UsuarioRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuarioOptionl = repository.findByEmail(username);

		if (usuarioOptionl.isPresent()) {

			return usuarioOptionl.get();
		}

		throw new UsernameNotFoundException("Usuário ou senha inválido");
	}

}
