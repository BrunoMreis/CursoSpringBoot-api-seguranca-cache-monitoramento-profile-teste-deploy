package br.com.alura.forum.controller.config.security;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.alura.forum.controller.TokenService;
import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;

@EnableWebSecurity
@Configuration
@Profile("prod")
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {


	private AutenticacaoService autenticacaoService;

	
	private TokenService tokenService;


	private UsuarioRepository usuarioRepository;
	
	
	@Autowired
	public SecurityConfigurations(AutenticacaoService autenticacaoService, TokenService tokenService,
			UsuarioRepository usuarioRepository) {
		this.autenticacaoService = autenticacaoService;
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
		
		
	}
	
	
	@PostConstruct
	public void listar() {
		System.err.println(usuarioRepository.findAll());
	}
	

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	// configurações de autenticação
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());

	
	}

	// configurações de autorizações
	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests().antMatchers(HttpMethod.GET, "/topicos").permitAll().antMatchers("/h2-console")
//				.permitAll().antMatchers(HttpMethod.GET, "/listar/*").permitAll().antMatchers(HttpMethod.POST, "/auth")
//				.permitAll().antMatchers(HttpMethod.DELETE, "/topicos/*").hasAnyRole("MODERADOR").anyRequest()
//				.authenticated().and().csrf().disable().sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//
//				.and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository),
//						UsernamePasswordAuthenticationFilter.class);
	
		http.authorizeRequests().anyRequest().permitAll();
	
	}

	// configurações de recursos estaticos(js, css, imagens, etc.)
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**",
				"/swagger-resources/**", "/h2-console", "http://localhost:8080/h2-console/**");
	}



}
