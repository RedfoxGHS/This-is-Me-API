function CreateEmail() {

  let horas = new Date().getHours().toString();
  let minutos = new Date().getMinutes().toString();
  let seg = new Date().getSeconds().toString();
  let email = horas + minutos + seg + 'Id@email.com';
  return {"email": email , "password": "12345", "verifyPassword": "12345", "characterName": "test123"};

}