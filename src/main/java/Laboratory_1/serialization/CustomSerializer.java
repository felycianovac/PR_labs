package Laboratory_1.serialization;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class CustomSerializer {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");


    public static String serialize(Object obj) {
        if (obj instanceof String) {
            return "string:" + obj;
        } else if (obj instanceof Integer) {
            return "int:" + obj;
        } else if (obj instanceof Boolean) {
            return "bool:" + obj;
        } else if (obj instanceof Double) {
            return "double:" + obj;
        } else if (obj instanceof Date) {
            return "date:" + dateFormat.format((Date) obj);
        }
        else if (obj instanceof List) {
            return serializeList((List<?>) obj);
        } else if (obj instanceof Map) {
            return serializeMap((Map<?, ?>) obj);
        } else {
            throw new IllegalArgumentException("Unsupported object type for serialization.");
        }
    }

    public static Object deserialize(String data) {
        if (data.startsWith("string:")) {
            return data.substring(7);
        } else if (data.startsWith("int:")) {
            return Integer.parseInt(data.substring(4));
        } else if (data.startsWith("bool:")) {
            return Boolean.parseBoolean(data.substring(5));
        } else if (data.startsWith("double:")) {
            return Double.parseDouble(data.substring(7));
        }
        else if (data.startsWith("date:")) {
            try {
                return dateFormat.parse(data.substring(5));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid date format.");
            }
        }
        else if (data.startsWith("list:[")) {
            return deserializeList(data);
        } else if (data.trim().startsWith("map:{")) {
            return deserializeMap(data);
        } else {
            throw new IllegalArgumentException("Unsupported data format for deserialization.");
        }
    }

    private static String serializeList(List<?> list) {
        StringBuilder sb = new StringBuilder("list:[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(serialize(list.get(i)));
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private static List<Object> deserializeList(String data) {
        String listContent = data.substring(6, data.length() - 1);
        List<Object> list = new ArrayList<>();
        String[] items = listContent.split("(?<=]),(?=\\w+:)|(?<=]),(?=\\[)|(?<=]),(?=\\{)");
        for (String item : items) {
            list.add(deserialize(item));
        }
        return list;
    }

    private static String serializeMap(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder("map:{");
        int count = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            sb.append(serialize(entry.getKey())).append("::").append(serialize(entry.getValue()));
            if (count < map.size() - 1) {
                sb.append(",");
            }
            count++;
        }
        sb.append("}");
        return sb.toString();
    }


    private static Map<Object, Object> deserializeMap(String data) {
        String mapContent = data.substring(5, data.length() - 1);
        Map<Object, Object> map = new LinkedHashMap<>();
        String[] entries = mapContent.split("(?<=]),(?=\\w+:)");
        for (String entry : entries) {
            String[] keyValue = entry.split("::", 2);

//            System.out.println(keyValue[0]  + keyValue[1]);
            map.put(deserialize(keyValue[0]), keyValue[1]);
        }
        return map;
    }

    public static byte[] serializeToBytes(Object obj) {
        String serializedData = serialize(obj);
        return serializedData.getBytes(StandardCharsets.UTF_8);
    }

    public static Object deserializeFromBytes(byte[] data) {
        String serializedData = new String(data, StandardCharsets.UTF_8);
        return deserialize(serializedData);
    }
}