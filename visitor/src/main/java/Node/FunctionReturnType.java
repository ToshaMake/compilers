package Node;
public class FunctionReturnType implements Node {
    private Type type;

    public FunctionReturnType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
