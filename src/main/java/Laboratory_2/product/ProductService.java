package Laboratory_2.product;


import Laboratory_2.product_specification.ProductSpecification;
import Laboratory_2.product_specification.ProductSpecificationDTO;
import Laboratory_2.product_specification.ProductSpecificationRepository;
import Laboratory_2.specification.SpecificationRepository;
import Laboratory_2.specification.Specification;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SpecificationRepository specificationRepository;

    @Autowired
    private ProductSpecificationRepository productSpecificationRepository;

    @Transactional
    public ProductResponse createProduct(ProductRequest productDTO) {
        ProductEntity productEntity = ProductEntity.builder()
                .title(productDTO.getTitle())
                .link(productDTO.getLink())
                .oldPrice(productDTO.getOldPrice())
                .newPrice(productDTO.getNewPrice())
                .build();

        ProductEntity savedProduct = productRepository.save(productEntity);

        List<ProductSpecification> productSpecificationList = productDTO.getSpecifications().entrySet().stream()
                .map(entry -> {
                    String specKey = entry.getKey();
                    String specValue = entry.getValue();

                    Specification specification = specificationRepository.findByType(specKey)
                            .orElseGet(() -> {
                                Specification newSpec = new Specification();
                                newSpec.setType(specKey);
                                return specificationRepository.save(newSpec);
                            });

                    return ProductSpecification.builder()
                            .product(savedProduct)
                            .specification(specification)
                            .value(specValue)
                            .build();
                })
                .collect(Collectors.toList());

        productSpecificationRepository.saveAll(productSpecificationList);

        List<ProductSpecificationDTO> specificationDTOs = productSpecificationRepository.findByProductId(savedProduct.getId()).stream()
                .map(ProductSpecificationDTO::from )
                .collect(Collectors.toList());

        System.out.println("ProductResponse.fromEntity(savedProduct, specificationDTOs): " + ProductResponse.fromEntity(savedProduct, specificationDTOs));
        return ProductResponse.fromEntity(savedProduct, specificationDTOs);
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
