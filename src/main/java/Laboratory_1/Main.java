package Laboratory_1;

import Laboratory_1.services.ProductService;

import static Laboratory_1.utils.TCPSocketHTTPClient.sendHttpsGetRequestWithHeaders;

public class Main {

    public static void main(String[] args) {

        String url = "https://darwin.md/telefoane";
//        String responseBody = sendHttpsGetRequestWithHeaders("darwin.md", "/telefoane");
//        System.out.println(responseBody);

        ProductService productService = new ProductService();
//
        productService.scrapeProducts("darwin.md", "/telefoane");

//        System.out.println(productService.processProducts(url, 100, 1000));

    }
}
