import model.ProcessedProductData;
import model.Product;
import parsers.ProductParser;
import serialization.CustomSerializer;
import serialization.ManualProcessedProductSerializer;
import serialization.ManualProductSerializer;
import services.ProductService;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static serialization.CustomSerializer.*;
import static utils.TCPSocketHTTPClient.sendHttpsGetRequestWithHeaders;

public class Main {

    public static void main(String[] args) {
//
        String url = "https://darwin.md/telefoane/smartphone";
        //task 6
        ProductService productService = new ProductService();
        System.out.println(productService.scrapeProducts(url));
//
//        List<Product> products = productService.scrapeProducts("darwin.md", "/telefoane/smartphone");


        //task 7
//        System.out.println(productService.processProducts(url, 100, 1000));



//        ProcessedProductData processedProductData = productService.processProducts(url, 100, 1000);
//        System.out.println(processedProductData);

//        //task 7
//        productService.scrapeProducts("darwin.md", "/telefoane");
//
//        //task 8
        ManualProductSerializer manualProductSerializer = new ManualProductSerializer();
//        ManualProcessedProductSerializer manualProcessedProductSerializer = new ManualProcessedProductSerializer();

//
//        if (products != null && !products.isEmpty()) {
//            System.out.println("=== Serialized Product List (Before Processing) ===");
//            for (Product product : products) {
//                System.out.println("Product JSON:\n" + manualProductSerializer.serializeToJSON(product));
//                System.out.println("Product XML:\n" + manualProductSerializer.serializeToXML(product));
//                System.out.println("---------------------------------------");
//            }
//
//            ProcessedProductData processedProductData = productService.processProducts(url, 100, 1000);
//            System.out.println(processedProductData.toString());
//
//            System.out.println("=== Serialized Processed Product Data ===");
//            System.out.println("Processed Product Data JSON:\n" + manualProcessedProductSerializer.serializeToJSON(processedProductData));
//            System.out.println("Processed Product Data XML:\n" + manualProcessedProductSerializer.serializeToXML(processedProductData));
//
//        } else {
//            System.out.println("No products found or scraping failed.");
//        }
//
//        String serializedProducts = serialize(products);
//        System.out.println(serializedProducts);
//        System.out.println(deserialize(serializedProducts));
    }

}


//        System.out.println(productService.processProducts(url, 100, 1000));
