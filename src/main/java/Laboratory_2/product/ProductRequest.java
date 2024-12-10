package Laboratory_2.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;


@Getter
@Setter
@Data
@Builder

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


    @JsonCreator
    public ProductRequest(
            @JsonProperty("title") String title,
            @JsonProperty("link") String link,
            @JsonProperty("oldPrice") String oldPrice,
            @JsonProperty("newPrice") String newPrice,
            @JsonProperty("specifications") Map<String, String> specifications) {
        this.title = title;
        this.link = link;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.specifications = specifications;
    }
}
