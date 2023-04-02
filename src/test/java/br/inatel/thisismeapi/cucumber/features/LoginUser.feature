# language: pt

Funcionalidade: Testar login de usuario comum

  Contexto: Usuário comum ja cadastrado
    Dado que o usuario já está cadastrado no sistema
      | characterName | email                | password | verifyPassword |
      | Test          | email_test@email.com | 123456   | 123456         |

  Cenário: Login com sucesso
    Dado que o usuario está na pagina de login
    E preenche o campo email com "email_test@email.com"
    E preenche o campo senha com "123456"
    Quando clica no botão entrar e enviar os dados
    Então o usuario deve estar logado com sucesso
    E o status da resposta http deve ser 302
    E o token deve ser retornado na resposta