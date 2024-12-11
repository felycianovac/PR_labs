package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ProcessedProductData {
    private List<Product> filteredProducts;
    private String  totalSum;
    private Instant timestamp;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Processed Product Data ===\n");
        sb.append("Total Price Sum: ").append(totalSum).append(" EUR\n");
        sb.append("Processed at (UTC): ").append(timestamp).append("\n");

        sb.append("\nFiltered Products:\n");
        for (Product product : filteredProducts) {
            sb.append("=== Product Details ===\n");
            sb.append("Title: ").append(product.getTitle()).append("\n");
            sb.append("Link: ").append(product.getLink()).append("\n");
            sb.append("Old Price: ").append(product.getOldPrice().isEmpty() ? "No old price" : product.getOldPrice()).append("\n");
            sb.append("New Price: ").append(product.getNewPrice()).append("\n");

            if (product.getSpecifications() != null && !product.getSpecifications().isEmpty()) {
                sb.append("Specifications:\n");
                product.getSpecifications().forEach((key, value) ->
                        sb.append("  - ").append(key).append(": ").append(value).append("\n")
                );
            } else {
                sb.append("Specifications: Not available\n");
            }
            sb.append("========================\n");
        }

        sb.append("=== End of Processed Data ===\n");
        return sb.toString();
    }
}

