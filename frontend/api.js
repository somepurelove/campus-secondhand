// API调用封装
const API_BASE_URL = 'http://localhost:8080/api';

// 通用请求方法
async function request(url, options = {}) {
    try {
        const response = await fetch(`${API_BASE_URL}${url}`, {
            ...options,
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            }
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('API request failed:', error);
        throw error;
    }
}

// 用户相关API
const userApi = {
    // 注册
    register: async (userData) => {
        return await request('/user/register', {
            method: 'POST',
            body: JSON.stringify(userData)
        });
    },
    
    // 登录
    login: async (loginData) => {
        return await request('/user/login', {
            method: 'POST',
            body: JSON.stringify(loginData)
        });
    },
    
    // 获取用户信息
    getUserInfo: async (userId) => {
        return await request(`/user/info/${userId}`);
    },
    
    // 更新用户信息
    updateUser: async (userData) => {
        return await request('/user/update', {
            method: 'PUT',
            body: JSON.stringify(userData)
        });
    }
};

// 商品相关API
const productApi = {
    // 发布商品
    publish: async (productData) => {
        return await request('/product/publish', {
            method: 'POST',
            body: JSON.stringify(productData)
        });
    },
    
    // 审核商品
    audit: async (auditData) => {
        return await request('/product/audit', {
            method: 'PUT',
            body: JSON.stringify(auditData)
        });
    },
    
    // 获取商品详情
    getDetail: async (productId) => {
        return await request(`/product/detail/${productId}`);
    },
    
    // 按分类查询商品
    getByCategory: async (categoryId) => {
        return await request(`/product/category/${categoryId}`);
    },
    
    // 按卖家查询商品
    getBySeller: async (sellerId) => {
        return await request(`/product/seller/${sellerId}`);
    },
    
    // 搜索商品
    search: async (keyword) => {
        return await request(`/product/search?keyword=${encodeURIComponent(keyword)}`);
    },
    
    // 更新商品信息
    update: async (productData) => {
        return await request('/product/update', {
            method: 'PUT',
            body: JSON.stringify(productData)
        });
    },
    
    // 删除商品
    delete: async (productId) => {
        return await request(`/product/delete/${productId}`, {
            method: 'DELETE'
        });
    }
};

// 订单相关API
const orderApi = {
    // 创建订单
    create: async (orderData) => {
        return await request('/order/create', {
            method: 'POST',
            body: JSON.stringify(orderData)
        });
    },
    
    // 更新订单状态
    updateStatus: async (statusData) => {
        return await request('/order/status', {
            method: 'PUT',
            body: JSON.stringify(statusData)
        });
    },
    
    // 更新支付状态
    updatePayment: async (paymentData) => {
        return await request('/order/payment', {
            method: 'PUT',
            body: JSON.stringify(paymentData)
        });
    },
    
    // 获取订单详情
    getDetail: async (orderId) => {
        return await request(`/order/detail/${orderId}`);
    },
    
    // 按买家查询订单
    getByBuyer: async (buyerId) => {
        return await request(`/order/buyer/${buyerId}`);
    },
    
    // 按卖家查询订单
    getBySeller: async (sellerId) => {
        return await request(`/order/seller/${sellerId}`);
    },
    
    // 取消订单
    cancel: async (orderId) => {
        return await request(`/order/cancel/${orderId}`, {
            method: 'PUT'
        });
    }
};

// 评价相关API
const reviewApi = {
    // 添加评价
    add: async (reviewData) => {
        return await request('/review/add', {
            method: 'POST',
            body: JSON.stringify(reviewData)
        });
    },
    
    // 按被评价人查询评价
    getByReviewed: async (reviewedId) => {
        return await request(`/review/reviewed/${reviewedId}`);
    },
    
    // 按订单查询评价
    getByOrder: async (orderId) => {
        return await request(`/review/order/${orderId}`);
    },
    
    // 获取用户平均评分
    getAverageRating: async (userId) => {
        return await request(`/review/rating/${userId}`);
    }
};

// 纠纷相关API
const disputeApi = {
    // 提交纠纷
    submit: async (disputeData) => {
        return await request('/dispute/submit', {
            method: 'POST',
            body: JSON.stringify(disputeData)
        });
    },
    
    // 处理纠纷
    handle: async (handleData) => {
        return await request('/dispute/handle', {
            method: 'PUT',
            body: JSON.stringify(handleData)
        });
    },
    
    // 获取纠纷详情
    getDetail: async (disputeId) => {
        return await request(`/dispute/detail/${disputeId}`);
    },
    
    // 按申请人查询纠纷
    getByApplicant: async (applicantId) => {
        return await request(`/dispute/applicant/${applicantId}`);
    },
    
    // 按状态查询纠纷
    getByStatus: async (status) => {
        return await request(`/dispute/status/${status}`);
    },
    
    // 按订单查询纠纷
    getByOrder: async (orderId) => {
        return await request(`/dispute/order/${orderId}`);
    }
};

// API对象已挂载到全局，可直接使用
// userApi, productApi, orderApi, reviewApi, disputeApi
