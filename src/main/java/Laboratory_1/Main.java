package Laboratory_1;

import Laboratory_1.services.ProductService;

public class Main {
    public static void main(String[] args) {
        String url = "https://darwin.md/telefoane";
        ProductService productService = new ProductService();

        productService.scrapeAndDisplayProducts(url);
    }
}
