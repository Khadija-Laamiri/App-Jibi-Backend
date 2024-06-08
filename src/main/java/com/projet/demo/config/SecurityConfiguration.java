package com.projet.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.projet.demo.entity.Permission.*;
import static com.projet.demo.entity.Role.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutHandler logoutHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf().disable()
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
            .requestMatchers("/cmi/service/**").hasRole(CLIENT.name())
            .requestMatchers(GET, "/cmi/service/**").hasAuthority(CLIENT_READ.name())
            .requestMatchers(POST, "/cmi/service/**").hasAuthority(CLIENT_CREATE.name())
            .requestMatchers(PUT, "/cmi/service/**").hasAuthority(CLIENT_UPDATE.name())
            .requestMatchers(DELETE, "/cmi/service/**").hasAuthority(CLIENT_DELETE.name())
            .requestMatchers("/client/infos/**").hasRole(CLIENT.name())
            .requestMatchers(GET, "/client/infos/**").hasAuthority(CLIENT_READ.name())
            .requestMatchers(POST, "/client/infos/**").hasAuthority(CLIENT_CREATE.name())
            .requestMatchers(PUT, "/client/infos/**").hasAuthority(CLIENT_UPDATE.name())
            .requestMatchers(DELETE, "/client/infos/**").hasAuthority(CLIENT_DELETE.name())
            .requestMatchers("/client/**").hasRole(CLIENT.name())
            .requestMatchers(GET, "/client/**").hasAuthority(CLIENT_READ.name())
            .requestMatchers("/fim/est3DgateV2/**").hasRole(AGENT.name())
            .requestMatchers(POST, "/fim/est3DgateV2/**").hasAuthority(AGENT_CREATE.name())
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .logout()
            .logoutUrl("/api/v1/auth/logout")
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

    http.cors(); // Enable CORS
    return http.build();
  }
}
