package utils;

public class ValidationUtils {

    public static String validateString(String input) {
        if (input != null && !input.isEmpty()) {
            return input.trim(); //rm whitespaces from start and end
        }
        return "Unknown";
    }

    public static String validatePrice(String price) {
        if (price != null && !price.isEmpty()) {
            String cleanedPrice = price.replaceAll("[^\\d]", "");
            return cleanedPrice.isEmpty() ? "0" : cleanedPrice;    }
        return "0";
    }
}
