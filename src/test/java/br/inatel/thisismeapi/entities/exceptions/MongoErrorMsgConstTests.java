package br.inatel.thisismeapi.entities.exceptions;

import br.inatel.thisismeapi.entities.Exceptions.MongoErrorMsgConst;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ServletWebServerFactoryAutoConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@ActiveProfiles("dev")
class MongoErrorMsgConstTests {

    @Test
    void testNewInstanceOfMongoErrorMsgConstTest() {
        MongoErrorMsgConst mongoErrorMsgConst = new MongoErrorMsgConst();

        assertEquals(MongoErrorMsgConst.class, mongoErrorMsgConst.getClass());
    }

    @Test
    void testErrorE11000() {
        String msg = MongoErrorMsgConst.getMsgByCode("E11000");

        assertEquals("Já Existe uma conta cadastrada com esse e-mail!", msg);
    }

    @Test
    void testUnknownError() {
        String code = "EUnknown123";
        String expected = "Erro " + code + " desconhecido!";
        String actual = MongoErrorMsgConst.getMsgByCode("EUnknown123");

        assertEquals(expected, actual);
    }

}
