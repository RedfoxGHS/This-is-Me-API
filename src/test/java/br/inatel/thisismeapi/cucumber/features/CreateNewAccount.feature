# language: pt

Funcionalidade: Teste de cadastro de usuário

  Cenário: Cadastro de usuário com sucesso
    Dado que o usuário preencheu o formulário de cadastro com as seguintes informações
      | characterName | email          | password | verifyPassword |
      | João          | joao@gmail.com | 123456   | 123456         |
    Quando o usuário clicar no botão de cadastro e enviar o formulário
    Então o usuário deve ser cadastrado com sucesso
    E o status da resposta deve ser 201

  Cenário: Cadastro de usuário com nome de personagem com caracteres especiais
    Dado que o usuário preencheu o formulário de cadastro com as seguintes informações
      | characterName | email          | password | verifyPassword |
      | João!         | joao@gmail.com | 123456   | 123456         |
    Quando o usuário clicar no botão de cadastro e enviar o formulário
    Então o usuário deve ser cadastrado com sucesso
    E o status da resposta deve ser 201

  Cenário: Cadastro de usuário já existente
    Dado que o usuario já está cadastrado no sistema
      | characterName | email          | password | verifyPassword |
      | João          | joao@gmail.com | 123456   | 123456         |
    E que o usuário preencheu o formulário de cadastro com as seguintes informações
      | characterName | email          | password | verifyPassword |
      | João          | joao@gmail.com | 123456   | 123456         |
    Quando o usuário clicar no botão de cadastro e enviar o formulário
    Então o status da resposta deve ser 409
    E a mensagem de erro deve ser "Já Existe uma conta cadastrada com esse e-mail!"

  Cenário: Cadastro de usuário com erro de email inválido
    Dado que o usuário preencheu o formulário de cadastro com as seguintes informações
      | characterName | email         | password | verifyPassword |
      | João          | emailinvalido | 123456   | 123456         |
    Quando o usuário clicar no botão de cadastro e enviar o formulário
    Então o status da resposta deve ser 400
    E a mensagem de erro deve ser "Email inválido!"

  Cenário: Cadastro de usuário com erro de senha diferentes
    Dado que o usuário preencheu o formulário de cadastro com as seguintes informações
      | characterName | email          | password | verifyPassword |
      | João          | joao@gmail.com | 123456   | 1234567        |
    Quando o usuário clicar no botão de cadastro e enviar o formulário
    Então o status da resposta deve ser 400
    E a mensagem de erro deve ser "As senhas não coincidem!"

  Cenário: Cadastro de usuário com erro de senha muito curta
    Dado que o usuário preencheu o formulário de cadastro com as seguintes informações
      | characterName | email          | password | verifyPassword |
      | João          | joao@gmail.com | 123      | 123            |
    Quando o usuário clicar no botão de cadastro e enviar o formulário
    Então o status da resposta deve ser 400
    E a mensagem de erro deve ser "Senha deve conter no mínimo 5 e no máximo 30 dígitos!"

  Cenário: Cadastro de usuário com erro de senha muito grande
    Dado que o usuário preencheu o formulário de cadastro com as seguintes informações
      | characterName | email          | password                                 | verifyPassword                           |
      | João          | joao@gmail.com | 0123456789012345678901234567890123456789 | 0123456789012345678901234567890123456789 |
    Quando o usuário clicar no botão de cadastro e enviar o formulário
    Então o status da resposta deve ser 400
    E a mensagem de erro deve ser "Senha deve conter no mínimo 5 e no máximo 30 dígitos!"

  Cenário: Cadastro de usuário com nome de personagem muito comprido
    Dado que o usuário preencheu o formulário de cadastro com as seguintes informações
      | characterName       | email           | password | verifyPassword |
      | Nome muito comprido | email@gmail.com | 123456   | 123456         |
    Quando o usuário clicar no botão de cadastro e enviar o formulário
    Então o status da resposta deve ser 400
    E a mensagem de erro deve ser "Nome do personagem pode ter no máximo 15 caracteres!"

  Cenário: Cadastro de usuário com nome de personagem nulo
    Dado que o usuário preencheu o formulário de cadastro com as seguintes informações
      | characterName | email          | password | verifyPassword |
      |               | joao@gmail.com | 123456   | 123456         |
    Quando o usuário clicar no botão de cadastro e enviar o formulário
    Então o status da resposta deve ser 400
    E a mensagem de erro deve ser "Nome do personagem inválido!"

  Cenário: Cadastro de usuário com nome de personagem em branco
    Dado que o usuário preencheu o formulário de cadastro com as seguintes informações
      | characterName | email          | password | verifyPassword |
      | [BLANK]       | joao@gmail.com | 123456   | 123456         |
    Quando o usuário clicar no botão de cadastro e enviar o formulário
    Então o status da resposta deve ser 400
    E a mensagem de erro deve ser "Nome do personagem inválido!"

  Cenário: Cadastro de usuário com email em branco
    Dado que o usuário preencheu o formulário de cadastro com as seguintes informações
      | characterName | email   | password | verifyPassword |
      | João          | [BLANK] | 123456   | 123456         |
    Quando o usuário clicar no botão de cadastro e enviar o formulário
    Então o status da resposta deve ser 400
    E a mensagem de erro deve ser "Email não pode ser deixado em branco!"

  Cenário: Cadastro de usuário com email nulo
    Dado que o usuário preencheu o formulário de cadastro com as seguintes informações
      | characterName | email | password | verifyPassword |
      | João          |       | 123456   | 123456         |
    Quando o usuário clicar no botão de cadastro e enviar o formulário
    Então o status da resposta deve ser 400
    E a mensagem de erro deve ser "Email não pode ser nulo!"

  Cenário: Cadastro de usuário com os campos de senhas em branco
    Dado que o usuário preencheu o formulário de cadastro com as seguintes informações
      | characterName | email          | password | verifyPassword |
      | João          | joao@gmail.com | [BLANK]  | [BLANK]        |
    Quando o usuário clicar no botão de cadastro e enviar o formulário
    Então o status da resposta deve ser 400
    E a mensagem de erro deve ser "Senha não pode ser deixada em branco!"

  Cenário: Cadastro de usuário com os campos de senhas nulos
    Dado que o usuário preencheu o formulário de cadastro com as seguintes informações
      | characterName | email          | password | verifyPassword |
      | João          | joao@gmail.com |          |                |
    Quando o usuário clicar no botão de cadastro e enviar o formulário
    Então o status da resposta deve ser 400
    E a mensagem de erro deve ser "Senha não pode ser nula!"

  Cenário: Cadastro de usuário com password nulo apenas
    Dado que o usuário preencheu o formulário de cadastro com as seguintes informações
      | characterName | email          | password | verifyPassword |
      | João          | joao@gmail.com |          | 123456         |
    Quando o usuário clicar no botão de cadastro e enviar o formulário
    Então o status da resposta deve ser 400
    E a mensagem de erro deve ser "Senha não pode ser nula!"

  Cenário: Cadastro de usuário com verifyPassword nulo apenas
    Dado que o usuário preencheu o formulário de cadastro com as seguintes informações
      | characterName | email          | password | verifyPassword |
      | João          | joao@gmail.com | 123456   |                |
    Quando o usuário clicar no botão de cadastro e enviar o formulário
    Então o status da resposta deve ser 400
    E a mensagem de erro deve ser "Senha não pode ser nula!"

  Cenário: Cadastro de usuário com password em branco apenas
    Dado que o usuário preencheu o formulário de cadastro com as seguintes informações
      | characterName | email          | password | verifyPassword |
      | João          | joao@gmail.com | [BLANK]  | 123456         |
    Quando o usuário clicar no botão de cadastro e enviar o formulário
    Então o status da resposta deve ser 400
    E a mensagem de erro deve ser "Senha não pode ser deixada em branco!"

  Cenário: Cadastro de usuário com verifyPassword em branco apenas
    Dado que o usuário preencheu o formulário de cadastro com as seguintes informações
      | characterName | email          | password | verifyPassword |
      | João          | joao@gmail.com | 123456   | [BLANK]        |
    Quando o usuário clicar no botão de cadastro e enviar o formulário
    Então o status da resposta deve ser 400
    E a mensagem de erro deve ser "Senha não pode ser deixada em branco!"
