package br.inatel.thisismeapi.services.exception;

import br.inatel.thisismeapi.config.exceptions.StandardError;
import br.inatel.thisismeapi.services.exceptions.ServicesExceptionsHandler;
import br.inatel.thisismeapi.services.exceptions.TokenExpiredException;
import br.inatel.thisismeapi.services.exceptions.TokenInvalidException;
import br.inatel.thisismeapi.services.exceptions.UnregisteredUserException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ServletWebServerFactoryAutoConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@ActiveProfiles("dev")
class ServicesExceptionsHandlerTest {

    private ServicesExceptionsHandler servicesExceptionsHandler;

    @Mock
    HttpServletRequest httpServletRequest;

    ServicesExceptionsHandlerTest() {
        servicesExceptionsHandler = new ServicesExceptionsHandler();
    }

    @Test
    void testUnregisteredUserException() {
        UnregisteredUserException unregisteredUserException = new UnregisteredUserException("msg");

        ResponseEntity<StandardError> responseEntity = servicesExceptionsHandler.unregisteredUserException(
                unregisteredUserException, httpServletRequest);

        assertEquals("msg",
                Objects.requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED.getReasonPhrase(), responseEntity.getBody().getError());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseEntity.getBody().getStatus());
    }

    @Test
    void testTokenHandlerException() {

        TokenExpiredException tokenHandler = new TokenExpiredException("msg");
        ResponseEntity<StandardError> responseEntity = servicesExceptionsHandler.tokenHandler(
                tokenHandler, httpServletRequest);

        assertEquals("msg",
                Objects.requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED.getReasonPhrase(), responseEntity.getBody().getError());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseEntity.getBody().getStatus());
    }

    @Test
    void testNewInstanceOfTokenInvalidException() {

        TokenInvalidException exception = new TokenInvalidException("msg");

        assertEquals(TokenInvalidException.class, exception.getClass());
        assertEquals("msg", exception.getMessage());
    }
}
