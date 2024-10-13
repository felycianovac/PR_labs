package Laboratory_1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
public class Product {
    private String title;
    private String link;
    private String oldPrice;
    private String newPrice;
    private Map<String, String> specifications;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Product Details ===\n");
        sb.append("Title: ").append(title).append("\n");
        sb.append("Link: ").append(link).append("\n");
        sb.append("Old Price: ").append(oldPrice.isEmpty() ? "No old price" : oldPrice).append("\n");
        sb.append("New Price: ").append(newPrice).append("\n");

        if (specifications != null && !specifications.isEmpty()) {
            sb.append("Specifications:\n");
            specifications.forEach((key, value) -> sb.append("  - ").append(key).append(": ").append(value).append("\n"));
        } else {
            sb.append("Specifications: Not available\n");
        }

        sb.append("========================\n");
        return sb.toString();
    }
}
