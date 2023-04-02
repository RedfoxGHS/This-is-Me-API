Feature: Everything about Skill API

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

    ### Post Skill
  Scenario: Post Skill, Estando logado, criar uma skill (Created)
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

    Given path '/skill'
    And request {"name": "Teste"}
    When method post
    Then status 201
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


  Scenario: Post Skill, Não estando logado, criar uma skill (Unauthorized)
    Given path '/skill'
    And request {"name": "Teste"}
    When method post
    Then status 401
    And print response


  Scenario: Post Skill, Estando logado, não enviar o request body (Bad Request)
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

    Given path '/skill'
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


  Scenario: Post Skill, Estando logado, enviar o request com o campo 'name' diferente('teste01') (Bad Request)
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

    Given path '/skill'
    And request {"teste01": "Teste"}
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


  Scenario: Post Skill, Estando logado, enviar o request com o campo 'name' vazio (Bad Request)
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

    Given path '/skill'
    And request {"name": ""}
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


  Scenario: Post Skill, Estando logado, enviar o request com uma informação a mais('title': 'teste') (Bad Request)
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

    Given path '/skill'
    And request {"name": "Teste01", "title": "teste"}
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




    ### Get Skill
  Scenario: Get Skill, Criado uma conta, logado, criado uma skill, consultar skills (Ok)
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

    Given path '/skill'
    And request {"name": "Teste"}
    When method post
    Then status 201
    And print response

    Given path '/skill/all'
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


  Scenario: Get Skill, Não estando logado, consultar skills (Unauthorized)
    Given path '/skill/all'
    When method get
    Then status 401
    And print response


  Scenario: Get Skill, Criado uma conta, logado, consultar skills (Not Found)
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

    Given path '/skill/all'
    When method get
    Then status 200
    And print response
    And match $ == []

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
