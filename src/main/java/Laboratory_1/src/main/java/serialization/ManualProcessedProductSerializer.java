package serialization;

import model.ProcessedProductData;
import model.Product;

public class ManualProcessedProductSerializer {

    public static String serializeToJSON(ProcessedProductData data) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");

        json.append("  \"totalSum\": ").append(data.getTotalSum()).append(",\n");
        json.append("  \"timestamp\": \"").append(data.getTimestamp()).append("\",\n");

        json.append("  \"products\": [\n");
        for (int i = 0; i < data.getFilteredProducts().size(); i++) {
            json.append(ManualProductSerializer.serializeToJSON(data.getFilteredProducts().get(i)));
            if (i < data.getFilteredProducts().size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("  ]\n");

        json.append("}");
        return json.toString();
    }

    public static String serializeToXML(ProcessedProductData data) {
        StringBuilder xml = new StringBuilder();
        xml.append("<ProcessedProductData>\n");

        xml.append("  <TotalSum>").append(data.getTotalSum()).append("</TotalSum>\n");
        xml.append("  <Timestamp>").append(data.getTimestamp()).append("</Timestamp>\n");

        xml.append("  <Products>\n");
        for (Product product : data.getFilteredProducts()) {
            xml.append(ManualProductSerializer.serializeToXML(product)).append("\n");
        }
        xml.append("  </Products>\n");

        xml.append("</ProcessedProductData>");
        return xml.toString();
    }
}
