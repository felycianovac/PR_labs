package Laboratory_1;

public class Main {
    public static void main(String[] args) {
        String url = Constants.URL;
        ProductService productService = new ProductService();

        // Scrape and display products
        productService.scrapeAndDisplayProducts(url);
    }
}
