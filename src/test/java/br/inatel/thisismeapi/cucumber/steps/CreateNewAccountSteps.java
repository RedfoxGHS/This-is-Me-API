package br.inatel.thisismeapi.cucumber.steps;

import br.inatel.thisismeapi.cucumber.config.RestAssuredExtension;
import br.inatel.thisismeapi.cucumber.models.CreateAccountContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateNewAccountSteps {

    private static ResponseOptions<Response> response;
    private static ResponseOptions<Response> user;
    private CreateAccountContext createAccountContext;

    @Before
    public void setup() {
        RestAssuredExtension restAssuredExtension = new RestAssuredExtension();
        createAccountContext = new CreateAccountContext();
    }

    @After
    public void tearDown() {
        if (user != null) {
            String email = user.getBody().jsonPath().get("email");
            RestAssuredExtension.DeleteAdminOps("/admin/delete-user-by-email?email=" + email);
        }
    }

    @Dado("que o usuário preencheu o formulário de cadastro com as seguintes informações")
    public void queOUsuárioPreencheuOFormulárioDeCadastroComAsSeguintesInformações(CreateAccountContext createAccount) {
        createAccountContext.setCharacterName(createAccount.getCharacterName());
        createAccountContext.setEmail(createAccount.getEmail());
        createAccountContext.setPassword(createAccount.getPassword());
        createAccountContext.setVerifyPassword(createAccount.getVerifyPassword());
    }

    @E("o status da resposta deve ser {int}")
    public void oStatusDaRespostaDeveSer(int status) {
        assertEquals(status, response.getStatusCode());
    }

    @Quando("o usuario acessar a rota helloUser")
    public void oUsuarioAcessarARotaHelloUser() {
        response = RestAssuredExtension.GetOps("/helloUser");
    }

    @Quando("o usuário clicar no botão de cadastro e enviar o formulário")
    public void oUsuárioClicarNoBotãoDeCadastroEEnviarOFormulário() {
        response = RestAssuredExtension.PostOps("/user/register", createAccountContext);
    }

    @Então("o usuário deve ser cadastrado com sucesso")
    public void oUsuárioDeveSerCadastradoComSucesso() {
        user = RestAssuredExtension.GetAdminOps("/admin/get-user-by-email?email=" + createAccountContext.getEmail());
        assert user != null;
        assertEquals(createAccountContext.getEmail(), user.getBody().jsonPath().get("email"));
    }

    @E("a mensagem de erro deve ser {string}")
    public void aMensagemDeErroDeveSer(String message) {
        assertEquals(message, response.getBody().jsonPath().get("message"));
    }

    @Dado("que o usuario já está cadastrado no sistema")
    public void queOUsuarioJáEstáCadastradoNoSistema(CreateAccountContext createAccount) {
        response = RestAssuredExtension.PostOps("/user/register", createAccount);
    }

    @DataTableType(replaceWithEmptyString = "[BLANK]")
    public CreateAccountContext createAccountEntryTransformer(Map<String, String> entry) {
        return new CreateAccountContext(
                entry.get("characterName"),
                entry.get("email"),
                entry.get("password"),
                entry.get("verifyPassword")
        );
    }

}
