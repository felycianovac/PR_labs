package Laboratory_2.product;


import Laboratory_2.product_specification.ProductSpecification;
import Laboratory_2.product_specification.ProductSpecificationDTO;
import Laboratory_2.product_specification.ProductSpecificationRepository;
import Laboratory_2.specification.SpecificationRepository;
import Laboratory_2.specification.Specification;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
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
    public ProductDTO createProduct(ProductRequest productDTO) {
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

        System.out.println("ProductResponse.fromEntity(savedProduct, specificationDTOs): " + ProductDTO.fromEntity(savedProduct, specificationDTOs));
        return ProductDTO.fromEntity(savedProduct, specificationDTOs);
    }


    public ProductDTO getProductById(int id) {
        ProductEntity productEntity = productRepository.findById(id).orElse(null);
        if (productEntity == null) {
            return null;
        }
        List<ProductSpecificationDTO> specificationDTOs = productSpecificationRepository.findByProductId(id).stream()
                .map(ProductSpecificationDTO::from)
                .collect(Collectors.toList());

        return ProductDTO.fromEntity(productEntity, specificationDTOs);
    }

    public Page<ProductDTO> getAllProducts(int offset, int limit) {
        Page<ProductEntity> productEntities = productRepository.findAll(PageRequest.of(offset, limit));

        return productEntities.map(entity -> ProductDTO.fromEntity(entity, productSpecificationRepository.findByProductId(entity.getId()).stream()
                .map(ProductSpecificationDTO::from)
                .collect(Collectors.toList())));
    }

    @Transactional
    public ProductDTO updateProduct(int id, ProductRequest productRequest) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productEntity.setTitle(productRequest.getTitle());
        productEntity.setLink(productRequest.getLink());
        productEntity.setOldPrice(productRequest.getOldPrice());
        productEntity.setNewPrice(productRequest.getNewPrice());

        ProductEntity updatedProduct = productRepository.save(productEntity);

        List<ProductSpecificationDTO> specificationDTOs = productSpecificationRepository.findByProductId(id).stream()
                .map(ProductSpecificationDTO::from)
                .collect(Collectors.toList());

        return ProductDTO.fromEntity(updatedProduct, specificationDTOs);
    }

    @Transactional
    public String deleteProduct(int id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productSpecificationRepository.deleteByProductId(id);

        productRepository.delete(product);

        return "Product and related specifications deleted successfully.";
    }


    public ProductDTO saveProductFromJsonFile(String filePath) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            ProductRequest productRequest = mapper.readValue(new File(filePath), ProductRequest.class);

            ProductEntity productEntity = new ProductEntity();
            productEntity.setTitle(productRequest.getTitle());
            productEntity.setLink(productRequest.getLink());
            productEntity.setOldPrice(productRequest.getOldPrice());
            productEntity.setNewPrice(productRequest.getNewPrice());

            ProductEntity savedProduct = productRepository.save(productEntity);

            List<ProductSpecification> productSpecifications = productRequest.getSpecifications().entrySet().stream()
                    .map(entry -> {
                        Specification specification = specificationRepository.findByType(entry.getKey())
                                .orElseGet(() -> {
                                    Specification newSpec = new Specification();
                                    newSpec.setType(entry.getKey());
                                    return specificationRepository.save(newSpec);
                                });

                        return ProductSpecification.builder()
                                .product(savedProduct)
                                .specification(specification)
                                .value(entry.getValue())
                                .build();
                    })
                    .collect(Collectors.toList());

            productSpecificationRepository.saveAll(productSpecifications);

            List<ProductSpecificationDTO> specificationDTOs = productSpecifications.stream()
                    .map(ProductSpecificationDTO::from)
                    .collect(Collectors.toList());

            return ProductDTO.fromEntity(savedProduct, specificationDTOs);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read JSON file: " + filePath);
        }
    }





}
