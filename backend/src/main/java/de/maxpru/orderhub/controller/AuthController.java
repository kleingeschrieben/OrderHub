package de.maxpru.orderhub.controller;

import de.maxpru.orderhub.dto.LoginResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Login for users")
public class AuthController {

    // TODO IMPL SESSIONS
    @GetMapping("/login")
    public LoginResponse login(Authentication authentication) {
        String username = authentication.getName();
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            authorities.add(authority.getAuthority());
        }
        return new LoginResponse(username, authorities);
    }

}
