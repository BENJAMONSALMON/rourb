var API = 'http://localhost:8088/api';
let cart = [];
let currentProduct = null;
window._products = [];

function authHeaders() {
  return {
    'Authorization': 'Bearer ' + localStorage.getItem('token'),
    'Content-Type': 'application/json'
  };
}

const usernameDisplay = document.getElementById('username-display');
if (usernameDisplay) usernameDisplay.textContent = localStorage.getItem('rourb_user') || '-';

if (document.getElementById('product-grid')) {
  loadProducts();
}

async function loadProducts() {
  try {
    const res = await fetch(`${API}/products/active`, { headers: authHeaders() });
    if (!res.ok) throw new Error(res.status);
    const products = await res.json();
    window._products = products;
    renderProducts(products);
  } catch (err) {
    console.error('Error:', err);
    const grid = document.getElementById('product-grid');
    if (grid) grid.innerHTML = `<div class="empty-state"><div class="icon">⚠️</div><p>Error al cargar. Verifica tu sesión.</p></div>`;
  }
}

function renderProducts(list) {
  const countEl = document.getElementById('prod-count');
  if (countEl) countEl.textContent = `${list.length} producto${list.length !== 1 ? 's' : ''}`;
  const grid = document.getElementById('product-grid');
  if (!grid) return;
  if (!list.length) {
    grid.innerHTML = `<div class="empty-state"><div class="icon">📦</div><p>No hay productos disponibles.</p></div>`;
    return;
  }
  grid.innerHTML = list.map((p, i) => `
    <div class="product-card" style="animation-delay:${i*0.06}s" onclick="openProductModal(${p.idProduct})">
      <div class="card-img">
        ${p.imageUrl
          ? `<img src="${p.imageUrl}" alt="${p.productName}" onerror="this.parentElement.innerHTML='<div class=card-img-placeholder>📦</div>'">`
          : `<div class="card-img-placeholder">📦</div>`}
      </div>
      <div class="card-body">
        <div class="card-name">${p.productName}</div>
        <div class="card-desc">Colección ROURB Street Culture. Haz clic para ver más.</div>
        <div class="card-meta">
          <div class="card-price">Q${parseFloat(p.price).toFixed(2)}</div>
          <div class="card-stock">${p.stock} disponibles</div>
        </div>
        <button class="card-add" onclick="event.stopPropagation(); quickAdd(${p.idProduct})">
          <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
          Agregar
        </button>
      </div>
    </div>
  `).join('');
}

function openProductModal(id) {
  const p = window._products.find(x => x.idProduct === id);
  if (!p) return;
  currentProduct = p;
  const wrap = document.getElementById('modal-img-wrap');
  if (!wrap) return;
  wrap.innerHTML = p.imageUrl
    ? `<img src="${p.imageUrl}" alt="${p.productName}" onerror="this.parentElement.innerHTML='<div class=modal-img-placeholder>📦</div>'">`
    : `<div class="modal-img-placeholder">📦</div>`;
  document.getElementById('modal-prod-name').textContent  = p.productName;
  document.getElementById('modal-prod-desc').textContent  = 'Producto exclusivo de la colección ROURB Street Culture. Calidad y estilo urbano en cada pieza.';
  document.getElementById('modal-prod-price').textContent = `Q${parseFloat(p.price).toFixed(2)}`;
  document.getElementById('modal-prod-stock').textContent = `${p.stock} disponibles`;
  document.getElementById('modal-product').classList.add('open');
}

function handleOverlayClick(e) {
  if (e.target === document.getElementById('modal-product')) closeProductModal();
}

function closeProductModal() {
  const modal = document.getElementById('modal-product');
  if (modal) modal.classList.remove('open');
  currentProduct = null;
}

function addCurrentToCart() {
  if (!currentProduct) return;
  addToCart(currentProduct);
  closeProductModal();
}

function quickAdd(id) {
  const p = window._products.find(x => x.idProduct === id);
  if (p) addToCart(p);
}

function addToCart(p) {
  const item = cart.find(i => i.product.idProduct === p.idProduct);
  if (item) {
    if (item.qty >= p.stock) { showToast('Sin más stock disponible'); return; }
    item.qty++;
  } else {
    cart.push({ product: p, qty: 1 });
  }
  updateCartBadge();
  renderCartItems();
  showToast(`✓ "${p.productName}" agregado`);
}

function removeFromCart(id) {
  cart = cart.filter(i => i.product.idProduct !== id);
  updateCartBadge();
  renderCartItems();
}

function changeQty(id, delta) {
  const item = cart.find(i => i.product.idProduct === id);
  if (!item) return;
  item.qty = Math.max(0, Math.min(item.product.stock, item.qty + delta));
  if (item.qty === 0) removeFromCart(id);
  else { updateCartBadge(); renderCartItems(); }
}

function updateCartBadge() {
  const n = cart.reduce((s, i) => s + i.qty, 0);
  const badge = document.getElementById('cart-count');
  const btn   = document.getElementById('btn-checkout');
  if (badge) badge.textContent = n;
  if (btn)   btn.disabled = n === 0;
}

