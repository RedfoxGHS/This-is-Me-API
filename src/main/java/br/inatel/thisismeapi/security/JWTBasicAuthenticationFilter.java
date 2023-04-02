package br.inatel.thisismeapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class JWTBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTBasicAuthenticationFilter.class);

    @Value("${private.key.default}")
    private String PRIVATE_KEY_DEFAULT;

    public JWTBasicAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.info("m=doFilterInternal");
        String jwt;
        Cookie token = WebUtils.getCookie(request, "token");

        if (token == null) {
            LOGGER.info("m=doFilterInternal, status=token is null");
            chain.doFilter(request, response);
            return;
        }

        try {
            jwt = token.getValue();
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(this.PRIVATE_KEY_DEFAULT))
                    .build()
                    .verify(jwt);
            LOGGER.info("m=doFilterInternal, status=validated token");

            String email = decodedJWT.getClaim("email").asString();
            String[] roles = decodedJWT.getClaim("auths").asArray(String.class);


            Claim c = decodedJWT.getClaim("auths");
            String[] auths = c.asArray(String.class);

            Set<GrantedAuthority> ga = new HashSet<>();

            for (String auth : auths) {
                ga.add(new SimpleGrantedAuthority(auth));
            }


            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, null, ga);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            super.doFilterInternal(request, response, chain);
        } catch (JWTVerificationException e) {
            LOGGER.info("m=doFilterInternal, errorMsg={}", e.getMessage());
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
