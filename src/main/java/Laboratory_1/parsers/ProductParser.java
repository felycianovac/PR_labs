package Laboratory_1.parsers;

import Laboratory_1.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static Laboratory_1.utils.ValidationUtils.validatePrice;
import static Laboratory_1.utils.ValidationUtils.validateString;


public class ProductParser {

    //task 3
    public Product parseProductDetails(Element productElement) {
        String title = validateString(productElement.select(".title-product.fs-16.lh-19.mb-sm-2").text());
//        System.out.println(title);
        String productLink = productElement.select("a.product-link").attr("href");
//        System.out.println(productLink);
        String oldPrice = validatePrice(productElement.select(".price-old").text()) + " lei";
//        System.out.println(oldPrice);
        Element newPriceElement = productElement.select(".price-new.fw-600.fs-16.lh-19.align-self-end").first();
        String newPrice = "";

        if (newPriceElement != null) {
//            String priceValue = validatePrice(newPriceElement.select("b").text());
//           String currency = newPriceElement.ownText();
            newPrice = validatePrice(newPriceElement.text().trim()) + " lei";
        }
//        System.out.println(newPrice + " new");
        if(oldPrice.equals("0 lei"))
            oldPrice = newPrice;
//        System.out.println(oldPrice + " old");
//        System.out.println("Product: " + title + " " + productLink + " " + oldPrice + " " + newPrice);
        return new Product(title, productLink, oldPrice, newPrice, fetchAdditionalProductData(productLink));
    }

    //task 4
    public Map<String, String> fetchAdditionalProductData(String productUrl) {
        Map<String, String> specsMap = new LinkedHashMap<>();

        try {
            Document productPage = Jsoup.connect(productUrl).get();

            Elements specRows = productPage.select("tr.d-flex");

            for (Element row : specRows) {
                Elements cells = row.select("td");
                if (cells.size() == 2) {
                    String key = cells.get(0).text().trim();
                    String value = cells.get(1).text().trim();
                    specsMap.put(key, value);
                }
            }
        } catch (IOException e) {
            System.err.println("Error fetching product page: " + productUrl);
            e.printStackTrace();
        }
//        System.out.println(specsMap.toString());

        return specsMap;
    }


    private Map<String, String> processProductSpecifications(Elements specifications) {
        Map<String, String> specsMap = new LinkedHashMap<>();

        for (Element spec : specifications) {
            String[] keyValue = spec.text().split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                specsMap.put(key, value);
            }
        }

        return specsMap;
    }

    public List<Product> parseProductsFromPage(String pageContent) throws Exception {
        Document doc = Jsoup.parse(pageContent);
//        System.out.println("Document: " + doc.toString());
        Elements productElements = doc.select(".product-card.bg-color-1c.br-20.position-relative.h-100.product-item");


        return productElements.stream()
                .map(this::parseProductDetails)
                .collect(Collectors.toList());
    }


}

