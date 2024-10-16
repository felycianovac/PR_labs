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
        String title = validateString(productElement.select(".title a[title]").attr("title"));
        String productLink = validateString(productElement.select(".title a[title]").attr("href"));
        String oldPrice = validatePrice(productElement.select(".last-price").text());

        Element newPriceElement = productElement.select(".price-new").first();
        String newPrice = "";
        if (newPriceElement != null) {
            String priceValue = validatePrice(newPriceElement.select("b").text());
            String currency = newPriceElement.ownText();
            newPrice = priceValue + " " + currency.trim();
        }

        return new Product(title, productLink, oldPrice, newPrice, fetchAdditionalProductData(productLink));
    }

    //task 4
    public Map<String, String> fetchAdditionalProductData(String productUrl) {
        try {
            Document productPage = Jsoup.connect(productUrl).get();
            Elements specifications = productPage.select("ul.features li.char_all");
            //select any element that has class "char_all" and is a child of an element with class "features"

            return processProductSpecifications(specifications);
        } catch (IOException e) {
            System.err.println("Error fetching product page: " + productUrl);
            e.printStackTrace();
        }
        return null;
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
        Elements productElements = doc.select(".card.card-product");

        return productElements.stream()
                .map(this::parseProductDetails)
                .collect(Collectors.toList());
    }


}

