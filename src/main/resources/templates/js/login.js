const BASE_URL = "https://hairremovalappointment.onrender.com/api/v1"

let errorOvelay = document.querySelector(".error-ovelay");
let errorMessage = errorOvelay.querySelector("#error-message");
let errorButton = errorOvelay.querySelector("button");

let login_container = document.querySelector("div#login");

let password_input = login_container.querySelector("#password");
let password_error = login_container.querySelector(".error-password");

let email_input = login_container.querySelector("#email");
let email_error = login_container.querySelector(".error-email");

document.querySelector(".btn-submit").addEventListener("click", function name() {


    if(password_input.value === ""){
        password_error.innerText = "Senha não pode ser vazio"
        return;
    }else{
        password_error.innerText = ""
    }

    if(password_input.value.length < 8){
       password_error.innerText = "Senha precisa ter 8 digitos" 
    }else{
        password_error.innerText = ""
    }

    if(email_input.value === ""){
        email_error.innerText = "Email não pode ser vazio"
        return;
    }else{
        email_error.innerText = ""
    }

    login();
})

async function login() {

    console.log(email_input.value + " : " + password_input.value);
    let header = new Headers();
    header.append("Content-Type", "application/json; charset=utf8");
    try {
        const response = await fetch(BASE_URL + '/auth/login', 
        { 
            method: 'POST',
            headers: header,
            body: JSON.stringify({
                email: email_input.value,
                password: password_input.value
            })
        }
        );

        const text = await response.text();

        let data = null;

        try {
            data = text ? JSON.parse(text) : null;
        } catch {
            data = text;
        }

        if (!response.ok) {
            if(response.status == 400){
                showErrorOverlay("Coloque as informações corretas");
                return null;
            }else{
                console.log(text)
                showErrorOverlay("Test" + JSON.parse(text).errors);
            }
            return null;
        }

        const { token } = await response.json();
        window.localStorage.setItem("Authorization", token);

        console.log('Token: Bearer ' + localStorage.getItem('Authorization'));
        window.setTimeout(() => {
            window.location = "./home";
        }, 2000);

    } catch (error) {
        showErrorOvelay("Erro de conexão com o servidor", error);
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