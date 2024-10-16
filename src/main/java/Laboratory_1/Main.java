package Laboratory_1;

import Laboratory_1.model.ProcessedProductData;
import Laboratory_1.model.Product;
import Laboratory_1.parsers.ProductParser;
import Laboratory_1.serialization.ManualProcessedProductSerializer;
import Laboratory_1.serialization.ManualProductSerializer;
import Laboratory_1.services.ProductService;

import java.util.List;

import static Laboratory_1.utils.TCPSocketHTTPClient.sendHttpsGetRequestWithHeaders;

public class Main {

    public static void main(String[] args) {

        String url = "https://darwin.md/telefoane";
        ProductService productService = new ProductService();



        ManualProductSerializer manualProductSerializer = new ManualProductSerializer();
        ManualProcessedProductSerializer manualProcessedProductSerializer = new ManualProcessedProductSerializer();

        List<Product> products = productService.scrapeProducts("darwin.md", "/telefoane");

        if (products != null && !products.isEmpty()) {
            System.out.println("=== Serialized Product List (Before Processing) ===");
            for (Product product : products) {
                System.out.println("Product JSON:\n" + manualProductSerializer.serializeToJSON(product));
                System.out.println("Product XML:\n" + manualProductSerializer.serializeToXML(product));
                System.out.println("---------------------------------------");
            }

            ProcessedProductData processedProductData = productService.processProducts(url, 100, 1000);
            System.out.println(processedProductData.toString());

            System.out.println("=== Serialized Processed Product Data ===");
            System.out.println("Processed Product Data JSON:\n" + manualProcessedProductSerializer.serializeToJSON(processedProductData));
            System.out.println("Processed Product Data XML:\n" + manualProcessedProductSerializer.serializeToXML(processedProductData));

        } else {
            System.out.println("No products found or scraping failed.");
        }
    }
}


//        System.out.println(productService.processProducts(url, 100, 1000));
