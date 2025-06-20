package br.com.alura.forum.controller;



import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/auth")
@Profile("prod")
public class AutenticacaoController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private TokenService tokenService;

	@PostMapping
	public ResponseEntity<TokenDTO> atenticacao(@RequestBody @Valid FormLogin form) {
		
		UsernamePasswordAuthenticationToken dadosLongi = form.converter();

		try {
			Authentication authentication = authenticationManager.authenticate(dadosLongi);
			String token = tokenService.gerarToken(authentication);
			return ResponseEntity.ok(new TokenDTO(token,"Bearer"));
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}

}
