package coffee.lucks.codefort.util.custom;

public abstract class CustomRegister {

    public abstract String utilName();

    public void register() {
        CustomUtil.getInstance().register(this);
    }
}
