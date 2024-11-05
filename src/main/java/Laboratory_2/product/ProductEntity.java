package Laboratory_2.product;


import Laboratory_2.product_specification.ProductSpecification;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "link", nullable = false, unique = true)
    private String link;
    @Column(name = "old_price")
    private String oldPrice;
    @Column(name = "new_price")
    private String newPrice;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
    private Set<ProductSpecification> productSpecifications;

    public void addProductSpecification(ProductSpecification productSpecification) {
        if (productSpecifications == null) {
            productSpecifications = new HashSet<>();
        }
        productSpecifications.add(productSpecification);
    }


}
