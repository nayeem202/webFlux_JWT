package com.example.webflux_jwt.configs.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class Enablesecurity {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService(PasswordEncoder encoder){
       UserDetails user = User.builder()
               .username("nayeem")
               .password(encoder.encode("123456"))
               .roles("USER")
               .build();
       return new MapReactiveUserDetailsService(user);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){return http
                .authorizeExchange(auth -> {
                    //auth.anyExchange().permitAll(); //every path is going to be open
                    auth.pathMatchers("/login").permitAll();
                    auth.anyExchange().authenticated();
                })
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .build();
    }


}
