// Menú. Ids requeridos en menu.html: menuGrid, userName, userRole, logoutBtn
const DESCRIPCIONES = {
  lotes: "Registra y consulta tus lotes de café verde, con puntaje de taza y precio.",
  marketplace: "Explora los lotes publicados por los caficultores de la comunidad.",
  ventas: "Revisa el estado de tus ventas de café verde.",
  compras: "Administra tus compras a los caficultores de la comunidad.",
  microcreditos: "Solicita o revisa apoyo de la comunidad para producción o empaque.",
  caficultores: "Administra las fincas y los caficultores de la comunidad.",
  compradores: "Administra los compradores registrados.",
  reportes: "Resumen de ventas, lotes y solicitudes.",
  usuarios: "Crea cuentas y asigna roles.",
  seguridad: "Revisa los accesos y cambios en el sistema."
};

function almacenActivo() {
  return sessionStorage.getItem("token") ? sessionStorage : localStorage;
}

function crear(tag, clase, texto) {
  const el = document.createElement(tag);
  if (clase) el.className = clase;
  if (texto) el.textContent = texto;
  return el;
}

document.addEventListener("DOMContentLoaded", async function () {
  const almacen = almacenActivo();
  if (!almacen.getItem("token")) { window.location.href = "index.html"; return; }

  document.getElementById("userName").textContent = almacen.getItem("usuario");
  document.getElementById("userRole").textContent = almacen.getItem("rol");

  document.getElementById("logoutBtn").addEventListener("click", async function () {
    try { await apiFetch("/api/auth/logout", { method: "POST" }); } catch (e) { /* se cierra igual */ }
    sessionStorage.clear();
    localStorage.clear();
    window.location.href = "index.html";
  });

  const grid = document.getElementById("menuGrid");
  try {
    const r = await apiFetch("/api/menu");
    if (r.status === 401) { sessionStorage.clear(); localStorage.clear(); window.location.href = "index.html"; return; }
    grid.innerHTML = "";
    (r.data.modulos || []).forEach(function (m) {
      const fila = crear("button", "modulo");
      fila.type = "button";
      fila.disabled = !m.disponible;
      fila.appendChild(crear("span", "nombre", m.nombre));
      if (!m.disponible) fila.appendChild(crear("span", "insignia", "Próximamente"));
      fila.appendChild(crear("span", "desc", DESCRIPCIONES[m.codigo] || ""));
      if (m.disponible) fila.addEventListener("click", function () { window.location.href = m.codigo + ".html"; });
      grid.appendChild(fila);
    });
  } catch (e) {
    grid.innerHTML = "";
    grid.appendChild(crear("p", "vacio", "No se pudo conectar con el servidor."));
  }
});
