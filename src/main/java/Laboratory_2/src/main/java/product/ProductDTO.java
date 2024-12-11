package product;

import product_specification.ProductSpecificationDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ProductDTO {
    private int id;
    private String title;
    private String link;
    private String oldPrice;
    private String newPrice;
    private List<ProductSpecificationDTO> specifications;

    public static ProductDTO fromEntity(ProductEntity productEntity, List<ProductSpecificationDTO> specificationDTOs) {
        return ProductDTO.builder()
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
