package Laboratory_2.product;


import Laboratory_2.product_specification.ProductSpecification;
import Laboratory_2.product_specification.ProductSpecificationRepository;
import Laboratory_2.specification.SpecificationRepository;
import Laboratory_2.specification.Specification;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SpecificationRepository specificationRepository;

    @Autowired
    private ProductSpecificationRepository productSpecificationRepository;

    @Transactional
    public ProductEntity createProduct(ProductDTO productDTO) {
        ProductEntity productEntity = ProductEntity.builder()
                .title(productDTO.getTitle())
                .link(productDTO.getLink())
                .oldPrice(productDTO.getOldPrice())
                .newPrice(productDTO.getNewPrice())
                .build();

        ProductEntity savedProduct = productRepository.save(productEntity);

        Map<String, String> specificationsData = productDTO.getSpecifications();
        specificationsData.forEach((specKey, specValue) -> {
            Specification specification = specificationRepository.findByType(specKey)
                    .orElseGet(() -> {
                        Specification newSpec = Specification.builder()
                                .type(specKey)
                                .build();
                        Specification savedSpec = specificationRepository.save(newSpec);
                        return savedSpec;
                    });

            ProductSpecification productSpec = ProductSpecification.builder()
                    .product(savedProduct)
                    .specification(specification)
                    .value(specValue)
                    .build();
            System.out.println(productSpec);
            productSpecificationRepository.save(productSpec);
        });


        savedProduct.builder()
                .productSpecifications(productSpecificationRepository.findAllByProduct(savedProduct))
                .build();

        return savedProduct;
    }


    public Optional<ProductEntity> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    public Page<ProductEntity> getAllProducts(int offset, int limit) {
        return productRepository.findAll(PageRequest.of(offset, limit));
    }

    public ProductEntity updateProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}
