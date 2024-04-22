package coffee.lucks.codefort.embeds.unit;

public class FortLoader extends ClassLoader {

    public Class get(byte[] b) {
        return super.defineClass(b, 0, b.length);
    }

}
