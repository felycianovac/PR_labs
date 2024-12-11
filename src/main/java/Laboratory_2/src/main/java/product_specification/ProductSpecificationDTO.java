package product_specification;


import product_specification.ProductSpecification;
import specification.Specification;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductSpecificationDTO {

    private String specification;
    private String value;


    public static ProductSpecificationDTO from(ProductSpecification productSpecification) {
        return ProductSpecificationDTO.builder()
                .specification(productSpecification.getSpecification().getType())
                .value(productSpecification.getValue())
                .build();
    }

}
