Feature: Everything about Character API

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

    ### Get Character - Basic Informations
  Scenario: Get Character, Criar usuário, Logar com usuário criado, Fazer o GetCharacter, Deletar usuário criado (Ok)
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

    Given path '/character/get-character'
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


  Scenario: Get Character, Realizar o GetCharacter sem logar primeiro (Unauthorized)
    Given path '/character/get-character'
    When method get
    Then status 401
    And print response




    ### Get Character - All Informations
  Scenario: Get Character All Infos, Criar usuário, Logar com usuário criado, Fazer o GetCharacter, Deletar usuário criado (Ok)
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

    Given path '/character/get-character-all-infos'
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


  Scenario: Get Character All Infos, Realizar o GetCharacter sem logar primeiro (Unauthorized)
    Given path '/character/get-character-all-infos'
    When method get
    Then status 401
    And print response




    ### Set Clothes - Atualizar roupa
  Scenario: Post Character, Criar usuário, Logar com usuário criado, Setar uma roupa existente, Deletar usuário criado (Ok)
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

    Given path '/character/set-clothes'
    And param number = 2
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


  Scenario: Post Character, Criar usuário, Logar com usuário criado, Setar uma roupa não existente, Deletar usuário criado (Not Found)
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

    Given path '/character/set-clothes'
    And param number = 99999
    When method post
    Then status 404
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


  Scenario: Post Character, Criar usuário, Logar com usuário criado, Setar uma roupa não existente(9999999999), Deletar usuário criado (Bad Request)
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

    Given path '/character/set-clothes'
    And param number = 9999999999
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


  Scenario: Post Character, Criar usuário, Logar com usuário criado, Setar uma roupa não existente(-1), Deletar usuário criado (Bad Request)
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

    Given path '/character/set-clothes'
    And param number = -1
    When method post
    Then status 404
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


  Scenario: Post Character, Setar uma roupa existente porém sem usuário logado (Unauthorized)
    Given path '/character/set-clothes'
    And param number = 2
    When method post
    Then status 401
    And print response

