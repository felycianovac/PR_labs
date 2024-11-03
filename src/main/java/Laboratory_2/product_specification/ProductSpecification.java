package Laboratory_2.product_specification;

import Laboratory_2.product.ProductEntity;
import Laboratory_2.specification.Specification;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product_specification")
public class ProductSpecification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specification_id", nullable = false)
    private Specification specification;

    @Column(name = "spec_value", nullable = false)
    private String value;

}
