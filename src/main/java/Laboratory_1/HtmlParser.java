package Laboratory_1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlParser {

    public void extractProductDetails(String html) {
        try {
            Document doc = Jsoup.parse(html);

            Elements products = doc.select(".card.card-product");
//            System.out.println("Number of products: " + products.html());
            for (Element product : products) {
                String title = product.select(".title a[title]").attr("title");

                String productLink = product.select(".title a[title]").attr("href");

                String oldPrice = product.select(".last-price").text();

                Element newPriceElement = product.select(".price-new").first();
                String newPrice = "";
                String currency = "";

                if (newPriceElement != null) {
                    String priceValue = newPriceElement.select("b").text();
                    currency = newPriceElement.ownText();
                    newPrice = priceValue + " " + currency.trim();
                }
                System.out.println("Product Title: " + title);
                System.out.println("Product Link: " + productLink);
                System.out.println("Old Price: " + (oldPrice.isEmpty() ? "No old price" : oldPrice));
                System.out.println("New Price: " + newPrice);
                System.out.println("--------------------------------");
                extractAdditionalProductData(productLink);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void extractAdditionalProductData(String productUrl) {
        try {
            Document productPage = Jsoup.connect(productUrl).get();

            Elements specifications = productPage.select("ul.features li.char_all");

            Map<String, String> specs = processProductSpecifications(specifications);

            System.out.println("Product Specifications:");
            specs.forEach((key, value) -> System.out.println(key + ": " + value));
            System.out.println("================================");
        } catch (IOException e) {
            System.err.println("Error fetching product page: " + productUrl);
            e.printStackTrace();
        }
    }

    public Map<String, String> processProductSpecifications(Elements specifications) {
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
}
