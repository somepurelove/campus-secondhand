// ==================== 全局配置 ====================
const API_BASE = 'http://localhost:8080/api';

// 分类图标映射
const CATEGORY_ICONS = {
    0: '🏪', 1: '📱', 2: '👕', 3: '📚', 4: '⚽', 5: '🏠', 6: '📦'
};

// 商品ID到图片的映射（根据数据库真实商品）
const PRODUCT_IMAGE_MAP = {
    6: 'images/product_6.jpg',   // 二手iPhone 13 Pro
    7: 'images/product_7.jpg',   // 高等数学教材全套
    8: 'images/product_8.jpg',   // 耐克篮球鞋42码
    9: 'images/product_9.jpg',   // 小米护眼台灯
    10: 'images/product_10.jpg', // 机械键盘青轴
    11: 'images/product_11.jpg', // 考研英语真题
};

// 分类ID到图片的映射
const CATEGORY_IMAGES = {
    7: 'images/category_digital.jpg',  // 数码产品
    8: 'images/clothes.jpg',           // 服装鞋包
    9: 'images/category_books.jpg',    // 书籍资料
    10: 'images/category_sports.jpg',  // 运动户外
    11: 'images/category_daily.jpg',   // 生活用品
    12: 'images/others.jpg'            // 其他
};

// 商品名称关键词到图片的映射（作为备选）
const PRODUCT_NAME_TO_IMAGE = {
    'iphone': 'images/product_6.jpg',
    '手机': 'images/product_6.jpg',
    '数学': 'images/product_7.jpg',
    '教材': 'images/product_7.jpg',
    '篮球鞋': 'images/product_8.jpg',
    '鞋': 'images/product_8.jpg',
    '台灯': 'images/product_9.jpg',
    '灯': 'images/product_9.jpg',
    '键盘': 'images/product_10.jpg',
    '英语': 'images/product_11.jpg',
    '真题': 'images/product_11.jpg'
};

// 订单状态映射
const ORDER_STATUS_MAP = {
    'pending': { text: '待付款', class: 'status-pending' },
    'paid': { text: '已付款', class: 'status-paid' },
    'shipped': { text: '已发货', class: 'status-shipped' },
    'completed': { text: '已完成', class: 'status-completed' },
    'cancelled': { text: '已取消', class: 'status-cancelled' }
};

// 商品状态映射
const PRODUCT_STATUS_MAP = {
    'pending': '待审核',
    'approved': '在售',
    'rejected': '已拒绝',
    'sold_out': '已售罄'
};

// ==================== Toast 通知系统 ====================
function showToast(message, type = 'info', duration = 3000) {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;

    const icons = { success: '✅', error: '❌', info: 'ℹ️', warning: '⚠️' };
    toast.innerHTML = `<span>${icons[type] || 'ℹ️'}</span><span>${message}</span>`;

    container.appendChild(toast);
    // 触发动画
    requestAnimationFrame(() => toast.classList.add('show'));

    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, duration);
}

// ==================== 工具函数 ====================
function getUser() {
    try {
        return JSON.parse(localStorage.getItem('user'));
    } catch { return null; }
}

function isLoggedIn() {
    return getUser() !== null;
}

function requireLogin(action) {
    if (!isLoggedIn()) {
        showToast('请先登录后再操作', 'warning');
        document.getElementById('login-modal').style.display = 'block';
        return false;
    }
    return true;
}

// 关闭所有模态框
function closeAllModals() {
    document.querySelectorAll('.modal').forEach(m => m.style.display = 'none');
}

// ==================== 页面初始化 ====================
document.addEventListener('DOMContentLoaded', function() {
    initPage();
    bindEvents();
});

function initPage() {
    loadProducts();
    updateLoginStatus();
    updateSectionLoginHints();
}

