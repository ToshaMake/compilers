package Node;

public class FunctionParameter implements Node{
    private Identificator identificator;
    private Type type;

    public FunctionParameter(Identificator identificator, Type type) {
        this.identificator = identificator;
        this.type = type;
    }

    public Identificator getIdentificator() {
        return identificator;
    }

    public Type getType() {
        return type;
    }
}
