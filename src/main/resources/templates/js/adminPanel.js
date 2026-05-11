const BASE_URL = "https://hairremovalappointment.onrender.com/api/v1";

// ─── ELEMENTOS DO DOM ────────────────────────────────────────────────────────
const errorOverlay  = document.querySelector(".error-overlay");
const errorMessage  = errorOverlay.querySelector(".error-message");
const errorButton   = errorOverlay.querySelector("button");

const header = document.querySelector("header");
const user_picture = header.querySelector("#user-picture");
const user_name = header.querySelector("#user-name")

const pendingAppointments = document.querySelector("#pending-appointments");
const userList            = document.querySelector("#user-list");
const calendarSection     = document.querySelector("#calendar");
const informations        = document.querySelector("#informations");

// ─── UTILITÁRIOS ─────────────────────────────────────────────────────────────
function getAuthHeaders() {
    const headers = new Headers();
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", `Bearer ${localStorage.getItem("Authorization")}`);
    return headers;
}

function formatDate(dateInput) {
    const date = new Date(dateInput);
    return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;
}

function calculateAge(birthday) {
    const birth = new Date(birthday);
    return Math.abs(birth.getFullYear() - new Date().getFullYear());
}

function showErrorOverlay(message) {
    errorMessage.innerText = message ?? "Erro desconhecido.";
    errorOverlay.style.display = "block";
}

function hideErrorOverlay() {
    errorOverlay.style.display = "none";
}

errorButton.addEventListener("click", hideErrorOverlay);

// ─── NAVEGAÇÃO ───────────────────────────────────────────────────────────────
function option(selected) {
    pendingAppointments.style.display = selected === "geral"    ? "block" : "none";
    userList.style.display            = selected === "users"    ? "block" : "none";
    calendarSection.style.display     = selected === "calendar" ? "block" : "none";
}

// ─── API ─────────────────────────────────────────────────────────────────────
async function apiFetch(path, method = "GET", body = null) {
    try {
        const options = { method, headers: getAuthHeaders() };
        if (body) options.body = JSON.stringify(body);

        const response = await fetch(`${BASE_URL}${path}`, options);

        if (!response.ok) {
            const text  = await response.text();
            const error = JSON.parse(text);
            showErrorOverlay(error.detail ?? error.message ?? text);
            return null;
        }

        return await response.json();
    } catch (err) {
        showErrorOverlay("Erro de conexão com o servidor.");
        return null;
    }
}

const getAllUsers = ()        => apiFetch("/user");
const getMyUser  = ()        => apiFetch("/user/my");
const getAllAppt  = ()        => apiFetch("/appointment");
const updateStatus = (id, status) => apiFetch(`/appointment/${id}`, "PATCH", { status });

// ─── INFORMAÇÕES (cards do topo) ─────────────────────────────────────────────
async function renderUserHeader(){
  const user = await getMyUser();

  user_picture.src = user.picture;
  user_name.innerText = "Bem-vindo " + user.name;
}

async function setInfo() {
    const [appts, users] = await Promise.all([getAllAppt(), getAllUsers()]);
    if (!appts || !users) return;

    const pending = appts.filter(a => a.status.includes("PROGRESS"));

    informations.querySelector("#appts").querySelector("span").innerText = appts.length;
    informations.querySelector("#pendings").querySelector("span").innerText = pending.length;
    informations.querySelector("#users").querySelector("span").innerText    = users.length;
}

// ─── AGENDAMENTOS PENDENTES ──────────────────────────────────────────────────
async function renderPendingAppointments() {
    const appts = await getAllAppt();
    if (!appts) return;

    const ul = pendingAppointments.getElementsByTagName("ul")[0];
    ul.innerHTML = "";

    appts.forEach(appt => {
        if(appt.status.includes("PROGRESS")){
          const li = createAppointmentCard(appt);
          console.log("PROGRESS" + appt)
          ul.appendChild(li);
        }
        
    });
}

