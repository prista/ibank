package com.prista.netbanking.web.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component("customAuthenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String username = authentication.getPrincipal() + "";
        final String password = authentication.getCredentials() + "";

        if (!"admin".equals(username)) {
            throw new BadCredentialsException("1000");
        }
        if (false) { // locked user
            throw new DisabledException("1001");
        }
        if (!"nimda".equals(password)) {
            throw new BadCredentialsException("1000");
        }

        final int userId = 1; // FIXME: it should be the real user id from DB

        final List<SimpleGrantedAuthority> roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return new ExtendedUsernamePasswordAuthenticationToken(userId, username, password, roles);

    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
