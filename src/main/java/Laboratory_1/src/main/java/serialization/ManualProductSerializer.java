package serialization;

import model.Product;

import java.util.Map;

public class ManualProductSerializer {

    public static String serializeToJSON(Product product) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");

        json.append("  \"title\": \"").append(product.getTitle()).append("\",\n");
        json.append("  \"link\": \"").append(product.getLink()).append("\",\n");
        json.append("  \"oldPrice\": \"").append(product.getOldPrice()).append("\",\n");
        json.append("  \"newPrice\": \"").append(product.getNewPrice()).append("\",\n");

        json.append("  \"specifications\": {\n");
        Map<String, String> specifications = product.getSpecifications();
        if (specifications != null && !specifications.isEmpty()) {
            int count = 0;
            for (Map.Entry<String, String> entry : specifications.entrySet()) {
                json.append("    \"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\"");
                if (++count < specifications.size()) {
                    json.append(",");
                }
                json.append("\n");
            }
        }
        json.append("  }\n");

        json.append("}");
        return json.toString();
    }

    public static String serializeToXML(Product product) {
        StringBuilder xml = new StringBuilder();
        xml.append("<Product>\n");

        xml.append("  <Title>").append(product.getTitle()).append("</Title>\n");
        xml.append("  <Link>").append(product.getLink()).append("</Link>\n");
        xml.append("  <OldPrice>").append(product.getOldPrice()).append("</OldPrice>\n");
        xml.append("  <NewPrice>").append(product.getNewPrice()).append("</NewPrice>\n");

        xml.append("  <Specifications>\n");
        Map<String, String> specifications = product.getSpecifications();
        if (specifications != null && !specifications.isEmpty()) {
            for (Map.Entry<String, String> entry : specifications.entrySet()) {
                xml.append("    <").append(entry.getKey()).append(">")
                        .append(entry.getValue())
                        .append("</").append(entry.getKey()).append(">\n");
            }
        }
        xml.append("  </Specifications>\n");

        xml.append("</Product>");
        return xml.toString();
    }
}
