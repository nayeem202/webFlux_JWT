package com.example.webflux_jwt.configs.Security;

import com.example.webflux_jwt.services.JWTService;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
@SuppressWarnings("Class can be record")
@Component
public class AuthManager implements ReactiveAuthenticationManager {

    final JWTService jwtService;

    final ReactiveUserDetailsService users;

    public AuthManager(JWTService jwtService, ReactiveUserDetailsService users) {
        this.jwtService = jwtService;
        this.users = users;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return  Mono.justOrEmpty(
                authentication
        )
                .cast(BearerToken.class)
                .flatMap(auth ->{
                    String getUsername = jwtService.getUserName(auth.getCredentials());
                    Mono<UserDetails> foundUser = users.findByUsername(getUsername).defaultIfEmpty(new UserDetails() {
                        @Override
                        public Collection<? extends GrantedAuthority> getAuthorities() {
                            return null;
                        }

                        @Override
                        public String getPassword() {
                            return null;
                        }

                        @Override
                        public String getUsername() {
                            return null;
                        }

                        @Override
                        public boolean isAccountNonExpired() {
                            return false;
                        }

                        @Override
                        public boolean isAccountNonLocked() {
                            return false;
                        }

                        @Override
                        public boolean isCredentialsNonExpired() {
                            return false;
                        }

                        @Override
                        public boolean isEnabled() {
                            return false;
                        }
                    });

                  Mono<Authentication> authenticate = foundUser.flatMap(u -> {
                        if(u.getUsername() == null){
                            Mono.error(new IllegalArgumentException(""));
                        }
                        if (jwtService.validate(u, auth.getCredentials())){
                            return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(u.getUsername(),u.getPassword(),u.getAuthorities()));
                        }
                        Mono.error(new IllegalArgumentException("Invalid/Expired token"));
                        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(u.getUsername(),u.getPassword(),u.getAuthorities()));
                    });
                    return authenticate;
                });
    }

/*    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(
                authentication
        )
                .cast(BearerToken.class)

                .flatMap(auth ->{
                    String userName = jwtService.getUserName(auth.getCredentials());
                    Mono<UserDetails> foundUser = users.findByUsername(userName).defaultIfEmpty(new UserDetails() {
                        @Override
                        public Collection<? extends GrantedAuthority> getAuthorities() {
                            return null;
                        }

                        @Override
                        public String getPassword() {
                            return null;
                        }

                        @Override
                        public String getUsername() {
                            return null;
                        }

                        @Override
                        public boolean isAccountNonExpired() {
                            return false;
                        }

                        @Override
                        public boolean isAccountNonLocked() {
                            return false;
                        }

                        @Override
                        public boolean isCredentialsNonExpired() {
                            return false;
                        }

                        @Override
                        public boolean isEnabled() {
                            return false;
                        }
                    });
                  var a = foundUser.flatMap(u -> {
                      if(u.getUsername() == null){
                          Mono.error(new IllegalArgumentException("user not found in auth manager"))
                      }

                      if(jwtService.validate(u, auth.getCredentials())){
                          return new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword(), u.getAuthorities());
                      }

                      Mono.error(new IllegalArgumentException("Invalid/expired token"));
                      return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword(), u.getAuthorities()));
                  }

                  );

                    return a;
                }
                );
    }*/



























}
