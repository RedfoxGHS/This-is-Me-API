package br.inatel.thisismeapi.units.utils;

import br.inatel.thisismeapi.units.classesToTest.EmailConstToTest;
import br.inatel.thisismeapi.utils.JwtUtils;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(classes = JwtUtils.class)
class JwtUtilsTests {


    @Test
    void testEncodeAndDecodedSuccess() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        Integer number = 10;
        long expiresAt = 1000;
        String privateKey = "private key test";
        String jwt = JwtUtils.createJwtResetTokenWith(email, number, expiresAt, privateKey);

        DecodedJWT decodedJWT = JwtUtils.decodedJWT(jwt, privateKey);

        String actualEmail = decodedJWT.getClaim("email").asString();
        assertEquals(email, actualEmail);
    }

    @Test
    void testDecodedThrowExceptionWhenKeyIsWrong() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        Integer number = 10;
        long expiresAt = 1000;
        String privateKey = "private key test";
        String jwt = JwtUtils.createJwtResetTokenWith(email, number, expiresAt, privateKey);


        JWTVerificationException exception = assertThrows(JWTVerificationException.class, () -> {
            DecodedJWT decodedJWT = JwtUtils.decodedJWT(jwt, "wrong key");
        });
    }

    @Test
    void testDecodedThrowExceptionWhenKeyIsNull() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        Integer number = 10;
        long expiresAt = 1000;
        String privateKey = "private key test";
        String jwt = JwtUtils.createJwtResetTokenWith(email, number, expiresAt, privateKey);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            DecodedJWT decodedJWT = JwtUtils.decodedJWT(jwt, null);
        });
    }

    @Test
    void testDecodedThrowExceptionWhenTokenIsNull() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        Integer number = 10;
        long expiresAt = 1000;
        String privateKey = "private key test";
        String jwt = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            DecodedJWT decodedJWT = JwtUtils.decodedJWT(jwt, null);
        });
    }
}
