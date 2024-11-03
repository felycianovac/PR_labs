package Laboratory_2.specification;


import Laboratory_2.product_specification.ProductSpecification;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "specification")
public class Specification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String type;

    @OneToMany(mappedBy = "specification", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductSpecification> productSpecifications = new HashSet<>();


}