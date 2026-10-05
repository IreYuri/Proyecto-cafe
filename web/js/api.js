// Utilidad para llamar a la API Java con el token de sesión.
async function apiFetch(ruta, opciones) {
  opciones = opciones || {};
  const headers = Object.assign({ "Content-Type": "application/json" }, opciones.headers || {});
  const token = sessionStorage.getItem("token") || localStorage.getItem("token");
  if (token) headers["Authorization"] = "Bearer " + token;

  const resp = await fetch(API_URL + ruta, Object.assign({}, opciones, { headers: headers }));
  let data = {};
  try { data = await resp.json(); } catch (e) { /* respuesta sin cuerpo */ }
  return { ok: resp.ok, status: resp.status, data: data };
}