async function renderNotPeddingAppts() {
    const appts = await getAllAppt();
    if (!appts) return;

    const ul = pendingAppointments.getElementsByTagName("ul")[1];
    ul.innerHTML = "";

    appts.forEach(appt => {
        if(!appt.status.includes("PROGRESS")){
          const li = createAppointmentCard(appt);
          console.log("NOT PROGRESS" + appt)
          ul.appendChild(li);
        }
        
    });
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

    
  const actions = document.createElement("div");
    actions.classList.add("appt-actions");
    if(appt.status.includes("PROGRESS")){
      // Ações
      

      const denyBtn = document.createElement("button");
      denyBtn.innerText = "Recusar";
      denyBtn.classList.add("btn-reject");
      denyBtn.addEventListener("click", () => updateStatus(appt.id, "DENIED"));

      const acceptBtn = document.createElement("button");
      acceptBtn.innerText = "Confirmar";
      acceptBtn.classList.add("btn-accept");
      acceptBtn.addEventListener("click", () => updateStatus(appt.id, "ACCEPTED"));

      actions.appendChild(denyBtn);
      actions.appendChild(acceptBtn);
    }else{
      
      const status = document.createElement("button");
      
      status.innerText = appt.status.includes("DENIED") ? "Negado" : "ACEITO";
      status.classList.add(appt.status.includes("ACCEPTED") ? "btn-accept" : "btn-reject")
      actions.appendChild(status);

      const updateToProgress = document.createElement("button");

      updateToProgress.innerText = "Colocar em espera";
      updateToProgress.classList.add("btn-progress");
      updateToProgress.addEventListener("click", () => updateStatus(appt.id, "PROGRESS"));

      
      actions.appendChild(updateToProgress);
      
    }   

    li.appendChild(info);
    li.appendChild(actions);

    return li;
}

// ─── LISTA DE USUÁRIOS ───────────────────────────────────────────────────────
async function renderUserList() {
    const users = await getAllUsers();
    if (!users) return;

    const ul = userList.querySelector("ul");
    ul.innerHTML = "";

    users.forEach(user => {
        const li = createUserCard(user);
        ul.appendChild(li);
    });
}

function createUserCard(user) {
    const li = document.createElement("li");
    li.classList.add("user-card");

    const info = document.createElement("div");
    info.classList.add("user-info");

    const name = document.createElement("span");
    name.classList.add("user-name");
    name.innerText = user.name;

    const birthday = document.createElement("span");
    birthday.classList.add("user-birthday");
    birthday.textContent = `${formatDate(user.birthday)} • ${calculateAge(user.birthday)} anos`;

    info.appendChild(name);
    info.appendChild(birthday);

    const contact = document.createElement("div");
    contact.classList.add("user-contact");

    const email = document.createElement("span");
    email.classList.add("user-email");
    email.textContent = user.email;

    const phone = document.createElement("span");
    phone.classList.add("user-phone");
    phone.textContent = user.phoneNumber;

    contact.appendChild(email);
    contact.appendChild(phone);

    li.appendChild(info);
    li.appendChild(contact);

    return li;
}

// ─── CALENDÁRIO ──────────────────────────────────────────────────────────────
function generateCalendar(year, month, appointmentDays = []) {
    const monthNames = [
        "Janeiro","Fevereiro","Março","Abril","Maio","Junho",
        "Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"
    ];

    const today    = new Date();
    const lastDay  = new Date(year, month + 1, 0).getDate();

    let startWeekDay = new Date(year, month, 1).getDay();
    startWeekDay = startWeekDay === 0 ? 6 : startWeekDay - 1;

    document.querySelector("#calendar h4").textContent =
        `Calendário — ${monthNames[month]} ${year}`;

    const tbody = document.querySelector(".calendar-table tbody");
    tbody.innerHTML = "";

    let day = 1;
    for (let row = 0; row < 6; row++) {
        if (day > lastDay) break;

        const tr = document.createElement("tr");

        for (let col = 0; col < 7; col++) {
            const td = document.createElement("td");

            if ((row === 0 && col < startWeekDay) || day > lastDay) {
                td.textContent = "";
            } else {
                td.textContent = day;

                const isToday =
                    day   === today.getDate()  &&
                    month === today.getMonth() &&
                    year  === today.getFullYear();

                if (isToday) {
                    td.classList.add("today");
                } else if (appointmentDays.includes(day)) {
                    td.classList.add("has-appt");
                }

                day++;
            }

            tr.appendChild(td);
        }

        tbody.appendChild(tr);
    }
}

async function renderCalendar() {
    const appts = await getAllAppt();
    const now   = new Date();
    const days  = appts
        ? appts.map(appt => new Date(appt.horary).getDate())
        : [];

    generateCalendar(now.getFullYear(), now.getMonth(), days);
}

// ─── INICIALIZAÇÃO ───────────────────────────────────────────────────────────
async function init() {
    await Promise.all([
        setInfo(),
        renderUserHeader(),
        renderPendingAppointments(),
        renderNotPeddingAppts(),
        renderUserList(),
        renderCalendar(),
    ]);
}

init();