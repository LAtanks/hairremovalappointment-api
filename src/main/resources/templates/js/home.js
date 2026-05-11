const BASE_URL = "https://hairremovalappointment.onrender.com/api/v1";

// ─── ELEMENTOS DO DOM ────────────────────────────────────────────────────────
const errorOverlay  = document.querySelector(".error-overlay");
const errorMessage  = errorOverlay.querySelector(".error-message");
const errorButton   = errorOverlay.querySelector("button");

const header = document.querySelector("header#user-header");
const user_picture = header.querySelector("#user-picture");
const user_name = header.querySelector("#username");

const myAppointment = document.querySelector("#my-appointments");

const create_appointment = document.querySelector("#create-appointment");


// ─── UTILITÁRIOS ─────────────────────────────────────────────────────────────
function getAuthHeaders() {
    const headers = new Headers();
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", `Bearer ${localStorage.getItem("Authorization")}`);
    return headers;
}

function formatDate(dateInput) {
    const date = new Date(dateInput);
    return `${date.getDate()} / ${date.getMonth() + 1} / ${date.getFullYear()}`;
}

function showErrorOverlay(message) {
    errorMessage.innerText = message;
    errorOverlay.style.display = "block";
}

function hideErrorOverlay() {
    errorOverlay.style.display = "none";
}

errorButton.addEventListener("click", hideErrorOverlay);

// ─── NAVEGAÇÃO ───────────────────────────────────────────────────────────────
function option(selected) {
    myAppointment.style.display = selected === "inicio"    ? "block" : "none";
    create_appointment.style.display            = selected === "agendar"    ? "block" : "none";
    //calendarSection.style.display     = selected === "perfil" ? "block" : "none";
}

// ─── API ─────────────────────────────────────────────────────────────────────
async function apiFetch(path, method = "GET", body = null) {
    const options = {
        method,
        headers: getAuthHeaders()
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(`${BASE_URL}${path}`, options);
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
                showErrorOverlay(JSON.parse(text).errors);
            }
            return null;
        }

        return data;

    } catch (err) {
        console.error(err);

        showErrorOverlay(
            err?.message || "Erro de conexão com o servidor."
        );

        return null;
    }
}

const getMyAppt = ()        => apiFetch("/appointment/my");
const getMyUser  = ()        => apiFetch("/user/my");
const createAppt = (horary, type, payment) => apiFetch(`/appointment`, "POST", { type, date, horary, payment });

// ─── INFORMAÇÕES (cards do topo) ─────────────────────────────────────────────
async function renderUserHeader(){
  const user = await getMyUser();

  user_picture.src = user.picture;
  user_name.innerText = "Bem-vindo " + user.name;

  if(user.roles.includes("ADMIN")){
    const btnAdm = document.querySelector("button");
    btnAdm.innerText = "Administração";
    header.appendChild(btnAdm);

    btnAdm.addEventListener('click', () => {
        window.setTimeout(() => {
            window.location = "./adminPanel";
        }, 2000);
    })
  }
}

async function renderMyAppoint(){
  const appt = await getMyAppt();
    if (!appt) return;

    const ul = myAppointment.querySelector("ul");
    ul.innerHTML = "";
    
    const li = createAppointmentCard(appt);
    ul.appendChild(li);
}

function createAppointmentCard(appt) {
    const li = document.createElement("li");
    li.classList.add("appt-card");

    // Informações
    const info = document.createElement("div");
    info.classList.add("appt-info");

    const name = document.createElement("span");
    name.classList.add("appt-name");
    name.innerText = appt.payment;

    const details = document.createElement("span");
    details.classList.add("appt-details");
    details.textContent = `${formatDate(appt.horary)} • ${appt.type}`;

    info.appendChild(name);
    info.appendChild(details);

    const status = document.createElement("span");
    status.innerText = appt.status;

    if(appt.status == "PROGRESS"){
        status.classList.add("pending");
    }
    if(appt.status == "DENIED"){
        status.classList.add("canceled");
    }
    if(appt.status == "ACCEPTED"){
        status.classList.add("confirmed");
    }
    
    status.addEventListener("click", () => updateStatus(appt.id, "DENIED"));

    li.appendChild(info)
    li.appendChild(status);

    return li;
}
// ─── AGENDAR ──────────────────────────────────────────────────

function createAppointment() {
    const serviceInput = create_appointment.querySelector("#service");
    const dateInput = create_appointment.querySelector("#date");
    const timeInput = create_appointment.querySelector("#time");

    const submitBtn = document.querySelector(".btn-create-appointment")
    document.querySelectorAll('input[type="radio"]').forEach(radio => {
        submitBtn.addEventListener("click", () => {
        
            const horary = new Date(`${dateInput.value}T${timeInput.value}`);

            if(radio.checked){
                console.log(horary, serviceInput.value, radio.id.toUpperCase())
                createAppt(horary, serviceInput.value, radio.id.toUpperCase())
            }
        });
    });  
}

// ─── INICIALIZAÇÃO ───────────────────────────────────────────────────────────
async function init() {
    await Promise.all([
        renderMyAppoint(),
        renderUserHeader(),
        createAppointment()
    ]);
}

init();