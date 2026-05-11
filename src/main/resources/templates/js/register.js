const BASE_URL = "http://localhost:8080/api/v1"

let errorOvelay = document.querySelector(".error-ovelay");
let errorMessage = errorOvelay.querySelector("#error-message");
let errorButton = errorOvelay.querySelector("button");

let register_container = document.querySelector("div#register");

let username_input = register_container.querySelector("#username");
let username_error = register_container.querySelector(".error-username");

let password_input = register_container.querySelector("#password");
let password_error = register_container.querySelector(".error-password");

let email_input = register_container.querySelector("#email");
let email_error = register_container.querySelector(".error-email");

let phoneNumber_input = register_container.querySelector("#phoneNumber");
let phoneNumber_error = register_container.querySelector(".error-phoneNumber");

let birthday_input = register_container.querySelector("#birthday");
let birthday_error = register_container.querySelector(".error-birthday");

document.querySelector(".btn-submit").addEventListener("click", function name() {
    if(username_input.value === ""){
       username_error.innerText = "Nome não pode ser vazio"
       return;
    }

    if(!isNaN(username_input.value) && !username_input.value === ""){
        username_error.innerText = "Não pode conter numeros no nome";
        return;
    }else{
        username_error.innerText = "";
    }

    if(password_input.value === ""){
        password_error.innerText = "Senha não pode ser vazio"
        return;
    }else{
        password_error.innerText = ""
    }

    if(password_input.value.length < 8){
       password_error.innerText = "Senha precisa ter 8 digitos" 
       return;
    }else{
        password_error.innerText = ""
    }

    if(email_input.value === ""){
        email_error.innerText = "Email não pode ser vazio"
        return;
    }else{
        email_error.innerText = ""
    }

    if(phoneNumber_input.value === ""){
        phoneNumber_error.innerText = "precisa colocar um numero de telefone"
        return;
    }else{
        phoneNumber_error.innerHTML = "";
    }

    if(birthday_input.value === ""){
        birthday_error.innerText = "Precisa colocar uma data de nascimento"
        return;
    }else{
        birthday_error.innerText = ""
    }

    register();
})

async function register() {

    let header = new Headers();
    header.append("Content-Type", "application/json; charset=utf8");

    try {
        const response = await fetch(BASE_URL + '/auth/register', 
        { 
            method: 'POST',
            headers: header,
            body: JSON.stringify({
                name : username_input.value,
                password : password_input.value,
                email : email_input.value,
                phoneNumber : phoneNumber_input.value,
                birthday : birthday_input.value,
            })
        }
        );
        
        if (!response.ok) {
            const text = await response.text(); // lê UMA vez só
            console.log("Texto: " + text);
            const error = JSON.parse(text); // converte o texto já lido
            console.log(error)
            showErrorOvelay(error.errors);
            return;
        }

        const { token } = await response.json();
        window.localStorage.setItem("Authorization", token);

        console.log('Token: Bearer ' + localStorage.getItem('Authorization'));

        showErrorOvelay("Agora faça o login.");
        window.setTimeout(() => {
            window.location = "./login";
        }, 3000);

    } catch (error) {
        showErrorOvelay("Erro de conexão com o servidor");
    }
}


function hideErrorOvelay() {
    errorOvelay.style.display = "none";
}

function showErrorOvelay(message) {
    errorMessage.innerText = message;
    errorOvelay.style.display = "block";
}

errorButton.addEventListener("click", hideErrorOvelay);