package Laboratory_2.product;

import lombok.*;

import java.util.Map;


@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String title;
    private String link;
    private String oldPrice;
    private String newPrice;
    private Map<String, String> specifications;

//    public static ProductDTO fromEntity(ProductEntity productEntity) {
//        Map<String, String> specifications = productEntity.getProductSpecifications().stream()
//                .collect(Collectors.toMap(
//                        spec -> spec.getSpecification().getType(),
//                        ProductSpecification::getValue
//                ));
//
//        return ProductDTO.builder()
//                .title(productEntity.getTitle())
//                .link(productEntity.getLink())
//                .oldPrice(productEntity.getOldPrice())
//                .newPrice(productEntity.getNewPrice())
//                .specifications(specifications)
//                .build();
//    }
}
