var API = 'http://localhost:8088/api';
const API_PRODUCTS = 'http://localhost:8088/api/products';

async function loadProducts() {
  try {
    const res = await fetch(API_PRODUCTS, {
      headers: { "Authorization": "Bearer " + localStorage.getItem("token") }
    });
    if (!res.ok) { console.error("Error HTTP:", res.status); return; }
    const text = await res.text();
    if (!text) { console.warn("Respuesta vacía"); return; }
    const data = JSON.parse(text);
    renderProducts(data);
    updateStats(data);
  } catch (err) { console.error(err); }
}

function renderProducts(data) {
  const tbody = document.getElementById("tbody-productos");
  if (!tbody) return;
  if (!data.length) {
    tbody.innerHTML = `<tr class="empty-row"><td colspan="6">No hay productos</td></tr>`;
    return;
  }
  tbody.innerHTML = data.map(p => `
    <tr>
      <td class="td-id">#${p.idProduct}</td>
      <td>
        ${p.imageUrl
          ? `<img src="${p.imageUrl}" style="width:40px;height:40px;object-fit:cover;border-radius:6px;" onerror="this.src='https://via.placeholder.com/40?text=IMG'">`
          : 'Sin imagen'}
      </td>
      <td>${p.productName}</td>
      <td class="td-price">Q${parseFloat(p.price).toFixed(2)}</td>
      <td class="${getStockClass(p.stock)}">${p.stock}</td>
      <td>
        <div class="action-btns">
          <button class="btn-sm btn-edit" onclick="openEditProduct(${p.idProduct},'${p.productName}',${p.price},${p.stock},${p.productCode || 0},'${p.stateProduct || "CONTINUED"}','${p.imageUrl || ""}')">Editar</button>
          <button class="btn-sm btn-del" onclick="deleteProduct(${p.idProduct})">Eliminar</button>
        </div>
      </td>
    </tr>
  `).join("");
}

function getStockClass(stock) {
  if (stock <= 0) return "td-stock-out";
  if (stock <= 5) return "td-stock-low";
  return "td-stock-ok";
}

function openProductModal() {
  document.getElementById("modal-title").textContent = "Nuevo Producto";
  document.getElementById("prod-id").value    = "";
  document.getElementById("prod-name").value  = "";
  document.getElementById("prod-price").value = "";
  document.getElementById("prod-stock").value = "";
  document.getElementById("prod-code").value  = "";
  document.getElementById("prod-state").value = "CONTINUED";
  document.getElementById("modal-product").style.display = "flex";
}

function openEditProduct(id, name, price, stock, code, state, image) {
  openProductModal();
  document.getElementById("modal-title").textContent = "Editar Producto";
  document.getElementById("prod-id").value    = id;
  document.getElementById("prod-name").value  = name;
  document.getElementById("prod-price").value = price;
  document.getElementById("prod-stock").value = stock;
  document.getElementById("prod-code").value  = code || "";
  document.getElementById("prod-state").value = state || "CONTINUED";
  const imgInput = document.getElementById("prod-image");
  if (imgInput) imgInput.value = image || "";
}

function closeProductModal() {
  document.getElementById("modal-product").style.display = "none";
}

async function saveProduct() {
  const id    = document.getElementById("prod-id").value;
  const name  = document.getElementById("prod-name").value;
  const price = document.getElementById("prod-price").value;
  const stock = document.getElementById("prod-stock").value;
  const code  = document.getElementById("prod-code").value;
  const state = document.getElementById("prod-state").value;
  const file  = document.getElementById("prod-file")?.files[0];

  if (!name || !price || !stock) { alert("Completa todos los campos"); return; }
  if (price <= 0)  { alert("El precio debe ser mayor a 0"); return; }
  if (stock < 0)   { alert("El stock no puede ser negativo"); return; }

  let imageUrl = document.getElementById("prod-image")?.value || "";

  try {
    if (file) {
      const formData = new FormData();
      formData.append("file", file);
      const uploadRes = await fetch(`${API}/products/upload`, {
        method: "POST",
        headers: { "Authorization": "Bearer " + localStorage.getItem("token") },
        body: formData
      });
      if (!uploadRes.ok) throw new Error("Error subiendo imagen");
      imageUrl = await uploadRes.text();
    }

    const payload = {
      productName: name,
      price: parseFloat(price),
      stock: parseInt(stock),
      productCode: parseInt(code) || 0,
      stateProduct: state,
      imageUrl: imageUrl
    };

    const method = id ? "PUT" : "POST";
    const url    = id ? `${API}/products/${id}` : `${API}/products`;

    const res = await fetch(url, {
      method,
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + localStorage.getItem("token")
      },
      body: JSON.stringify(payload)
    });

    if (!res.ok) throw new Error("Error guardando producto");
    closeProductModal();
    loadProducts();
  } catch (err) {
    console.error(err);
    alert("Error guardando producto: " + err.message);
  }
}

async function deleteProduct(id) {
  if (!confirm("¿Eliminar producto?")) return;
  try {
    await fetch(`${API_PRODUCTS}/${id}`, {
      method: "DELETE",
      headers: { "Authorization": "Bearer " + localStorage.getItem("token") }
    });
    loadProducts();
  } catch (err) { console.error(err); }
}

function updateStats(data) {
  const total = document.getElementById("total-products");
  const stock = document.getElementById("total-stock");
  const low   = document.getElementById("low-stock");
  if (total) total.textContent = data.length;
  if (stock) stock.textContent = data.reduce((sum, p) => sum + (p.stock || 0), 0);
  if (low)   low.textContent   = data.filter(p => p.stock <= 5).length;
}

window.openProductModal  = openProductModal;
window.openEditProduct   = openEditProduct;
window.closeProductModal = closeProductModal;
window.deleteProduct     = deleteProduct;
window.saveProduct       = saveProduct;

document.addEventListener("DOMContentLoaded", () => {
  loadProducts();
  const fileInput = document.getElementById("prod-file");
  if (fileInput) {
    fileInput.addEventListener("change", function(e) {
      const file = e.target.files[0];
      if (!file) return;
      const preview = document.getElementById("preview");
      if (preview) { preview.src = URL.createObjectURL(file); preview.style.display = "block"; }
    });
  }
});