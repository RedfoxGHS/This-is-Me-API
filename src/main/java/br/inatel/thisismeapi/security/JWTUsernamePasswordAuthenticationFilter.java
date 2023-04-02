package br.inatel.thisismeapi.security;

import br.inatel.thisismeapi.controllers.dtos.requests.UserLoginRequestDTO;
import br.inatel.thisismeapi.models.Roles;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTUsernamePasswordAuthenticationFilter.class);

    private final AuthenticationManager authManager;

    @Value("${private.key.default}")
    private String PRIVATE_KEY_DEFAULT;

    public JWTUsernamePasswordAuthenticationFilter(AuthenticationManager authManager) {
        super();
        this.authManager = authManager;
        this.setFilterProcessesUrl("/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LOGGER.info("m=attemptAuthentication, status=trying authenticate");
        UserLoginRequestDTO authRequest = null;
        try {
            authRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UserLoginRequestDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(authRequest.getEmail().toLowerCase(),
                authRequest.getPassword());
        LOGGER.info("m=attemptAuthentication, email={}", authRequest.getEmail());

        return authManager.authenticate(auth);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        UserPrincipal user = (UserPrincipal) authResult.getPrincipal();
        String email = user.getUsername();
        LOGGER.info("m=successfulAuthentication, username={}", user.getUsername());

        String[] grant_auth = new String[authResult.getAuthorities().size()];
        Object[] grants = authResult.getAuthorities().toArray();

        for (int i = 0; i < authResult.getAuthorities().size(); i++) {
            Roles roles = (Roles) grants[i];
            grant_auth[i] = roles.getRoleName().toString();
        }


        String jwt = JWT.create()
                .withClaim("email", email)
                .withArrayClaim("auths", grant_auth)
                .sign(Algorithm.HMAC256(this.PRIVATE_KEY_DEFAULT));
        Cookie cookie = new Cookie("token", jwt);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 30);

        response.addCookie(cookie);

        response.sendRedirect("/character/get-character");

        LOGGER.info("m=successfulAuthentication, status=TokenJWTCreated");
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        LOGGER.info("m=unsuccessfulAuthentication, msg={}", failed.getMessage());
        response.reset();
        response.resetBuffer();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        response.getWriter().write(failed.getLocalizedMessage());
        response.getWriter().flush();
    }
}
