## https://temp-mail.org/pt

Feature: Everything about User API

  Background:
    * url 'https://timeapibyredfoxghs.herokuapp.com'
    * headers Accept = 'application/json'
    * def emailCode = {"email": "jaxiri6605@nubotel.com", "number": 670702}
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


    ### Resetar senha - Verificar código enviado por email
  Scenario: Post User, Dado que o usuário solicitou um novo código, verificar este código (Ok)
    Given path '/user/reset/verify-code-reset'
    And request emailCode
    When method post
    Then status 200
    And print response
    And print responseCookies
    And print responseCookies['token_reset']['value']


  Scenario: Post User, Solicitar verificação do código sem o request (Bad Request)
    Given path '/user/reset/verify-code-reset'
    When method post
    Then status 400
    And print response


  Scenario: Post User, Verificar código com email e código inválido (Unauthorized)
    Given path '/user/reset/verify-code-reset'
    And request {"email": "string", "number": 0}
    When method post
    Then status 401
    And print response


  Scenario: Post User, Verificar código com código inválido (Unauthorized)
    Given path '/user/reset/verify-code-reset'
    And request {"email": "teste2@teste.com", "number": 0}
    When method post
    Then status 401
    And print response



    ### Resetar senha
  Scenario: Post User, Solicita requisição de senha sem enviar o request (Bad Request)
    Given path '/user/reset/verify-code-reset'
    And request emailCode
    When method post
    Then status 200
    And def token = responseCookies['token_reset']['value']

    Given path '/user/reset/reset-password'
    And header Authorization = token
    When method post
    Then status 400
    And print response


  Scenario: Post User, Solicitar requisição com a nova senha sem ter verificado o token (Unauthorized)
    Given path '/user/reset/reset-password'
    And request {"password": "987654", "passwordVerify": "987654"}
    When method post
    Then status 401
    And print response


  Scenario: Post User, Resetar senha (Ok)
    Given path '/user/reset/verify-code-reset'
    And request emailCode
    When method post
    Then status 200
    And def token = responseCookies['token_reset']['value']

    Given path '/user/reset/reset-password'
    And header Authorization = token
    And request {"password": "654321", "passwordVerify": "654321"}
    When method post
    Then status 200
    And print response
