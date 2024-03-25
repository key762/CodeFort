package coffee.lucks.codefort;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String args, Instrumentation inst) throws Exception {
        CodeFortAgent.premain(args, inst);
    }

}