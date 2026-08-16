package com.raghul.productapi.service;

import com.raghul.productapi.entity.Product;
import com.raghul.productapi.exception.DuplicateSkuException;
import com.raghul.productapi.exception.ProductNotFoundException;
import com.raghul.productapi.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product createProduct(Product product) {
        if (productRepository.existsBySkuIgnoreCase(product.getSku())) {
            throw new DuplicateSkuException(product.getSku());
        }
        product.setId(null);
        product.setSku(product.getSku().trim().toUpperCase());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product input) {
        Product existing = getProductById(id);
        if (productRepository.existsBySkuIgnoreCaseAndIdNot(input.getSku(), id)) {
            throw new DuplicateSkuException(input.getSku());
        }
        existing.setName(input.getName());
        existing.setSku(input.getSku().trim().toUpperCase());
        existing.setPrice(input.getPrice());
        existing.setQuantity(input.getQuantity());
        return productRepository.save(existing);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
