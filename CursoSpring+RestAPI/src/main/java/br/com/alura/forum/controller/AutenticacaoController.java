package br.com.alura.forum.controller;



import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.controller.dto.TokenDTO;
import br.com.alura.forum.controller.form.FormLogin;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
@Profile("prod")
public class AutenticacaoController {

	private static final Logger log = LoggerFactory.getLogger(AutenticacaoController.class);

	private AuthenticationManager authenticationManager;
	private TokenService tokenService;

	public AutenticacaoController(AuthenticationManager authenticationManager, TokenService tokenService) {
		this.authenticationManager = authenticationManager;
		this.tokenService = tokenService;
	}

	@PostMapping
	public ResponseEntity<TokenDTO> atenticacao(@RequestBody @Valid FormLogin form) {
		
		UsernamePasswordAuthenticationToken dadosLongi = form.converter();

		try {
			Authentication authentication = authenticationManager.authenticate(dadosLongi);
			String token = tokenService.gerarToken(authentication);
			return ResponseEntity.ok(new TokenDTO(token,"Bearer"));
		} catch (AuthenticationException e) {
			log.error("Erro ao autenticar: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}

}
