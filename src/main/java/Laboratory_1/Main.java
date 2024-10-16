package Laboratory_1;

import Laboratory_1.model.ProcessedProductData;
import Laboratory_1.model.Product;
import Laboratory_1.parsers.ProductParser;
import Laboratory_1.serialization.ManualProcessedProductSerializer;
import Laboratory_1.serialization.ManualProductSerializer;
import Laboratory_1.services.ProductService;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static Laboratory_1.serialization.CustomSerializer.*;
import static Laboratory_1.utils.TCPSocketHTTPClient.sendHttpsGetRequestWithHeaders;

public class Main {

    public static void main(String[] args) {
//
//        String url = "https://darwin.md/telefoane";
//        ProductService productService = new ProductService();
//
//
//
//        ManualProductSerializer manualProductSerializer = new ManualProductSerializer();
//        ManualProcessedProductSerializer manualProcessedProductSerializer = new ManualProcessedProductSerializer();
//
//        List<Product> products = productService.scrapeProducts("darwin.md", "/telefoane");
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
        String originalString = "Hello, World!";
        String serializedString = serialize(originalString);
        System.out.println("Serialized String: " + serializedString);
        String deserializedString = (String) deserialize(serializedString);
        System.out.println("Deserialized String: " + deserializedString);

        // Serialize and deserialize an integer
        int originalInt = 12345;
        String serializedInt = serialize(originalInt);
        System.out.println("Serialized Integer: " + serializedInt);
        int deserializedInt = (Integer) deserialize(serializedInt);
        System.out.println("Deserialized Integer: " + deserializedInt);

        // Serialize and deserialize a list
        List<Object> originalList = Arrays.asList("Apple", 42, Arrays.asList("Nested", "List"));
        String serializedList = serialize(originalList);
        System.out.println("Serialized List: " + serializedList);
        List<?> deserializedList = (List<?>) deserialize(serializedList);
        System.out.println("Deserialized List: " + deserializedList);

        // Serialize and deserialize a map
        Map<Object, Object> originalMap = new LinkedHashMap<>();
        originalMap.put("Name", "John Doe");
        originalMap.put("Age", 30);
        originalMap.put("Scores", Arrays.asList(85, 90, 88));
        String serializedMap = serialize(originalMap);
        System.out.println("Serialized Map: " + serializedMap);
        Map<?, ?> deserializedMap = (Map<?, ?>) deserialize(serializedMap);
        System.out.println("Deserialized Map: " + deserializedMap);
        System.out.println(serializeToBytes(originalMap));
    }

}


//        System.out.println(productService.processProducts(url, 100, 1000));
