const API_DETAILS = "http://localhost:8088/api/sale-details";
const API_SALES_D = "http://localhost:8088/api/sales";
const API_PRODS_D = "http://localhost:8088/api/products";

let detailsCache = [];

function getAuthHeadersD() {
  return {
    "Authorization": "Bearer " + localStorage.getItem("token"),
    "Content-Type": "application/json"
  };
}

document.addEventListener("DOMContentLoaded", () => {
  loadDetails();
  loadSalesSelect();
  loadProductsSelect();
});

async function loadDetails() {
  try {
    const res = await fetch(API_DETAILS, { headers: getAuthHeadersD() });
    if (!res.ok) { console.error("Error cargando detalles:", res.status); return; }
    detailsCache = await res.json();
    renderDetails();
  } catch (err) { console.error("Error cargando detalles:", err); }
}

function renderDetails() {
  const tbody = document.querySelector("#sec-detalles tbody");
  if (!tbody) return;
  if (!detailsCache.length) {
    tbody.innerHTML = `<tr class="empty-row"><td colspan="8">No hay detalles de venta registrados.</td></tr>`;
    return;
  }
  tbody.innerHTML = detailsCache.map(d => `
    <tr>
      <td class="td-id">#${d.idSaleDetail}</td>
      <td>${d.productName ?? "-"}</td>
      <td>#${d.saleCode ?? "-"}</td>
      <td>${d.clientName ?? "-"}</td>
      <td>${d.amount ?? 0}</td>
      <td class="td-price">Q${formatDecimal(d.unitaryPrice)}</td>
      <td class="td-price">Q${formatDecimal(d.subtotal)}</td>
      <td>
        <div class="action-btns">
          <button class="btn-sm btn-edit" onclick="editDetail(${d.idSaleDetail})">Editar</button>
          <button class="btn-sm btn-del"  onclick="deleteDetail(${d.idSaleDetail})">Eliminar</button>
        </div>
      </td>
    </tr>
  `).join("");
}

function formatDecimal(val) {
  return Number(val || 0).toFixed(2);
}

async function loadSalesSelect() {
  try {
    const res = await fetch(API_SALES_D, { headers: getAuthHeadersD() });
    if (!res.ok) return;
    const sales = await res.json();
    const sel = document.getElementById("detail-sale");
    if (!sel) return;
    sel.innerHTML = sales.map(s =>
      `<option value="${s.idSale}">Factura #${s.saleCode ?? s.idSale} – ${s.clientName ?? "Sin cliente"}</option>`
    ).join("");
  } catch (err) { console.error(err); }
}

async function loadProductsSelect() {
  try {
    const res = await fetch(API_PRODS_D, { headers: getAuthHeadersD() });
    if (!res.ok) return;
    const prods = await res.json();
    const sel = document.getElementById("detail-product");
    if (!sel) return;
    sel.innerHTML = prods.map(p =>
      `<option value="${p.idProduct}" data-price="${p.price}">${p.productName} (Q${parseFloat(p.price).toFixed(2)})</option>`
    ).join("");
    sel.addEventListener("change", autoFillPrice);
    autoFillPrice();
  } catch (err) { console.error(err); }
}

function autoFillPrice() {
  const sel = document.getElementById("detail-product");
  if (!sel) return;
  const opt = sel.options[sel.selectedIndex];
  if (!opt) return;
  const price = opt.getAttribute("data-price") || "0";
  const up = document.getElementById("detail-unit-price");
  if (up) up.value = parseFloat(price).toFixed(2);
  calcSubtotal();
}

function calcSubtotal() {
  const qty   = parseFloat(document.getElementById("detail-amount")?.value) || 0;
  const price = parseFloat(document.getElementById("detail-unit-price")?.value) || 0;
  const sub   = document.getElementById("detail-subtotal");
  if (sub) sub.value = (qty * price).toFixed(2);
}

async function saveDetail() {
  const id           = document.getElementById("detail-id").value;
  const detailCode   = Number(document.getElementById("detail-code").value);
  const amount       = Number(document.getElementById("detail-amount").value);
  const unitaryPrice = parseFloat(document.getElementById("detail-unit-price").value);
  const subtotal     = parseFloat(document.getElementById("detail-subtotal").value);
  const productId    = Number(document.getElementById("detail-product").value);
  const saleId       = Number(document.getElementById("detail-sale").value);

  if (!amount || !unitaryPrice || !productId || !saleId) {
    alert("Completa todos los campos obligatorios");
    return;
  }

  const data   = { detailSaleCode: detailCode, amount, unitaryPrice, subtotal, productId, saleId };
  const method = id ? "PUT" : "POST";
  const url    = id ? `${API_DETAILS}/${id}` : API_DETAILS;

  try {
    const res = await fetch(url, {
      method,
      headers: getAuthHeadersD(),
      body: JSON.stringify(data)
    });
    if (!res.ok) {
      const err = await res.text();
      console.error("Backend:", err);
      alert("Error guardando detalle: " + err);
      return;
    }
    closeDetailModal();
    loadDetails();
  } catch (err) { console.error("Error guardando detalle:", err); }
}

function editDetail(id) {
  const d = detailsCache.find(x => x.idSaleDetail === id);
  if (!d) return;
  document.getElementById("detail-modal-title").textContent = "Editar Detalle";
  document.getElementById("detail-id").value         = d.idSaleDetail;
  document.getElementById("detail-code").value       = d.detailSaleCode ?? "";
  document.getElementById("detail-amount").value     = d.amount ?? "";
  document.getElementById("detail-unit-price").value = d.unitaryPrice ?? "";
  document.getElementById("detail-subtotal").value   = d.subtotal ?? "";
  setTimeout(() => {
    document.getElementById("detail-product").value = d.productId ?? "";
    document.getElementById("detail-sale").value    = d.saleId ?? "";
  }, 50);
  openDetailModal("Editar Detalle");
}

async function deleteDetail(id) {
  if (!confirm("¿Eliminar este detalle?")) return;
  try {
    await fetch(`${API_DETAILS}/${id}`, {
      method: "DELETE",
      headers: getAuthHeadersD()
    });
    loadDetails();
  } catch (err) { console.error("Error eliminando detalle:", err); }
}

function openDetailModal(title = "Nuevo Detalle") {
  const modal = document.getElementById("modal-detail");
  if (!modal) return;
  document.getElementById("detail-modal-title").textContent = title;
  modal.style.display = "flex";
}

function closeDetailModal() {
  const modal = document.getElementById("modal-detail");
  if (!modal) return;
  modal.style.display = "none";
  document.getElementById("detail-id").value         = "";
  document.getElementById("detail-code").value       = "";
  document.getElementById("detail-amount").value     = "";
  document.getElementById("detail-unit-price").value = "";
  document.getElementById("detail-subtotal").value   = "";
}

window.openDetailModal  = openDetailModal;
window.closeDetailModal = closeDetailModal;
window.saveDetail       = saveDetail;
window.editDetail       = editDetail;
window.deleteDetail     = deleteDetail;
window.calcSubtotal     = calcSubtotal;
window.autoFillPrice    = autoFillPrice;