package Laboratory_1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductService {

    private WebScrapper webScrapper;
    private HtmlParser htmlParser;

    public ProductService() {
        this.webScrapper = new WebScrapper();
        this.htmlParser = new HtmlParser();
    }

    public void scrapeAndDisplayProducts(String url) {
        String pageContent = webScrapper.fetchPageContent(url);

        if (pageContent != null) {
            try {
                Document doc = Jsoup.parse(pageContent);
                Elements products = doc.select(".card.card-product");

                List<Product> productList = new ArrayList<>();
                for (Element productElement : products) {
                    Product product = htmlParser.parseProductDetails(productElement);

                    Map<String, String> specs = htmlParser.fetchAdditionalProductData(product.getLink());
                    product.setSpecifications(specs);

                    productList.add(product);
                }

                productList.forEach(System.out::println);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
