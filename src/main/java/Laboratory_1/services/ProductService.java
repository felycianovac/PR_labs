package Laboratory_1.services;

import Laboratory_1.model.Product;
import Laboratory_1.parsers.ProductParser;
import Laboratory_1.utils.WebFetcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static Laboratory_1.utils.WebFetcher.fetchPageContent;

public class ProductService {

    private ProductParser productParser;

    public ProductService() {
        this.productParser = new ProductParser();
    }

    public void scrapeAndDisplayProducts(String url) {
        String pageContent = fetchPageContent(url);

        if (pageContent != null) {
            try {
                Document doc = Jsoup.parse(pageContent);
                Elements products = doc.select(".card.card-product");

                List<Product> productList = new ArrayList<>();
                for (Element productElement : products) {
                    Product product = productParser.parseProductDetails(productElement);

//                    Map<String, String> specs = htmlParser.fetchAdditionalProductData(product.getLink());
//                    product.setSpecifications(specs);

                    productList.add(product);
                }

                productList.forEach(System.out::println);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
