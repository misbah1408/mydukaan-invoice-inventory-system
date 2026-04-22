package com.mydukaan.service;

import com.mydukaan.dto.common.ApiResponse;
import com.mydukaan.dto.request.ProductRequest;
import com.mydukaan.dto.response.ProductResponse;
import com.mydukaan.exception.ResourceNotFoundException;
import com.mydukaan.model.Product;
import com.mydukaan.model.Store;
import com.mydukaan.repository.ProductRepository;
import com.mydukaan.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    public ProductService(ProductRepository productRepository, StoreRepository storeRepository) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
    }

    // ✅ CREATE SINGLE PRODUCT
    public ApiResponse<ProductResponse> addProduct(ProductRequest request) {

        Store store = getStore(request.getStoreId());

        Product product = buildProduct(request, store);

        Product saved = productRepository.save(product);

        return new ApiResponse<>(true, "Product added successfully", mapToResponse(saved));
    }

    // ✅ GET PRODUCTS BY STORE
    @Transactional(readOnly = true)
    public ApiResponse<List<ProductResponse>> getProducts(Long storeId) {

        List<Product> products = productRepository.findByStoreId(storeId);

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found for this store");
        }

        List<ProductResponse> response = products.stream()
                .map(this::mapToResponse)
                .toList();

        return new ApiResponse<>(true, "Products fetched successfully", response);
    }

    // ✅ UPDATE PRODUCT (PARTIAL UPDATE)
    public ApiResponse<ProductResponse> updateProduct(ProductRequest request, Long productId) {

        Product product = getProduct(productId);

        updateFields(product, request);

        Product updated = productRepository.save(product);

        return new ApiResponse<>(true, "Product updated successfully", mapToResponse(updated));
    }

    // ✅ DELETE PRODUCT WITH AUTH CHECK
    public ApiResponse<Void> deleteProduct(Long storeId, Long productId) {

        Product product = getProduct(productId);

        validateStoreOwnership(product, storeId);

        productRepository.delete(product);

        return new ApiResponse<>(true, "Product deleted successfully", null);
    }

    // ✅ BULK INSERT (OPTIMIZED)
    public ApiResponse<Void> addMultipleProducts(List<ProductRequest> requests, Long storeId) {

        Store store = getStore(storeId);

        List<Product> products = requests.stream()
                .map(req -> buildProduct(req, store))
                .toList();

        productRepository.saveAll(products);

        return new ApiResponse<>(true, "All products added successfully", null);
    }

    public ApiResponse searchProduct(Long storeId, String keyword){
        List<Product> products = productRepository.searchProductsByStoreIdAndNameOrCategory(storeId, keyword);
        List<ProductResponse> responses = products.stream().map(this::mapToResponse).toList();
        return new ApiResponse(true, "Success", responses);
    }

    // =========================
    // 🔧 HELPER METHODS
    // =========================

    private Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    private Store getStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
    }

    private void validateStoreOwnership(Product product, Long storeId) {
        if (!Objects.equals(product.getStore().getId(), storeId)) {
            throw new IllegalArgumentException("Unauthorized access to this product");
        }
    }

    private Product buildProduct(ProductRequest request, Store store) {
        Product product = new Product();
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setThreshold(request.getThreshold());
        product.setStore(store);
        return product;
    }

    private void updateFields(Product product, ProductRequest request) {

        if (request.getName() != null) {
            product.setName(request.getName());
        }

        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }

        if (request.getStock() != null) {
            product.setStock(request.getStock());
        }

        if (request.getCategory() != null) {
            product.setCategory(request.getCategory());
        }

        if (request.getThreshold() != null) {
            product.setThreshold(request.getThreshold());
        }

        if (request.getStoreId() != null) {
            Store store = getStore(request.getStoreId());
            product.setStore(store);
        }
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .stock(product.getStock())
                .threshold(product.getThreshold())
                .storeId(product.getStore().getId())
                .build();
    }
}