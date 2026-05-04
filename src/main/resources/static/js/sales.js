var API = 'http://localhost:8088/api';
const CLIENT_API = "http://localhost:8088/api/clients";

let salesCache = [];

function getAuthHeaders() {
  return {
    "Authorization": "Bearer " + localStorage.getItem("token"),
    "Content-Type": "application/json"
  };
}

document.addEventListener("DOMContentLoaded", () => {
  loadSales();
  loadClients();
});

async function loadSales() {
  try {
    const res = await fetch(`${API}/sales`, { headers: getAuthHeaders() });
    if (!res.ok) { console.error("Error cargando ventas:", res.status); return; }
    salesCache = await res.json();
    renderSales();
  } catch (err) { console.error("Error cargando ventas:", err); }
}

async function loadClients() {
  try {
    const res = await fetch(CLIENT_API, { headers: getAuthHeaders() });
    if (!res.ok) { console.error("Error cargando clientes:", res.status); return; }
    const clients = await res.json();
    const select = document.getElementById("sale-client");
    if (!select) return;
    select.innerHTML = clients.map(c =>
      `<option value="${c.idClient}">${c.clientName} (ID: ${c.idClient})</option>`
    ).join("");
  } catch (err) { console.error("Error cargando clientes:", err); }
}

async function saveSale() {
  const id       = document.getElementById("sale-id")?.value;
  const userId   = Number(localStorage.getItem("user_id"));
  const clientId = Number(document.getElementById("sale-client").value);
  const saleCode = Number(document.getElementById("sale-code").value);
  const total    = parseFloat(document.getElementById("sale-total").value);
  const stateSale = document.getElementById("sale-state").value;

  if (!userId || !clientId || !saleCode || !total) {
    alert("Completa todos los campos");
    return;
  }

  const data   = { saleCode, total, stateSale, clientId, userId };
  const method = id ? "PUT" : "POST";
  const url    = id ? `${API}/sales/${id}` : `${API}/sales`;

  try {
    const res = await fetch(url, {
      method,
      headers: getAuthHeaders(),
      body: JSON.stringify(data)
    });
    if (!res.ok) {
      const errBody = await res.text();
      console.error("Backend respondió:", errBody);
      throw new Error("Error guardando venta: " + res.status);
    }
    closeSaleModal();
    loadSales();
  } catch (err) { console.error("Error guardando venta:", err); }
}

async function deleteSale(id) {
  if (!confirm("¿Seguro que quieres eliminar esta factura?")) return;
  try {
    await fetch(`${API}/sales/${id}`, {
      method: "DELETE",
      headers: getAuthHeaders()
    });
    loadSales();
  } catch (err) { console.error("Error eliminando venta:", err); }
}

function renderSales() {
  const tbody = document.querySelector("#sec-ventas tbody");
  if (!tbody) return;
  if (!salesCache.length) {
    tbody.innerHTML = `<tr class="empty-row"><td colspan="6">No hay ventas registradas</td></tr>`;
    return;
  }
  tbody.innerHTML = salesCache.map(s => `
    <tr>
      <td class="td-id">#${s.idSale}</td>
      <td>${s.saleCode ?? "-"}</td>
      <td>${s.clientName ?? "Sin cliente"}</td>
      <td>${formatDate(s.saleDate)}</td>
      <td class="td-price">Q ${formatMoney(s.total)}</td>
      <td>
        <div class="action-btns">
          <button class="btn-sm btn-edit" onclick="viewSale(${s.idSale})">Ver</button>
          <button class="btn-sm btn-edit" onclick="editSale(${s.idSale})">Editar</button>
          <button class="btn-sm btn-del"  onclick="deleteSale(${s.idSale})">Eliminar</button>
        </div>
      </td>
    </tr>
  `).join("");
}

function formatDate(date) {
  if (!date) return "-";
  return new Date(date).toLocaleDateString();
}

function formatMoney(value) {
  return Number(value || 0).toFixed(2);
}

function viewSale(id) {
  const sale = salesCache.find(s => s.idSale === id);
  if (!sale) return;
  alert(`FACTURA #${sale.idSale}\n\nCliente: ${sale.clientName}\nFecha: ${formatDate(sale.saleDate)}\nEstado: ${sale.stateSale}\n\nTOTAL: Q ${formatMoney(sale.total)}`);
}

function editSale(id) {
  const sale = salesCache.find(s => s.idSale === id);
  if (!sale) return;
  document.getElementById("sale-id").value     = sale.idSale;
  document.getElementById("sale-code").value   = sale.saleCode;
  document.getElementById("sale-total").value  = sale.total;
  document.getElementById("sale-state").value  = sale.stateSale || "";
  document.getElementById("sale-client").value = sale.clientId || "";
  openSaleModal("Editar Factura");
}

function openSaleModal(title = "Nueva Factura") {
  const el = document.getElementById("modal-sale-title");
  if (el) el.innerText = title;
  document.getElementById("modal-sale").style.display = "flex";
}

function closeSaleModal() {
  document.getElementById("modal-sale").style.display = "none";
  document.getElementById("sale-id").value     = "";
  document.getElementById("sale-code").value   = "";
  document.getElementById("sale-total").value  = "";
  document.getElementById("sale-state").value  = "ACTIVE";
  document.getElementById("sale-client").value = "";
}

window.saveSale       = saveSale;
window.deleteSale     = deleteSale;
window.editSale       = editSale;
window.viewSale       = viewSale;
window.openSaleModal  = openSaleModal;
window.closeSaleModal = closeSaleModal;