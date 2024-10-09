package Laboratory_1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {

    public void extractProductDetails(String html) {
        try {
            Document doc = Jsoup.parse(html);

            Elements products = doc.select(".card.card-product");
            System.out.println("Number of products: " + products.html());
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
