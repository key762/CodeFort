package coffee.lucks.codefort.util.custom;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;

public class CustomUtil {

    private static CustomUtil instance;

    private final Map<String, CustomRegister> customUtils = new HashMap<>();

    public static synchronized CustomUtil getInstance() {
        if (instance == null) {
            instance = new CustomUtil();
        }
        return instance;
    }

    public CustomUtil() {
        for (CustomRegister customRegister : ServiceLoader.load(CustomRegister.class)) {
            register(customRegister);
        }
    }

    public void register(CustomRegister customUtil) {
        customUtils.put(customUtil.utilName(), customUtil);
    }

    public CustomRegister get(String type) {
        return customUtils.get(type.toLowerCase(Locale.ROOT));
    }

}
