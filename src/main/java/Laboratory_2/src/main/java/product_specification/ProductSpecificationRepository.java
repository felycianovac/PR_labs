package product_specification;

import product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, Integer> {


    List<ProductSpecification> findByProductId(Integer productId);

    @Modifying
    @Query("DELETE FROM ProductSpecification ps WHERE ps.product.id = :productId")
    void deleteByProductId(@Param("productId") int productId);


}
