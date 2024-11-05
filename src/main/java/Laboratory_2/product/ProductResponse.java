package Laboratory_2.product;

import Laboratory_2.product_specification.ProductSpecificationDTO;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class ProductResponse {
    private int id;
    private String title;
    private String link;
    private String oldPrice;
    private String newPrice;
    private List<ProductSpecificationDTO> specifications;

    public static ProductResponse fromEntity(ProductEntity productEntity, List<ProductSpecificationDTO> specificationDTOs) {
        return ProductResponse.builder()
                .id(productEntity.getId())
                .title(productEntity.getTitle())
                .link(productEntity.getLink())
                .oldPrice(productEntity.getOldPrice())
                .newPrice(productEntity.getNewPrice())
                .specifications(specificationDTOs)
                .build();
    }

    @Override
    public String toString() {
        return "ProductResponse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", oldPrice='" + oldPrice + '\'' +
                ", newPrice='" + newPrice + '\'' +
                ", specifications=" + specifications +
                '}';
    }

}
