package Laboratory_2.specification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/specifications")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    @PostMapping
    public ResponseEntity<Specification> createSpecification(@RequestBody Specification specification) {
        return ResponseEntity.ok(specificationService.createSpecification(specification));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specification> getSpecificationById(@PathVariable int id) {
        return specificationService.getSpecificationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