// ==================== 事件绑定 ====================
function bindEvents() {
    const loginModal = document.getElementById('login-modal');
    const registerModal = document.getElementById('register-modal');
    const detailModal = document.getElementById('detail-modal');
    const purchaseModal = document.getElementById('purchase-modal');
    const reviewModal = document.getElementById('review-modal');

    // 关闭按钮
    document.querySelectorAll('.close').forEach(btn => {
        btn.addEventListener('click', closeAllModals);
    });

    // 点击遮罩关闭
    [loginModal, registerModal, detailModal, purchaseModal, reviewModal].forEach(modal => {
        modal.addEventListener('click', function(e) {
            if (e.target === this) this.style.display = 'none';
        });
    });

    // 登录/注册切换
    document.getElementById('switch-to-register').addEventListener('click', function(e) {
        e.preventDefault();
        loginModal.style.display = 'none';
        registerModal.style.display = 'block';
    });
    document.getElementById('switch-to-login').addEventListener('click', function(e) {
        e.preventDefault();
        registerModal.style.display = 'none';
        loginModal.style.display = 'block';
    });

    // 登录按钮
    document.getElementById('login-btn').addEventListener('click', function() {
        loginModal.style.display = 'block';
    });

    // 退出按钮
    document.getElementById('logout-btn').addEventListener('click', function() {
        localStorage.removeItem('user');
        updateLoginStatus();
        updateSectionLoginHints();
        showToast('已退出登录', 'success');
    });

    // 登录表单
    document.getElementById('login-form').addEventListener('submit', handleLogin);

    // 注册表单
    document.getElementById('register-form').addEventListener('submit', handleRegister);

    // 发布商品表单
    document.getElementById('publish-form').addEventListener('submit', handlePublish);

    // 搜索
    document.getElementById('search-btn').addEventListener('click', handleSearch);
    document.getElementById('search-input').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') handleSearch();
    });

    // 分类点击
    document.querySelectorAll('.category-item').forEach(item => {
        item.addEventListener('click', function() {
            document.querySelectorAll('.category-item').forEach(i => i.classList.remove('active'));
            this.classList.add('active');
            const categoryId = parseInt(this.dataset.categoryId);
            if (categoryId === 0) {
                loadProducts();
            } else {
                loadProductsByCategory(categoryId);
            }
        });
    });

    // 导航平滑滚动
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const sectionId = this.dataset.section;
            const target = document.getElementById(sectionId);
            if (target) {
                target.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }
            // 更新active状态
            document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
            this.classList.add('active');
        });
    });

    // 星级评分
    document.querySelectorAll('#star-rating .star').forEach(star => {
        star.addEventListener('click', function() {
            const value = parseInt(this.dataset.value);
            document.getElementById('review-rating').value = value;
            document.querySelectorAll('#star-rating .star').forEach((s, i) => {
                s.classList.toggle('active', i < value);
            });
        });
        star.addEventListener('mouseenter', function() {
            const value = parseInt(this.dataset.value);
            document.querySelectorAll('#star-rating .star').forEach((s, i) => {
                s.classList.toggle('hover', i < value);
            });
        });
    });
    document.getElementById('star-rating').addEventListener('mouseleave', function() {
        const value = parseInt(document.getElementById('review-rating').value);
        document.querySelectorAll('#star-rating .star').forEach((s, i) => {
            s.classList.remove('hover');
            s.classList.toggle('active', i < value);
        });
    });

    // 评价表单
    document.getElementById('review-form').addEventListener('submit', handleReview);

    // 未登录提示中的登录链接
    ['publish-login-link', 'orders-login-link', 'profile-login-link'].forEach(id => {
        const el = document.getElementById(id);
        if (el) {
            el.addEventListener('click', function(e) {
                e.preventDefault();
                loginModal.style.display = 'block';
            });
        }
    });

    // 滚动时更新导航高亮
    window.addEventListener('scroll', updateNavHighlight);
}

// ==================== 导航高亮 ====================
function updateNavHighlight() {
    const sections = ['banner', 'products', 'publish', 'orders', 'profile'];
    let current = 'banner';
    for (const id of sections) {
        const el = document.getElementById(id);
        if (el && el.getBoundingClientRect().top <= 120) {
            current = id;
        }
    }
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.toggle('active', link.dataset.section === current);
    });
}

