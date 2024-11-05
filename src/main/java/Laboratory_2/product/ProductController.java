package Laboratory_2.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductRequest product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id")  int id) {
        return ResponseEntity.ok(productService.getProductById(id));}


    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "5") int limit) {

        Page<ProductDTO> productResponses = productService.getAllProducts(offset, limit);

        return ResponseEntity.ok(productResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable int id, @RequestBody ProductRequest product) {

        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @PostMapping("/upload")
    public ResponseEntity<ProductDTO> uploadProduct(@RequestParam("file") MultipartFile file) {
        try {
            File tempFile = File.createTempFile("product", ".json");
            file.transferTo(tempFile);

            ProductDTO productResponse = productService.saveProductFromJsonFile(tempFile.getAbsolutePath());

            return ResponseEntity.ok(productResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
