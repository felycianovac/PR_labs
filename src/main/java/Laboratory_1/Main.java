package Laboratory_1;

public class Main {

    public static void main(String[] args) {
        WebScrapper scrapper = new WebScrapper();
        HtmlParser parser = new HtmlParser();

        String htmlContent = scrapper.baseGetRequest();

        parser.extractProductDetails(htmlContent);    }
}
