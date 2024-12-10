package Laboratory_1.services;

import Laboratory_1.model.ProcessedProductData;
import Laboratory_1.model.Product;
import Laboratory_1.parsers.ProductParser;
import Laboratory_1.utils.WebFetcher;
import Laboratory_3.rabbitMQ.RabbitMQPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static Laboratory_1.parsers.ExchangeRateParser.getEurToMdlRate;
import static Laboratory_1.utils.TCPSocketHTTPClient.sendHttpsGetRequestWithHeaders;
import static Laboratory_1.utils.WebFetcher.fetchPageContent;

public class ProductService {


    private ProductParser productParser;


    public ProductService() {
        this.productParser = new ProductParser();

    }

    public List<Product> scrapeProducts(String url) {
        String pageContent = fetchPageContent(url);
        return scrapeAndDisplay(pageContent);
    }

    public List<Product> scrapeProducts(String host, String path) {
        String pageContent = sendHttpsGetRequestWithHeaders(host, path);
        return scrapeAndDisplay(pageContent);
    }


    public List<Product> scrapeAndDisplay(String pageContent) {
        if (pageContent != null) {
            try {
//                System.out.println(pageContent);
                List<Product> productList =productParser.parseProductsFromPage(pageContent);
                productList.forEach(System.out::println);
                productList.forEach(product -> {
                    try {
                        sendHttpRequestToRabbitController(product.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                return productList;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private void sendHttpRequestToRabbitController(String productMessage) throws Exception {
        String apiUrl = "http://localhost:8080/api/rabbit/send";

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        String jsonInputString = "{\"message\":"  + productMessage + "}";
        System.out.println(jsonInputString);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }



        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Message sent to RabbitMQ successfully for: " + productMessage);
        } else {
            System.out.println("Failed to send message for: " + productMessage + " with response code: " + responseCode);
        }

        connection.disconnect();
    }


    public ProcessedProductData processProducts(String url, double minPriceEUR, double maxPriceEUR) {
        String pageContent = WebFetcher.fetchPageContent(url);

        if (pageContent != null) {
            try {
                List<Product> products = productParser.parseProductsFromPage(pageContent);

//                products.forEach(System.out::println);
                double eurToMdlRate = getEurToMdlRate();
                double mdlToEurRate = 1 / eurToMdlRate;


                List<Product> mappedProducts = products.stream()
                        .map(product -> convertPriceToEUR(product, mdlToEurRate, eurToMdlRate))
                        .collect(Collectors.toList());

                List<Product> filteredProducts = mappedProducts.stream()
                        .filter(product -> {
                            double priceEUR = Double.parseDouble(
                                    (product.getNewPrice() != null && !product.getNewPrice().isEmpty() ? product.getNewPrice() : product.getOldPrice())
                                            .replaceAll("[^\\d.]", ""));
                            return priceEUR >= minPriceEUR && priceEUR <= maxPriceEUR;
                        })
                        .collect(Collectors.toList());

                Optional<Double> totalSum = filteredProducts.stream()
                        .map(product -> Double.parseDouble((product.getNewPrice() != null && !product.getNewPrice().isEmpty() ? product.getNewPrice() : product.getOldPrice())
                                        .replaceAll("[^\\d.]", "")))
                        .reduce(Double::sum);

                return new ProcessedProductData(filteredProducts, totalSum.orElse(0.0)+" EUR", Instant.now());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ProcessedProductData(null, "0.0 EUR", Instant.now());
    }

    private Product convertPriceToEUR(Product product, double mdlToEurRate, double eurToMdlRate) {
        String newPrice = product.getNewPrice();
        if (newPrice != null && !newPrice.isEmpty()) {
            String newCurrency = newPrice.replaceAll("[0-9]", "").trim();
            double newPriceValue = Double.parseDouble(newPrice.replaceAll("[^\\d.]", ""));
            if (newCurrency.equalsIgnoreCase("lei")) {
                product.setNewPrice(String.valueOf(newPriceValue * mdlToEurRate)+" EUR");
            } else if (newCurrency.equalsIgnoreCase("EUR")) {
                product.setNewPrice(String.valueOf(newPriceValue * eurToMdlRate)+" lei");
            }
        }

        String oldPrice = product.getOldPrice();
        if (oldPrice != null && !oldPrice.isEmpty()) {
            String oldCurrency = oldPrice.replaceAll("[0-9]", "").trim();
            double oldPriceValue = Double.parseDouble(oldPrice.replaceAll("[^\\d.]", ""));
            if (oldCurrency.equalsIgnoreCase("lei")) {
                product.setOldPrice(String.valueOf(oldPriceValue * mdlToEurRate)+ " EUR");
            } else if (oldCurrency.equalsIgnoreCase("EUR")) {
                product.setOldPrice(String.valueOf(oldPriceValue * eurToMdlRate)+ " lei");
            }
        }

        return product;
    }
}