package Laboratory_2.specification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SpecificationService {

    @Autowired
    private SpecificationRepository specificationRepository;

    public Specification createSpecification(Specification specification) {
        return specificationRepository.save(specification);
    }

    public Optional<Specification> getSpecificationById(int  id) {
        return specificationRepository.findById(id);
    }
}

