package br.com.alura.forum.controller.config.security;

import java.io.IOException;

import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.controller.TokenService;
import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Profile("prod")
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

	private TokenService tokenService;
	private UsuarioRepository usuarioRepository;

	public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull FilterChain filterChain)
			throws ServletException, IOException {

		String token = pegaToken(request);
		boolean valido = tokenService.isValido(token);
		if(valido) {
			autenticarCliente(token);
		}
		

		filterChain.doFilter(request, response);
	}



	private void autenticarCliente(String token) {
		
		Long idUsuario = tokenService.getIdUsuario(token);
		Usuario usuario = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
		
		UsernamePasswordAuthenticationToken authentication = 
				new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private String pegaToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");

		if (token == null || token.isBlank() || !token.startsWith("Bearer ")) {
			return null;
		}

		return token.substring(7, token.length());
	}

}
