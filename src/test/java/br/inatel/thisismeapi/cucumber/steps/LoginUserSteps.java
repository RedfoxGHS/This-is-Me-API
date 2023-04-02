package br.inatel.thisismeapi.cucumber.steps;

import br.inatel.thisismeapi.cucumber.config.RestAssuredExtension;
import br.inatel.thisismeapi.cucumber.models.LoginUserContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import org.junit.jupiter.api.Assertions;

public class LoginUserSteps {


    private static final String EMAIL_TEST = "email_test@gmail.com";
    private LoginUserContext loginUserContext;

    private static ResponseOptions<Response> response;

    @Before
    public void setup() {
        RestAssuredExtension restAssuredExtension = new RestAssuredExtension();
    }

    @After
    public void tearDown() {
        RestAssuredExtension.DeleteAdminOps("/admin/delete-user-by-email?email=" + EMAIL_TEST);
    }

    @Dado("que o usuario está na pagina de login")
    public void queOUsuarioEstáNaPaginaDeLogin() {
        loginUserContext = new LoginUserContext();
    }

    @Dado("preenche o campo email com {string}")
    public void oUsuarioInformaOEmail(String email) {
        loginUserContext.setEmail(email);
    }

    @Dado("preenche o campo senha com {string}")
    public void oUsuarioInformaASenha(String password) {
        loginUserContext.setPassword(password);
    }


    @Quando("clica no botão entrar e enviar os dados")
    public void clicaNoBotãoEntrarEEnviarOsDados() {
        response = RestAssuredExtension.LoginWithUser("/user/login", loginUserContext);
    }

    @Então("o usuario deve estar logado com sucesso")
    public void oUsuarioDeveEstarLogadoComSucesso() {
        var helloResponse = RestAssuredExtension.GetOps("/user/helloUser");

        assert helloResponse != null;
        Assertions.assertEquals(200, helloResponse.getStatusCode());
    }


    @E("o token deve ser retornado na resposta")
    public void oTokenDeveSerRetornadoNaResposta() {
        assert response != null;
        Assertions.assertNotNull(response.getCookie("token"));
    }

    @E("o status da resposta http deve ser {int}")
    public void oStatusDaRespostaHttpDeveSer(int status) {
        assert response != null;
        Assertions.assertEquals(status, response.getStatusCode());
    }

    @Então("o usuario deve ver a mensagem {string}")
    public void oUsuarioDeveVerAMensagem(String message) {
        assert response != null;
        Assertions.assertEquals(message, response.getBody().jsonPath().get("message"));
    }
}
