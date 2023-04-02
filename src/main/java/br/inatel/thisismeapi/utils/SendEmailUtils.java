package br.inatel.thisismeapi.utils;

public class SendEmailUtils {

    public final static String SUBJECT_RESET_TOKEN = "[TiMe] Troca de Senha";

    public static String getMessageResetToken(Long number) {
        return "Solicitação de troca de senha \n\n" +
                "Token de Verificação: " + number + "" +
                "\n\nO token irá expirar em 24 horas.\n" +
                "caso não tenha solicitado a troca de senha, ignore este email." +
                "\n\n\n" +
                "Time Not Working - App This is Me";
    }
}