function renderCartItems() {
  const el = document.getElementById('cart-items');
  if (!el) return;
  if (!cart.length) {
    el.innerHTML = `<div class="cart-empty">Tu carrito está vacío</div>`;
    const t = document.getElementById('cart-total');
    if (t) t.textContent = 'Q0.00';
    return;
  }
  el.innerHTML = cart.map(i => `
    <div class="cart-item">
      <div class="cart-item-thumb">
        ${i.product.imageUrl ? `<img src="${i.product.imageUrl}" onerror="this.style.display='none'">` : '📦'}
      </div>
      <div class="cart-item-info">
        <div class="cart-item-name">${i.product.productName}</div>
        <div class="cart-item-price">Q${(parseFloat(i.product.price)*i.qty).toFixed(2)}</div>
      </div>
      <div class="cart-item-qty">
        <button class="qty-btn" onclick="changeQty(${i.product.idProduct},-1)">−</button>
        <span>${i.qty}</span>
        <button class="qty-btn" onclick="changeQty(${i.product.idProduct},1)">+</button>
      </div>
    </div>
  `).join('');
  const total = cart.reduce((s,i) => s + parseFloat(i.product.price)*i.qty, 0);
  const t = document.getElementById('cart-total');
  if (t) t.textContent = `Q${total.toFixed(2)}`;
}

function openCart() {
  document.getElementById('cart-overlay')?.classList.add('open');
  document.getElementById('cart-drawer')?.classList.add('open');
}

function closeCart() {
  document.getElementById('cart-overlay')?.classList.remove('open');
  document.getElementById('cart-drawer')?.classList.remove('open');
}

function openCheckout() {
  const saved = JSON.parse(localStorage.getItem('rourb_client_info') || '{}');
  if (saved.name)     document.getElementById('co-name').value     = saved.name;
  if (saved.lastname) document.getElementById('co-lastname').value = saved.lastname;
  if (saved.dpi)      document.getElementById('co-dpi').value      = saved.dpi;
  if (saved.address)  document.getElementById('co-address').value  = saved.address;

  const total = cart.reduce((s,i) => s + parseFloat(i.product.price)*i.qty, 0);
  const rows  = cart.map(i =>
    `<div class="summary-row"><span>${i.product.productName} ×${i.qty}</span><span>Q${(parseFloat(i.product.price)*i.qty).toFixed(2)}</span></div>`
  ).join('');
  document.getElementById('checkout-summary').innerHTML =
    rows + `<div class="summary-row total"><span>Total</span><span>Q${total.toFixed(2)}</span></div>`;

  closeCart();
  document.getElementById('checkout-modal').classList.add('open');
}

function closeCheckout() {
  document.getElementById('checkout-modal').classList.remove('open');
  openCart();
}

async function confirmCheckout() {
  const name     = document.getElementById('co-name').value.trim();
  const lastname = document.getElementById('co-lastname').value.trim();
  const dpi      = document.getElementById('co-dpi').value.trim();
  const address  = document.getElementById('co-address').value.trim();
  const state    = document.getElementById('co-state').value;

  if (!name || !dpi || !address) {
    showToast('Nombre, DPI y dirección son obligatorios');
    return;
  }

  localStorage.setItem('rourb_client_info', JSON.stringify({ name, lastname, dpi, address }));

  const userId = Number(localStorage.getItem('user_id'));
  const total  = cart.reduce((s,i) => s + parseFloat(i.product.price)*i.qty, 0);

  try {
    let clientId = Number(localStorage.getItem('client_id') || '0');

    if (!clientId) {
      const cr = await fetch(`${API}/clients`, {
        method: 'POST',
        headers: authHeaders(),
        body: JSON.stringify({
          clientDpi: Number(dpi),
          clientName: name,
          clientLastName: lastname,
          address,
          state,
          userId
        })
      });
      if (cr.ok) {
        const cd = await cr.json();
        clientId = cd.id || cd.idClient;
        if (clientId) localStorage.setItem('client_id', clientId);
      }
    }

    if (!clientId) { showToast('No se pudo registrar el cliente'); return; }

    const sr = await fetch(`${API}/sales`, {
      method: 'POST',
      headers: authHeaders(),
      body: JSON.stringify({
        saleCode: Math.floor(Math.random() * 999999),
        total,
        stateSale: 'IN_PROCESS',
        clientId,
        userId
      })
    });

    if (!sr.ok) {
      const errData = await sr.json();
      console.log('Error al crear venta:', errData);
      localStorage.removeItem('client_id');
      throw new Error('venta');
    }

    const sd     = await sr.json();
    const saleId = sd.id || sd.idSale;

    for (const item of cart) {
      await fetch(`${API}/sale-details`, {
        method: 'POST',
        headers: authHeaders(),
        body: JSON.stringify({
          detailSaleCode: saleId,
          amount: item.qty,
          unitaryPrice: parseFloat(item.product.price),
          subtotal: parseFloat(item.product.price) * item.qty,
          productId: item.product.idProduct,
          saleId
        })
      });

      await fetch(`${API}/products/${item.product.idProduct}`, {
        method: 'PUT',
        headers: authHeaders(),
        body: JSON.stringify({
          productName: item.product.productName,
          price: item.product.price,
          stock: item.product.stock - item.qty,
          imageUrl: item.product.imageUrl || ""
        })
      });
    }

    cart = [];
    updateCartBadge();
    renderCartItems();
    document.getElementById('checkout-modal').classList.remove('open');
    showToast('¡Pedido realizado con éxito!');

  } catch (err) {
    console.error(err);
    showToast('Error al procesar el pedido');
  }
}

function showToast(msg) {
  const t = document.getElementById('cart-toast');
  if (!t) return;
  t.textContent = msg;
  t.classList.add('show');
  setTimeout(() => t.classList.remove('show'), 2800);
}