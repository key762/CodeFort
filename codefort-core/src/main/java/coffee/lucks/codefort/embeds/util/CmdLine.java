package coffee.lucks.codefort.embeds.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdLine {

    /**
     * option
     */
    private List<String> options = new ArrayList<>();

    /**
     * hasArgs
     */
    private List<Boolean> hasArgs = new ArrayList<>();

    /**
     * optionsMap
     */
    private Map<String, List<String>> optionsMap = new HashMap<>();

    public CmdLine addOption(String opt, boolean hasArg) {
        opt = resolveOption(opt);
        if (!options.contains(opt)) {
            options.add(opt);
            hasArgs.add(hasArg);
        }
        return this;
    }

    public String getOptionValue(String opt, String dv) {
        String[] values = getOptionValues(opt);
        return (values == null) ? dv : values[0];
    }

    public String[] getOptionValues(String opt) {
        List<String> values = optionsMap.get(resolveOption(opt));
        return (values == null || values.isEmpty()) ? null : values.toArray(new String[values.size()]);
    }

    public CmdLine parse(String[] arguments) {
        int optIndex = -1;
        for (int i = 0; i < arguments.length; i++) {
            String arg = arguments[i];

            if (arg.startsWith("-") || arg.startsWith("--")) {
                arg = resolveOption(arg);
                //check last option hasArg
                if (optIndex > -1 && hasArgs.get(optIndex)) {
                    String lastOption = options.get(optIndex);
                    if (optionsMap.get(lastOption).size() == 0) {
                        throw new IllegalArgumentException("Missing argument for option: " + lastOption);
                    }
                }
                optIndex = options.indexOf(arg);
                if (optIndex < 0) {
                    throw new IllegalArgumentException("Unrecognized option: " + arguments[i]);
                }
                optionsMap.put(arg, new ArrayList<>());
            } else if (optIndex > -1) {
                String option = options.get(optIndex);
                optionsMap.get(option).add(arg);
            }
        }
        return this;
    }

    private static String resolveOption(String str) {
        if (str == null) return null;
        if (str.startsWith("--")) {
            return str.substring(2, str.length());
        } else if (str.startsWith("-")) {
            return str.substring(1, str.length());
        }
        return str;
    }

}
