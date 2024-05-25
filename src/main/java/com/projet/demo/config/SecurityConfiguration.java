package com.projet.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.projet.demo.model.Permission.*;
import static com.projet.demo.model.Role.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf()
            .disable()
            .authorizeHttpRequests()
            .requestMatchers("/api/v1/auth/**").permitAll()

            .requestMatchers("/api/v1/client/**").hasAnyRole(ADMIN.name(), AGENT.name())
            .requestMatchers(GET, "/api/v1/client/**").hasAnyAuthority(ADMIN_READ.name(), AGENT_READ.name())
            .requestMatchers(POST, "/api/v1/client/**").hasAnyAuthority(ADMIN_CREATE.name(), AGENT_CREATE.name())
            .requestMatchers(PUT, "/api/v1/client/**").hasAnyAuthority(ADMIN_UPDATE.name(), AGENT_UPDATE.name())
            .requestMatchers(DELETE, "/api/v1/client/**").hasAnyAuthority(ADMIN_DELETE.name(), AGENT_DELETE.name())

            .requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())
            .requestMatchers(GET, "/api/v1/admin/**").hasAuthority(ADMIN_READ.name())
            .requestMatchers(POST, "/api/v1/admin/**").hasAuthority(ADMIN_CREATE.name())
            .requestMatchers(PUT, "/api/v1/admin/**").hasAuthority(ADMIN_UPDATE.name())
            .requestMatchers(DELETE, "/api/v1/admin/**").hasAuthority(ADMIN_DELETE.name())

            .requestMatchers("/fim/est3Dgate/**").hasRole(CLIENT.name())
            .requestMatchers(GET, "/fim/est3Dgate/**").hasAuthority(CLIENT_READ.name())
            .requestMatchers(POST, "/fim/est3Dgate/**").hasAuthority(CLIENT_CREATE.name())
            .requestMatchers(PUT, "/fim/est3Dgate/**").hasAuthority(CLIENT_UPDATE.name())
            .requestMatchers(DELETE, "/fim/est3Dgate/**").hasAuthority(CLIENT_DELETE.name())

            .requestMatchers("/client/**").hasRole(CLIENT.name())
            .requestMatchers(GET, "/client/**").hasAuthority(CLIENT_READ.name())

            .requestMatchers("/fim/est3DgateV2/**").hasRole(AGENT.name())
            .requestMatchers(POST, "/fim/est3DgateV2/**").hasAuthority(AGENT_CREATE.name())

            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    http.cors();
    return http.build();
  }
}
