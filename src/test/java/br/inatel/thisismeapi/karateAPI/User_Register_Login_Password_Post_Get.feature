Feature: Everything about User API

  Background:
    * url 'https://timeapibyredfoxghs.herokuapp.com'
    * headers Accept = 'application/json'
    * def createdEmail =
    """
      function CreateEmail() {
      let horas = new Date().getHours().toString();
      let minutos = new Date().getMinutes().toString();
      let seg = new Date().getSeconds().toString();
      let email = horas + minutos + seg + 'id@email.com';
      return {"register": {"email": email , "password": "12345", "verifyPassword": "12345", "characterName": "test123"}, "login": {"email": email , "password": "12345"}};
      }
    """

    ### Registro do Usuário - Post User
  Scenario: Post User, Registrar usuário com todas as informações válidas(preenchidas automaticamente), realizar login, deletar email criado (Ok)
    Given path '/user/register'
    And def email = createdEmail()
    And request email.register
    When method post
    Then status 201
    And print response

    Given path '/user/login'
    And request email.login
    When method post
    Then status 200
    And print response

    Given path '/user/login'
    And request {"email": "admin@admin.com", "password": "admin456"}
    When method post
    Then status 200
    And print response

    Given path '/admin/delete-user-by-email'
    And param email = email.login.email
    When method delete
    Then status 204
    And print response


  Scenario: Post User, Registrando usuário com ambos os campos senha em branco (Bad Request)
    Given path '/user/register'
    And request {"email": "testSenha1@test.com", "password": "", "verifyPassword": "", "characterName": ""}
    When method post
    Then status 400
    And print response


  Scenario: Post User, Registrando usuário com o campo senha válido e o verificarSenha em branco (Bad Request)
    Given path '/user/register'
    And request {"email": "testSenha1@test.com", "password": "12345", "verifyPassword": "", "characterName": ""}
    When method post
    Then status 400
    And print response


  Scenario: Post User, Registrando usuário com o campo senha em branco e o verificarSenha válido (Bad Request)
    Given path '/user/register'
    And request {"email": "testSenha1@test.com", "password": "", "verifyPassword": "12345", "characterName": ""}
    When method post
    Then status 400
    And print response


  Scenario: Post User, Registrando usuário com a senha inferior a 5 caracteres (Bad Request)
    Given path '/user/register'
    And request {"email": "testSenha1@test.com", "password": "1234", "verifyPassword": "1234", "characterName": ""}
    When method post
    Then status 400
    And print response


  Scenario: Post User, Registrando usuário com a senha superior a 30 caracteres (Bad Request)
    Given path '/user/register'
    And request {"email": "testSenha1@test.com", "password": "0123456789012345678901234567890123456789", "verifyPassword": "0123456789012345678901234567890123456789", "characterName": ""}
    When method post
    Then status 400
    And print response


  Scenario: Post User, Registrando usuário com duplicidade de emails (Conflict)
    Given path '/user/register'
    And def email = createdEmail()
    And request email.register
    When method post
    Then status 201
    And print response

    Given path '/user/register'
    And request email.register
    When method post
    Then status 409
    And print response

    Given path '/user/login'
    And request {"email": "admin@admin.com", "password": "admin456"}
    When method post
    Then status 200
    And print response

    Given path '/admin/delete-user-by-email'
    And param email = email.login.email
    When method delete
    Then status 204
    And print response


  Scenario: Post User, Registrando usuário com email inválido, senhas incompatíveis e characterName em branco (Bad Request)
    Given path '/user/register'
    And request {"email": "testesteste", "password": "123456789", "verifyPassword": "123456", "characterName": ""}
    When method post
    Then status 400
    And print response


  Scenario: Post User, Registrando usuário com senhas incompatíveis e characterName em branco (Bad Request)
    Given path '/user/register'
    And request {"email": "testeEmailValido@test.com", "password": "123456789", "verifyPassword": "123456", "characterName": ""}
    When method post
    Then status 400
    And print response


  Scenario: Post User, Registrando usuário com senhas compatíveis e characterName em branco (Bad Request)
    Given path '/user/register'
    And request {"email": "testeEmailValido@test.com", "password": "123456789", "verifyPassword": "123456789", "characterName": ""}
    When method post
    Then status 400
    And print response


  Scenario: Post User, Registrar um novo user, Realizar Login, Verificar acesso do usuário na rota admin e Deletar email criado (Unauthorized)
    Given path '/user/register'
    And def email = createdEmail()
    And request email.register
    When method post
    Then status 201
    And print response

    Given path '/user/login'
    And request email.login
    When method post
    Then status 200
    And print response

    Given path '/admin/helloAdmin'
    When method get
    Then status 403
    And print response

    Given path '/user/login'
    And request {"email": "admin@admin.com", "password": "admin456"}
    When method post
    Then status 200
    And print response

    Given path '/admin/delete-user-by-email'
    And param email = email.login.email
    When method delete
    Then status 204
    And print response




    ### Usuário Logado - Get User
  Scenario: Get User, Verifica se usuário está logado. Usuário Logado. (Ok)
    Given path '/user/register'
    And def email = createdEmail()
    And request email.register
    When method post
    Then status 201
    And print response

    Given path '/user/login'
    And request email.login
    When method post
    Then status 200
    And print response

    Given path '/user/helloUser'
    When method get
    Then status 200
    And print response

    Given path '/user/login'
    And request {"email": "admin@admin.com", "password": "admin456"}
    When method post
    Then status 200
    And print response

    Given path '/admin/delete-user-by-email'
    And param email = email.login.email
    When method delete
    Then status 204
    And print response


  Scenario: Get User, Verifica se usuário está logado. Usuário Não Logado. (Unauthorized)
    Given path '/user/helloUser'
    When method get
    Then status 401
    And print response




    ### Resetar senha - Enviar código por email
  Scenario: Post User, Registrar usuário, logar, solicitar código para reset de senha enviado através do email (Ok)
    Given path '/user/register'
    And def email = createdEmail()
    And request email.register
    When method post
    Then status 201
    And print response

    Given path '/user/login'
    And request email.login
    When method post
    Then status 200
    And print response

    Given path '/user/reset/forgot-password'
    And param email = email.login.email
    When method post
    Then status 200
    And print response

    Given path '/user/login'
    And request {"email": "admin@admin.com", "password": "admin456"}
    When method post
    Then status 200
    And print response

    Given path '/admin/delete-user-by-email'
    And param email = email.login.email
    When method delete
    Then status 204
    And print response


  Scenario: Post User, Solicitar código para reset de senha enviado através do email em branco (Bad Request)
    Given path '/user/reset/forgot-password'
    And param email = ""
    When method post
    Then status 400
    And print response


  Scenario: Post User, Registrar usuário, não logar, solicitar código para reset de senha enviado através do email (Ok)
    Given path '/user/register'
    And def email = createdEmail()
    And request email.register
    When method post
    Then status 201
    And print response

    Given path '/user/reset/forgot-password'
    And param email = email.login.email
    When method post
    Then status 200
    And print response

    Given path '/user/login'
    And request {"email": "admin@admin.com", "password": "admin456"}
    When method post
    Then status 200
    And print response

    Given path '/admin/delete-user-by-email'
    And param email = email.login.email
    When method delete
    Then status 204
    And print response


  Scenario: Post User, Solicitar código para reset de senha enviado através de um email não cadastrado no sistema (Unauthorized)
    Given path '/user/reset/forgot-password'
    And param email = "testeResetSenha@teste.com"
    When method post
    Then status 401
    And print response


  Scenario: Post User, Não enviar o email (Bad Request)
    Given path '/user/reset/forgot-password'
    When method post
    Then status 400
    And print response


  Scenario: Post User, Registrar usuário, logar, solicitar código para reset de senha enviado através de um email inválido (Bad Request)
    Given path '/user/register'
    And def email = createdEmail()
    And request email.register
    When method post
    Then status 201
    And print response

    Given path '/user/reset/forgot-password'
    And param email = "testetestet.com"
    When method post
    Then status 400
    And print response

    Given path '/user/login'
    And request {"email": "admin@admin.com", "password": "admin456"}
    When method post
    Then status 200
    And print response

    Given path '/admin/delete-user-by-email'
    And param email = email.login.email
    When method delete
    Then status 204
    And print response




    ### Cenário de teste servindo para os próximos cenários
  Scenario: Post User, Registrar usuário, solicitar código para reset de senha enviado através do email (Ok)
    Given path '/user/register'
    And request {"email": "jaxiri6605@nubotel.com" , "password": "12345", "verifyPassword": "12345", "characterName": "test123"}
    When method post
    Then assert responseStatus == 201 || responseStatus == 409
    And print response

    Given path '/user/reset/forgot-password'
    And param email = "jaxiri6605@nubotel.com"
    When method post
    Then status 200
    And print response

