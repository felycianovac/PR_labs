package Laboratory_1.parsers;

import Laboratory_1.utils.WebFetcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExchangeRateParser {

    public double getMdlToEurRate() {
            String url = "https://www.curs.md/ro";
            String pageContent = WebFetcher.fetchPageContent(url);

            if (pageContent != null) {
                try {
                    Document doc = Jsoup.parse(pageContent);
                    Elements rows = doc.select("tr");

                    for (Element row : rows) {
                        Element currencyElement = row.selectFirst("td.currency");
                        if (currencyElement != null && currencyElement.text().equalsIgnoreCase("EUR")) {
                            Element rateElement = row.selectFirst("td.rate");
                            if (rateElement != null) {
                                String rateText = rateElement.text().replace(",", ".").replaceAll("[^\\d.]", "").trim();
                                return Double.parseDouble(rateText);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Failed to parse exchange rate.");
                    e.printStackTrace();
                }
            }

            return 20;
        }
    }
