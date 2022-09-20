package Node;

public class ArrayType implements Node{
    private Type type;
    private String size;

    public ArrayType(Type type, String size) {
        this.type = type;
        this.size = size;
    }

    public Type getType() {
        return type;
    }

    public String getSize() {
        return size;
    }
}
