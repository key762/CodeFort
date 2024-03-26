package coffee.lucks.codefort.embeds.arms;

import java.util.HashMap;
import java.util.Map;

public class MapArm {

    /**
     * map转json字符串
     *
     * @param map map
     * @return json字符串
     */
    private static String toString(Map<String, Object> map) {
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

    /**
     * json字符串转map
     *
     * @param jsonString 字符串
     * @return map
     */
    private static Map<String, Object> toMap(String jsonString) {
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
