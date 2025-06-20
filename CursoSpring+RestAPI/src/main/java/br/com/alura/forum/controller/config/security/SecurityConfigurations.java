package br.com.alura.forum.controller.config.security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.alura.forum.controller.TokenService;
import br.com.alura.forum.repository.UsuarioRepository;

@EnableWebSecurity
@Configuration
@Profile("prod")
public class SecurityConfigurations {

    private final AutenticacaoService autenticacaoService;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/auth").permitAll()
                .requestMatchers("/topicos").permitAll()
                .requestMatchers("/listar/*").permitAll()
                .requestMatchers("/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**").permitAll()
                .requestMatchers("/**.html").permitAll()
                .requestMatchers("/topicos/*").hasRole("MODERADOR")
                .anyRequest().authenticated()
            )
            .addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository),
                    UsernamePasswordAuthenticationFilter.class);

        // Para acessar o H2-console
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
