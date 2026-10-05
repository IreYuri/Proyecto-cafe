// Login. Ids requeridos en index.html: loginForm, usuario, clave, errorMsg, submitBtn,
// verClave, usarDemo, recordar, y radios name="perfil".

const CUENTAS_DEMO = {
  CAFICULTOR: "caficultor1",
  COMPRADOR: "comprador1",
  ADMINISTRADOR: "admin"
};
const CLAVE_DEMO = "Admin123*";

function perfilSeleccionado() {
  const radio = document.querySelector('input[name="perfil"]:checked');
  return radio ? radio.value : null;
}

document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("loginForm");
  const errorMsg = document.getElementById("errorMsg");
  const btn = document.getElementById("submitBtn");
  const campoUsuario = document.getElementById("usuario");
  const campoClave = document.getElementById("clave");

  document.getElementById("verClave").addEventListener("click", function () {
    const oculta = campoClave.type === "password";
    campoClave.type = oculta ? "text" : "password";
    this.textContent = oculta ? "Ocultar" : "Mostrar";
  });

  document.getElementById("usarDemo").addEventListener("click", function () {
    campoUsuario.value = CUENTAS_DEMO[perfilSeleccionado()] || "";
    campoClave.value = CLAVE_DEMO;
  });

  function mostrarError(texto) {
    errorMsg.textContent = texto;
    errorMsg.style.display = "block";
  }

  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    errorMsg.style.display = "none";
    btn.disabled = true;
    try {
      const r = await apiFetch("/api/auth/login", {
        method: "POST",
        body: JSON.stringify({
          usuario: campoUsuario.value,
          clave: campoClave.value,
          perfil: perfilSeleccionado()
        })
      });
      if (r.ok) {
        const recordar = document.getElementById("recordar").checked;
        const almacen = recordar ? localStorage : sessionStorage;
        almacen.setItem("token", r.data.token);
        almacen.setItem("usuario", r.data.usuario);
        almacen.setItem("rol", r.data.rol);
        window.location.href = "menu.html";
      } else {
        let texto = r.data.mensaje || "No se pudo iniciar sesión.";
        if (r.data.intentosRestantes !== undefined) {
          texto += " Te quedan " + r.data.intentosRestantes + " intento(s).";
        }
        mostrarError(texto);
      }
    } catch (err) {
      mostrarError("No se pudo conectar con el servidor.");
    } finally {
      btn.disabled = false;
    }
  });
});
