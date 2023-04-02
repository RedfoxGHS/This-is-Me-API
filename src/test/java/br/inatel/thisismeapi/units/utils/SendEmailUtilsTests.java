package br.inatel.thisismeapi.units.utils;

import br.inatel.thisismeapi.utils.SendEmailUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = SendEmailUtils.class)
class SendEmailUtilsTests {

    @Test
    public void testGetMessageResetToken() {
        Long number = 123456L;
        String expected = "Solicitação de troca de senha \n\n" +
                "Token de Verificação: " + number + "" +
                "\n\nO token irá expirar em 24 horas.\n" +
                "caso não tenha solicitado a troca de senha, ignore este email." +
                "\n\n\n" +
                "Time Not Working - App This is Me";

        String actual = SendEmailUtils.getMessageResetToken(number);

        assertEquals(expected, actual);
    }
}
