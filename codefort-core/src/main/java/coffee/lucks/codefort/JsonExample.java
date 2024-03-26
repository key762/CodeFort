package coffee.lucks.codefort;

import java.util.HashMap;
import java.util.Map;

public class JsonExample {
    public static void main(String[] args) {
        // 创建一个 JSON 对象
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("name", "Alice");
        jsonObject.put("age", 30);
        jsonObject.put("city", "New York");

        // 将 JSON 对象转换为字符串
        String jsonString = mapToJsonString(jsonObject);
        System.out.println("JSON String: " + jsonString);

        // 将 JSON 字符串转换为 JSON 对象
        Map<String, Object> parsedJsonObject = jsonStringToMap(jsonString);

        String name = (String) parsedJsonObject.get("name");
        int age = (int) parsedJsonObject.get("age");
        String city = (String) parsedJsonObject.get("city");

        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("City: " + city);
    }

    private static String mapToJsonString(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof String) {
                sb.append("\"").append(entry.getValue()).append("\",");
            } else {
                sb.append(entry.getValue()).append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1); // Remove the extra comma
        sb.append("}");
        return sb.toString();
    }

    private static Map<String, Object> jsonStringToMap(String jsonString) {
        Map<String, Object> map = new HashMap<>();
        jsonString = jsonString.substring(1, jsonString.length() - 1); // Remove curly braces
        String[] keyValuePairs = jsonString.split(",");
        for (String pair : keyValuePairs) {
            String[] entry = pair.split(":");
            String key = entry[0].replace("\"", "").trim();
            String value = entry[1].replace("\"", "").trim();
            if (value.matches("\\d+")) {
                map.put(key, Integer.parseInt(value));
            } else {
                map.put(key, value);
            }
        }
        return map;
    }
}
