package product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;


@Getter
@Setter
@Data
@NoArgsConstructor
public class ProductRequest {
    private String title;
    private String link;
    private String oldPrice;
    private String newPrice;
    private Map<String, String> specifications;


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
