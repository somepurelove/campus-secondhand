package com.campus.secondhand.service;

import com.campus.secondhand.entity.Product;
import com.campus.secondhand.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 商品服务单元测试
 * 
 * 测试用例：
 * 1. 发布商品设置初始状态
 * 2. 审核商品
 * 3. 按分类查询商品
 * 4. 搜索商品
 * 5. 增加浏览量
 */
public class ProductServiceTest {
    
    @Mock
    private ProductMapper productMapper;
    
    @InjectMocks
    private ProductService productService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testPublishProduct() {
        Product product = new Product();
        product.setProductName("测试商品");
        product.setPrice(100.0);
        
        when(productMapper.insert(any(Product.class))).thenReturn(1);
        
        boolean result = productService.publishProduct(product);
        
        assertTrue(result);
        assertEquals("pending", product.getStatus());
        assertEquals(0, product.getViews());
    }
    
    @Test
    void testAuditProduct_Approved() {
        Product product = new Product();
        product.setProductId(1);
        
        when(productMapper.updateById(any(Product.class))).thenReturn(1);
        
        boolean result = productService.auditProduct(1, "approved");
        
        assertTrue(result);
    }
    
    @Test
    void testAuditProduct_Rejected() {
        when(productMapper.updateById(any(Product.class))).thenReturn(1);
        
        boolean result = productService.auditProduct(1, "rejected");
        
        assertTrue(result);
    }
    
    @Test
    void testGetProductsByCategory() {
        Product product1 = new Product();
        product1.setProductId(1);
        product1.setCategoryId(1);
        product1.setStatus("approved");
        
        Product product2 = new Product();
        product2.setProductId(2);
        product2.setCategoryId(1);
        product2.setStatus("approved");
        
        when(productMapper.selectList(any())).thenReturn(Arrays.asList(product1, product2));
        
        List<Product> products = productService.getProductsByCategory(1);
        
        assertEquals(2, products.size());
    }
    
    @Test
    void testSearchProducts() {
        Product product = new Product();
        product.setProductId(1);
        product.setProductName("iPhone");
        product.setStatus("approved");
        
        when(productMapper.selectList(any())).thenReturn(Arrays.asList(product));
        
        List<Product> products = productService.searchProducts("iPhone");
        
        assertEquals(1, products.size());
    }
    
    @Test
    void testIncreaseViews() {
        Product product = new Product();
        product.setProductId(1);
        product.setViews(10);
        
        when(productMapper.selectById(1)).thenReturn(product);
        when(productMapper.updateById(any(Product.class))).thenReturn(1);
        
        boolean result = productService.increaseViews(1);
        
        assertTrue(result);
        assertEquals(11, product.getViews());
    }
    
    @Test
    void testIncreaseViews_ProductNotFound() {
        when(productMapper.selectById(999)).thenReturn(null);
        
        boolean result = productService.increaseViews(999);
        
        assertFalse(result);
    }
    
    @Test
    void testGetProductsBySeller() {
        Product product = new Product();
        product.setProductId(1);
        product.setSellerId(1);
        
        when(productMapper.selectList(any())).thenReturn(Arrays.asList(product));
        
        List<Product> products = productService.getProductsBySeller(1);
        
        assertEquals(1, products.size());
    }
}
