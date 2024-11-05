package Laboratory_2.product_specification;

import Laboratory_2.product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, Integer> {


    Set<ProductSpecification> findAllByProduct(ProductEntity savedProduct);
}
