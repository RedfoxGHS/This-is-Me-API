Feature: Everything about Quest API

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

    ### Post Quest
  Scenario: Post Quest, Criar uma quest não estando logado (Unauthorized)
    Given path '/quest'
    And request
    """
    {
      "hexColor": "string",
      "name": "string",
      "desc": "string",
      "startDate": "23-12-2022",
      "endDate": "23-12-2022",
      "skill": {
        "name": "Java",
        "percentage": 0
      },
      "week": [
        {
          "startTime": "00:00",
          "endTime": "00:00",
          "dayOfWeekCustom": "SUNDAY"
        }
      ]
    }
    """
    When method post
    Then status 401
    And print response


  Scenario: Post Quest, Criar usuário, Logar com usuário criado, Criar uma quest com todos os dados inválidos, Deletar usuário criado (Bad Request)
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

    Given path '/quest'
    And request
    """
    {
      "hexColor": "string",
      "name": "string",
      "desc": "string",
      "startDate": "23-11-2022",
      "endDate": "23-11-2022",
      "skill": {
        "name": "Java",
        "percentage": 0
      },
      "week": [
        {
          "startTime": "00:00",
          "endTime": "00:00",
          "dayOfWeekCustom": "SUNDAY"
        }
      ]
    }
    """
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


  Scenario: Post Quest, Criar usuário, Logar com usuário criado, Criar uma quest com todos os dados válidos, Deletar usuário criado (OK)
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

    Given path '/quest'
    And request
    """
    {
      "hexColor": "#FFFFFF",
      "name": "QuestTest",
      "desc": "string",
      "startDate": "15-12-2022",
      "endDate": "30-12-2022",
      "skill": {
        "name": "skill",
        "percentage": 0
      },
      "week": [
        {
          "startTime": "18:00",
          "endTime": "23:00",
          "dayOfWeekCustom": "SUNDAY"
        }
      ]
    }
    """
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


  Scenario: Post Quest, Criar usuário, Logar com usuário criado, Criar uma quest com dados válidos e a skill em branco, Deletar usuário criado (OK)
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

    Given path '/quest'
    And request
    """
    {
      "hexColor": "0xFFFFFFFF",
      "name": "QuestTest",
      "desc": "string",
      "startDate": "15-12-2022",
      "endDate": "30-12-2022",
      "skill": {
        "name": "",
        "percentage": 0
      },
      "week": [
        {
          "startTime": "18:00",
          "endTime": "23:00",
          "dayOfWeekCustom": "SUNDAY"
        }
      ]
    }
    """
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


  Scenario: Post Quest, Criar usuário, Logar com usuário criado, Criar uma quest com dados válidos e a cor nula, Deletar usuário criado (Bad Request)
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

    Given path '/quest'
    And request
    """
    {
      "hexColor": "",
      "name": "QuestTest",
      "desc": "string",
      "startDate": "15-12-2022",
      "endDate": "30-12-2022",
      "skill": {
        "name": "skill",
        "percentage": 0
      },
      "week": [
        {
          "startTime": "18:00",
          "endTime": "23:00",
          "dayOfWeekCustom": "SUNDAY"
        }
      ]
    }
    """
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


  Scenario: Post Quest, Criar usuário, Logar com usuário criado, Criar uma quest com dados válidos e o nome nulo, Deletar usuário criado (Bad Request)
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

    Given path '/quest'
    And request
    """
    {
      "hexColor": "#FFFFFF",
      "name": "",
      "desc": "string",
      "startDate": "15-12-2022",
      "endDate": "30-12-2022",
      "skill": {
        "name": "skill",
        "percentage": 0
      },
      "week": [
        {
          "startTime": "18:00",
          "endTime": "23:00",
          "dayOfWeekCustom": "SUNDAY"
        }
      ]
    }
    """
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


  Scenario: Post Quest, Criar usuário, Logar com usuário criado, Criar uma quest com dados válidos e dataFim antes da dataInicio, Deletar usuário criado (Bad Request)
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

    Given path '/quest'
    And request
    """
    {
      "hexColor": "#FFFFFF",
      "name": "QuestTest",
      "desc": "string",
      "startDate": "26-11-2022",
      "endDate": "10-09-2022",
      "skill": {
        "name": "skill",
        "percentage": 0
      },
      "week": [
        {
          "startTime": "18:00",
          "endTime": "23:00",
          "dayOfWeekCustom": "SUNDAY"
        }
      ]
    }
    """
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


  Scenario: Post Quest, Criar usuário, Logar com usuário criado, Criar uma quest com dados válidos e escolher um dia da semana que não está incluso entre a data definida, Deletar usuário criado (Bad Request)
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

    Given path '/quest'
    And request
    """
    {
      "hexColor": "#FFFFFF",
      "name": "QuestTest",
      "desc": "string",
      "startDate": "26-12-2025",
      "endDate": "27-12-2025",
      "skill": {
        "name": "skill",
        "percentage": 0
      },
      "week": [
        {
          "startTime": "18:00",
          "endTime": "23:00",
          "dayOfWeekCustom": "MONDAY"
        }
      ]
    }
    """
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


  Scenario: Post Quest, Criar usuário, Logar com usuário criado, Criar uma quest com dados válidos e horário não existente, Deletar usuário criado (Bad Request)
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

    Given path '/quest'
    And request
    """
    {
      "hexColor": "#FFFFFF",
      "name": "QuestTest",
      "desc": "string",
      "startDate": "15-12-2022",
      "endDate": "30-12-2022",
      "skill": {
        "name": "skill",
        "percentage": 0
      },
      "week": [
        {
          "startTime": "40:00",
          "endTime": "50:00",
          "dayOfWeekCustom": "SUNDAY"
        }
      ]
    }
    """
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


  Scenario: Post Quest, Criar usuário, Logar com usuário criado, Criar uma quest com dados válidos e passar dia da semana inválido, Deletar usuário criado (Bad Request)
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

    Given path '/quest'
    And request
    """
    {
      "hexColor": "#FFFFFF",
      "name": "QuestTest",
      "desc": "string",
      "startDate": "15-12-2022",
      "endDate": "30-12-2022",
      "skill": {
        "name": "skill",
        "percentage": 0
      },
      "week": [
        {
          "startTime": "18:00",
          "endTime": "23:00",
          "dayOfWeekCustom": "WRONG"
        }
      ]
    }
    """
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




    ### Get Quest
  Scenario: Get Quest, Não estando logado, consultar uma quest  (Unauthorized)
    Given path '/quest'
    And param id = '123'
    When method get
    Then status 401
    And print response


  Scenario: Get Quest, Estando logado, consultar uma quest não existente (Not Found)
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

    Given path '/quest'
    And param id = 123
    When method get
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

  Scenario: Get Quest, Estando logado, não enviar o parâmetro na requisição (Bad Request)
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

    Given path '/quest'
    When method get
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


  Scenario: Get Quest, Estando logado, enviar o nome do parâmetro errado (Bad Request)
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

    Given path '/quest'
    And param test = 123
    When method get
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
