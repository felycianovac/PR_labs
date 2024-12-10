package Laboratory_1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String title;
    private String link;
    private String oldPrice;
    private String newPrice;
    private Map<String, String> specifications;




    @Override
    public String toString() {
        StringBuilder specificationsString = new StringBuilder();

        // Iterate over the specifications map to build the JSON for the specifications
        specifications.forEach((key, value) -> {
            specificationsString
                    .append("\\\"").append(key).append("\\\": \\\"")
                    .append(value).append("\\\", ");
        });

        // Remove the trailing comma and space from the specifications part if necessary
        if (specificationsString.length() > 2) {
            specificationsString.setLength(specificationsString.length() - 2); // remove last comma
        }

        // Build the entire JSON string with proper escaping
        return "\"{\\\"title\\\": \\\"" + title + "\\\", " +
                "\\\"link\\\": \\\"" + link + "\\\", " +
                "\\\"oldPrice\\\": \\\"" + oldPrice + "\\\", " +
                "\\\"newPrice\\\": \\\"" + newPrice + "\\\", " +
                "\\\"specifications\\\": {" + specificationsString +
                "}}\"";
    }



}