// ==================== 登录/注册 ====================
async function handleLogin(e) {
    e.preventDefault();
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;

    if (!username || !password) {
        showToast('请填写用户名和密码', 'warning');
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/user/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        const data = await res.json();

        if (data.code === 200) {
            localStorage.setItem('user', JSON.stringify(data.user));
            closeAllModals();
            updateLoginStatus();
            updateSectionLoginHints();
            showToast(`欢迎回来，${data.user.username}！`, 'success');
            document.getElementById('login-form').reset();
            // 刷新订单列表
            loadMyOrders();
        } else {
            showToast(data.message || '登录失败', 'error');
        }
    } catch (error) {
        console.error('登录失败:', error);
        showToast('网络错误，请稍后重试', 'error');
    }
}

async function handleRegister(e) {
    e.preventDefault();
    const username = document.getElementById('reg-username').value.trim();
    const password = document.getElementById('reg-password').value;
    const phone = document.getElementById('reg-phone').value.trim();
    const role = document.getElementById('reg-role').value;

    if (!username) { showToast('请输入用户名', 'warning'); return; }
    if (!password || password.length < 6) { showToast('密码长度至少6位', 'warning'); return; }
    if (!/^1[3-9]\d{9}$/.test(phone)) { showToast('请输入正确的手机号', 'warning'); return; }

    try {
        const res = await fetch(`${API_BASE}/user/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password, phone, role })
        });
        const data = await res.json();

        if (data.code === 200) {
            closeAllModals();
            showToast('注册成功！请登录', 'success');
            document.getElementById('register-form').reset();
            document.getElementById('login-modal').style.display = 'block';
        } else {
            showToast(data.message || '注册失败', 'error');
        }
    } catch (error) {
        console.error('注册失败:', error);
        showToast('网络错误，请稍后重试', 'error');
    }
}

// ==================== 登录状态管理 ====================
function updateLoginStatus() {
    const user = getUser();
    const loginBtn = document.getElementById('login-btn');
    const logoutBtn = document.getElementById('logout-btn');
    const navUsername = document.getElementById('nav-username');

    if (user) {
        loginBtn.style.display = 'none';
        logoutBtn.style.display = 'inline-block';
        navUsername.style.display = 'inline';
        navUsername.textContent = `👤 ${user.username}`;
    } else {
        loginBtn.style.display = 'inline-block';
        logoutBtn.style.display = 'none';
        navUsername.style.display = 'none';
    }
}

function updateSectionLoginHints() {
    const loggedIn = isLoggedIn();
    ['publish-login-hint', 'orders-login-hint', 'profile-login-hint'].forEach(id => {
        const el = document.getElementById(id);
        if (el) el.style.display = loggedIn ? 'none' : 'block';
    });
    // 控制表单和内容的显示
    const publishForm = document.getElementById('publish-form');
    if (publishForm) publishForm.style.display = loggedIn ? 'block' : 'none';
}

// ==================== 商品列表 ====================
async function loadProducts() {
    const listEl = document.getElementById('product-list');
    const countEl = document.getElementById('product-count');

    listEl.innerHTML = '<div class="loading-spinner"><div class="spinner"></div><p>加载中...</p></div>';
    countEl.textContent = '';

    try {
        const res = await fetch(`${API_BASE}/product/search?keyword=`);
        const data = await res.json();

        if (data.code === 200 && data.products) {
            renderProductList(data.products);
            countEl.textContent = `共 ${data.products.length} 件商品`;
        } else {
            listEl.innerHTML = '<div class="empty-state"><span class="empty-icon">📭</span><p>暂无商品</p></div>';
        }
    } catch (error) {
        console.error('加载商品失败:', error);
        listEl.innerHTML = '<div class="empty-state"><span class="empty-icon">⚠️</span><p>加载失败，请检查后端是否启动</p></div>';
    }
}

async function loadProductsByCategory(categoryId) {
    const listEl = document.getElementById('product-list');
    const countEl = document.getElementById('product-count');

    listEl.innerHTML = '<div class="loading-spinner"><div class="spinner"></div><p>加载中...</p></div>';
    countEl.textContent = '';

    try {
        const res = await fetch(`${API_BASE}/product/category/${categoryId}`);
        const data = await res.json();

        if (data.code === 200 && data.products) {
            renderProductList(data.products);
            countEl.textContent = `共 ${data.products.length} 件商品`;
        } else {
            listEl.innerHTML = '<div class="empty-state"><span class="empty-icon">📭</span><p>该分类暂无商品</p></div>';
        }
    } catch (error) {
        console.error('加载分类商品失败:', error);
        listEl.innerHTML = '<div class="empty-state"><span class="empty-icon">⚠️</span><p>加载失败</p></div>';
    }
}

// 根据商品信息获取对应的图片路径
function getProductImage(product) {
    // 1. 优先使用商品ID精确匹配
    if (PRODUCT_IMAGE_MAP[product.productId]) {
        return PRODUCT_IMAGE_MAP[product.productId];
    }
    
    // 2. 根据商品名称关键词匹配
    const productName = product.productName.toLowerCase();
    for (const [keyword, imagePath] of Object.entries(PRODUCT_NAME_TO_IMAGE)) {
        if (productName.includes(keyword.toLowerCase())) {
            return imagePath;
        }
    }

    // 3. 根据分类匹配
    if (CATEGORY_IMAGES[product.categoryId]) {
        return CATEGORY_IMAGES[product.categoryId];
    }

    // 4. 默认图片
    return 'images/others.jpg';
}

function renderProductList(products) {
    const listEl = document.getElementById('product-list');

    if (!products || products.length === 0) {
        listEl.innerHTML = '<div class="empty-state"><span class="empty-icon">📭</span><p>暂无商品</p></div>';
        return;
    }

    listEl.innerHTML = products.map(product => {
        const imagePath = getProductImage(product);
        const statusText = PRODUCT_STATUS_MAP[product.status] || product.status;
        const statusClass = product.status === 'approved' ? 'product-status-active' : 'product-status-pending';

        return `
            <div class="product-item" onclick="showProductDetail(${product.productId})">
                <div class="product-image">
                    <img src="${imagePath}" alt="${product.productName}" onerror="this.src='images/others.jpg'">
                </div>
                <div class="product-info">
                    <div class="product-name" title="${product.productName}">${product.productName}</div>
                    <div class="product-price">¥${parseFloat(product.price).toFixed(2)}</div>
                    <div class="product-meta">
                        <span class="product-stock">库存: ${product.stock}</span>
                        <span class="product-status ${statusClass}">${statusText}</span>
                    </div>
                    <div class="product-description">${product.description || '暂无描述'}</div>
                    <div class="product-actions">
                        <button class="btn btn-detail" onclick="event.stopPropagation(); showProductDetail(${product.productId})">查看详情</button>
                        ${product.status === 'approved' && product.stock > 0 ?
                            `<button class="btn btn-primary" onclick="event.stopPropagation(); showPurchaseModal(${product.productId})">购买</button>` :
                            `<button class="btn btn-disabled" disabled>暂不可购</button>`
                        }
                    </div>
                </div>
            </div>
        `;
    }).join('');
}

// ==================== 搜索 ====================
async function handleSearch() {
    const keyword = document.getElementById('search-input').value.trim();
    if (!keyword) {
        showToast('请输入搜索关键词', 'warning');
        return;
    }

    // 重置分类选中状态
    document.querySelectorAll('.category-item').forEach(i => i.classList.remove('active'));
    document.querySelector('.category-item[data-category-id="0"]').classList.add('active');

    const listEl = document.getElementById('product-list');
    const countEl = document.getElementById('product-count');
    listEl.innerHTML = '<div class="loading-spinner"><div class="spinner"></div><p>搜索中...</p></div>';
    countEl.textContent = '';

    try {
        const res = await fetch(`${API_BASE}/product/search?keyword=${encodeURIComponent(keyword)}`);
        const data = await res.json();

        if (data.code === 200 && data.products) {
            renderProductList(data.products);
            countEl.textContent = `搜索"${keyword}"，共 ${data.products.length} 件商品`;
            // 滚动到商品列表
            document.getElementById('products').scrollIntoView({ behavior: 'smooth' });
            if (data.products.length === 0) {
                showToast('未找到相关商品', 'info');
            }
        }
    } catch (error) {
        console.error('搜索失败:', error);
        showToast('搜索失败，请稍后重试', 'error');
    }
}

// ==================== 商品详情 ====================
async function showProductDetail(productId) {
    const detailModal = document.getElementById('detail-modal');
    const detailBody = document.getElementById('detail-body');

    detailBody.innerHTML = '<div class="loading-spinner"><div class="spinner"></div><p>加载中...</p></div>';
    detailModal.style.display = 'block';

    try {
        const res = await fetch(`${API_BASE}/product/detail/${productId}`);
        const data = await res.json();

        if (data.code === 200 && data.product) {
            const p = data.product;
            const icon = CATEGORY_ICONS[p.categoryId] || '📦';
            const statusText = PRODUCT_STATUS_MAP[p.status] || p.status;

            detailBody.innerHTML = `
                <div class="detail-layout">
                    <div class="detail-image">${icon}</div>
                    <div class="detail-info">
                        <h3 class="detail-title">${p.productName}</h3>
                        <div class="detail-price">¥${parseFloat(p.price).toFixed(2)}</div>
                        <div class="detail-meta">
                            <span>状态: ${statusText}</span>
                            <span>库存: ${p.stock}</span>
                            <span>浏览: ${p.views || 0}</span>
                        </div>
                        <div class="detail-desc">
                            <h4>商品描述</h4>
                            <p>${p.description || '暂无描述'}</p>
                        </div>
                        <div class="detail-actions">
                            ${p.status === 'approved' && p.stock > 0 ?
                                `<button class="btn btn-primary btn-lg" onclick="closeAllModals(); showPurchaseModal(${p.productId})">🛒 立即购买</button>` :
                                `<button class="btn btn-disabled btn-lg" disabled>暂不可购买</button>`
                            }
                            <button class="btn btn-secondary btn-lg" onclick="closeAllModals()">返回</button>
                        </div>
                    </div>
                </div>
            `;
        } else {
            detailBody.innerHTML = '<div class="empty-state"><span class="empty-icon">⚠️</span><p>商品不存在</p></div>';
        }
    } catch (error) {
        console.error('加载详情失败:', error);
        detailBody.innerHTML = '<div class="empty-state"><span class="empty-icon">⚠️</span><p>加载失败</p></div>';
    }
}

// ==================== 购买流程 ====================
async function showPurchaseModal(productId) {
    if (!requireLogin()) return;

    const purchaseModal = document.getElementById('purchase-modal');
    const purchaseBody = document.getElementById('purchase-body');

    purchaseBody.innerHTML = '<div class="loading-spinner"><div class="spinner"></div><p>加载中...</p></div>';
    purchaseModal.style.display = 'block';

    try {
        const res = await fetch(`${API_BASE}/product/detail/${productId}`);
        const data = await res.json();

        if (data.code === 200 && data.product) {
            const p = data.product;
            const user = getUser();

            purchaseBody.innerHTML = `
                <div class="purchase-info">
                    <div class="purchase-product">
                        <span class="purchase-icon">${CATEGORY_ICONS[p.categoryId] || '📦'}</span>
                        <div>
                            <div class="purchase-name">${p.productName}</div>
                            <div class="purchase-price">¥${parseFloat(p.price).toFixed(2)}</div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="purchase-quantity">购买数量</label>
                        <div class="quantity-control">
                            <button class="btn btn-qty" id="qty-minus" onclick="changeQty(-1)">−</button>
                            <input type="number" id="purchase-quantity" value="1" min="1" max="${p.stock}" readonly>
                            <button class="btn btn-qty" id="qty-plus" onclick="changeQty(1)">+</button>
                            <span class="stock-hint">库存 ${p.stock} 件</span>
                        </div>
                    </div>
                    <div class="purchase-total">
                        <span>合计：</span>
                        <span id="purchase-total-price" class="total-price">¥${parseFloat(p.price).toFixed(2)}</span>
                    </div>
                    <div class="purchase-actions">
                        <button class="btn btn-primary btn-block" onclick="confirmPurchase(${p.productId}, ${p.sellerId})">确认下单</button>
                        <button class="btn btn-secondary btn-block" onclick="closeAllModals()">取消</button>
                    </div>
                </div>
            `;

            // 存储单价用于计算
            window._currentProductPrice = parseFloat(p.price);
            window._currentProductStock = p.stock;
            window._currentProductId = p.productId;
            window._currentSellerId = p.sellerId;
        }
    } catch (error) {
        console.error('加载商品信息失败:', error);
        showToast('加载失败', 'error');
        closeAllModals();
    }
}

function changeQty(delta) {
    const input = document.getElementById('purchase-quantity');
    let qty = parseInt(input.value) + delta;
    qty = Math.max(1, Math.min(qty, window._currentProductStock));
    input.value = qty;
    const total = (qty * window._currentProductPrice).toFixed(2);
    document.getElementById('purchase-total-price').textContent = `¥${total}`;
}

async function confirmPurchase(productId, sellerId) {
    const user = getUser();
    if (!user) return;

    const quantity = parseInt(document.getElementById('purchase-quantity').value);
    const totalPrice = (quantity * window._currentProductPrice).toFixed(2);

    try {
        const res = await fetch(`${API_BASE}/order/create`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                buyerId: user.userId,
                sellerId: sellerId,
                productId: productId,
                quantity: quantity,
                totalPrice: parseFloat(totalPrice),
                orderStatus: 'pending',
                paymentStatus: 'unpaid'
            })
        });
        const data = await res.json();

        if (data.code === 200) {
            closeAllModals();
            showToast('下单成功！请尽快完成付款', 'success');
            loadProducts(); // 刷新商品列表
            loadMyOrders(); // 刷新订单列表
        } else {
            showToast(data.message || '下单失败', 'error');
        }
    } catch (error) {
        console.error('下单失败:', error);
        showToast('网络错误，请稍后重试', 'error');
    }
}

// ==================== 发布商品 ====================
async function handlePublish(e) {
    e.preventDefault();
    if (!requireLogin()) return;

    const user = getUser();
    const productName = document.getElementById('product-name').value.trim();
    const categoryId = parseInt(document.getElementById('product-category').value);
    const price = parseFloat(document.getElementById('product-price').value);
    const stock = parseInt(document.getElementById('product-stock').value);
    const description = document.getElementById('product-description').value.trim();

    if (!productName) { showToast('请输入商品名称', 'warning'); return; }
    if (isNaN(price) || price <= 0) { showToast('请输入有效价格', 'warning'); return; }
    if (isNaN(stock) || stock < 1) { showToast('请输入有效库存', 'warning'); return; }
    if (!description) { showToast('请输入商品描述', 'warning'); return; }

    try {
        const res = await fetch(`${API_BASE}/product/publish`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                sellerId: user.userId,
                categoryId, productName, description, price, stock
            })
        });
        const data = await res.json();

        if (data.code === 200) {
            showToast('发布成功，等待审核！', 'success');
            document.getElementById('publish-form').reset();
            loadProducts();
        } else {
            showToast(data.message || '发布失败', 'error');
        }
    } catch (error) {
        console.error('发布失败:', error);
        showToast('网络错误，请稍后重试', 'error');
    }
}

// ==================== 我的订单 ====================
async function loadMyOrders() {
    const user = getUser();
    const listEl = document.getElementById('order-list');

    if (!user) {
        listEl.innerHTML = '';
        return;
    }

    listEl.innerHTML = '<div class="loading-spinner"><div class="spinner"></div><p>加载订单中...</p></div>';

    try {
        // 同时加载买家和卖家订单
        const [buyerRes, sellerRes] = await Promise.all([
            fetch(`${API_BASE}/order/buyer/${user.userId}`),
            fetch(`${API_BASE}/order/seller/${user.userId}`)
        ]);
        const buyerData = await buyerRes.json();
        const sellerData = await sellerRes.json();

        const buyerOrders = (buyerData.code === 200 && buyerData.orders) ? buyerData.orders : [];
        const sellerOrders = (sellerData.code === 200 && sellerData.orders) ? sellerData.orders : [];

        // 合并去重
        const orderMap = new Map();
        [...buyerOrders, ...sellerOrders].forEach(order => {
            if (!orderMap.has(order.orderId)) {
                orderMap.set(order.orderId, order);
            }
        });
        const allOrders = Array.from(orderMap.values());

        if (allOrders.length === 0) {
            listEl.innerHTML = '<div class="empty-state"><span class="empty-icon">📋</span><p>暂无订单</p></div>';
            return;
        }

        listEl.innerHTML = allOrders.map(order => {
            const status = ORDER_STATUS_MAP[order.orderStatus] || { text: order.orderStatus, class: '' };
            const isBuyer = order.buyerId === user.userId;
            const roleLabel = isBuyer ? '【买家】' : '【卖家】';

            let actionsHtml = '';
            if (isBuyer) {
                if (order.orderStatus === 'pending') {
                    actionsHtml += `<button class="btn btn-sm btn-primary" onclick="payOrder(${order.orderId})">付款</button>`;
                    actionsHtml += `<button class="btn btn-sm btn-danger" onclick="cancelOrder(${order.orderId})">取消</button>`;
                }
                if (order.orderStatus === 'shipped') {
                    actionsHtml += `<button class="btn btn-sm btn-primary" onclick="confirmReceive(${order.orderId})">确认收货</button>`;
                }
                if (order.orderStatus === 'completed') {
                    actionsHtml += `<button class="btn btn-sm btn-warning" onclick="openReviewModal(${order.orderId}, ${order.sellerId})">评价</button>`;
                }
            } else {
                if (order.orderStatus === 'paid') {
                    actionsHtml += `<button class="btn btn-sm btn-primary" onclick="shipOrder(${order.orderId})">发货</button>`;
                }
            }

            return `
                <div class="order-item">
                    <div class="order-header">
                        <div>
                            <span class="order-id">${roleLabel} 订单号: ${order.orderId}</span>
                            <span class="order-time">${order.createdAt || ''}</span>
                        </div>
                        <span class="order-status ${status.class}">${status.text}</span>
                    </div>
                    <div class="order-body">
                        <div class="order-product-info">
                            <span>商品ID: ${order.productId}</span>
                            <span>数量: ${order.quantity || 1}</span>
                            <span class="order-total">¥${parseFloat(order.totalPrice || 0).toFixed(2)}</span>
                        </div>
                        ${actionsHtml ? `<div class="order-actions">${actionsHtml}</div>` : ''}
                    </div>
                </div>
            `;
        }).join('');

    } catch (error) {
        console.error('加载订单失败:', error);
        listEl.innerHTML = '<div class="empty-state"><span class="empty-icon">⚠️</span><p>加载订单失败</p></div>';
    }
}

// ==================== 订单操作 ====================
async function payOrder(orderId) {
    try {
        const res = await fetch(`${API_BASE}/order/payment`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ orderId, paymentStatus: 'paid' })
        });
        const data = await res.json();

        if (data.code === 200) {
            showToast('付款成功！', 'success');
            loadMyOrders();
        } else {
            showToast(data.message || '付款失败', 'error');
        }
    } catch (error) {
        showToast('网络错误', 'error');
    }
}

async function cancelOrder(orderId) {
    try {
        const res = await fetch(`${API_BASE}/order/cancel/${orderId}`, { method: 'PUT' });
        const data = await res.json();

        if (data.code === 200) {
            showToast('订单已取消', 'success');
            loadMyOrders();
            loadProducts();
        } else {
            showToast(data.message || '取消失败', 'error');
        }
    } catch (error) {
        showToast('网络错误', 'error');
    }
}

async function confirmReceive(orderId) {
    try {
        const res = await fetch(`${API_BASE}/order/status`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ orderId, orderStatus: 'completed' })
        });
        const data = await res.json();

        if (data.code === 200) {
            showToast('已确认收货', 'success');
            loadMyOrders();
        } else {
            showToast(data.message || '操作失败', 'error');
        }
    } catch (error) {
        showToast('网络错误', 'error');
    }
}

async function shipOrder(orderId) {
    try {
        const res = await fetch(`${API_BASE}/order/status`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ orderId, orderStatus: 'shipped' })
        });
        const data = await res.json();

        if (data.code === 200) {
            showToast('发货成功！', 'success');
            loadMyOrders();
        } else {
            showToast(data.message || '发货失败', 'error');
        }
    } catch (error) {
        showToast('网络错误', 'error');
    }
}

// ==================== 评价 ====================
function openReviewModal(orderId, sellerId) {
    document.getElementById('review-order-id').value = orderId;
    document.getElementById('review-seller-id').value = sellerId;
    document.getElementById('review-rating').value = 5;
    document.getElementById('review-content').value = '';
    document.querySelectorAll('#star-rating .star').forEach((s, i) => {
        s.classList.toggle('active', i < 5);
    });
    document.getElementById('review-modal').style.display = 'block';
}

async function handleReview(e) {
    e.preventDefault();
    const user = getUser();
    if (!user) return;

    const orderId = parseInt(document.getElementById('review-order-id').value);
    const sellerId = parseInt(document.getElementById('review-seller-id').value);
    const rating = parseInt(document.getElementById('review-rating').value);
    const content = document.getElementById('review-content').value.trim();

    if (!content) { showToast('请输入评价内容', 'warning'); return; }

    try {
        const res = await fetch(`${API_BASE}/review/add`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                orderId, reviewerId: user.userId, reviewedId: sellerId,
                rating, comment: content
            })
        });
        const data = await res.json();

        if (data.code === 200) {
            closeAllModals();
            showToast('评价成功！感谢您的反馈', 'success');
        } else {
            showToast(data.message || '评价失败', 'error');
        }
    } catch (error) {
        showToast('网络错误', 'error');
    }
}

// ==================== 个人中心 ====================
async function loadUserProfile() {
    const user = getUser();
    if (!user) return;

    const profileInfo = document.getElementById('profile-info');
    profileInfo.innerHTML = '<div class="loading-spinner"><div class="spinner"></div><p>加载中...</p></div>';

    try {
        const res = await fetch(`${API_BASE}/user/info/${user.userId}`);
        const data = await res.json();

        if (data.code === 200 && data.user) {
            const u = data.user;
            const roleMap = { buyer: '买家', seller: '卖家', auditor: '审核员' };
            profileInfo.innerHTML = `
                <div class="profile-card">
                    <div class="profile-avatar">👤</div>
                    <div class="profile-details">
                        <div class="profile-item">
                            <span class="profile-label">用户名</span>
                            <span class="profile-value">${u.username}</span>
                        </div>
                        <div class="profile-item">
                            <span class="profile-label">真实姓名</span>
                            <span class="profile-value">${u.realName || '未设置'}</span>
                        </div>
                        <div class="profile-item">
                            <span class="profile-label">手机号</span>
                            <span class="profile-value">${u.phone || '未设置'}</span>
                        </div>
                        <div class="profile-item">
                            <span class="profile-label">邮箱</span>
                            <span class="profile-value">${u.email || '未设置'}</span>
                        </div>
                        <div class="profile-item">
                            <span class="profile-label">角色</span>
                            <span class="profile-value">${roleMap[u.role] || u.role}</span>
                        </div>
                        <div class="profile-item">
                            <span class="profile-label">信用分数</span>
                            <span class="profile-value credit-score">${u.creditScore || 100}</span>
                        </div>
                    </div>
                </div>
            `;
        }
    } catch (error) {
        console.error('加载个人信息失败:', error);
        profileInfo.innerHTML = '<div class="empty-state"><span class="empty-icon">⚠️</span><p>加载失败</p></div>';
    }
}

// ==================== 页面滚动监听 - 自动加载 ====================
// 当滚动到订单或个人中心区域时自动加载数据
const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            const id = entry.target.id;
            if (id === 'orders' && isLoggedIn()) {
                loadMyOrders();
            }
            if (id === 'profile' && isLoggedIn()) {
                loadUserProfile();
            }
        }
    });
}, { threshold: 0.2 });

// 观察订单和个人中心区域
document.addEventListener('DOMContentLoaded', () => {
    const ordersSection = document.getElementById('orders');
    const profileSection = document.getElementById('profile');
    if (ordersSection) observer.observe(ordersSection);
    if (profileSection) observer.observe(profileSection);
});
