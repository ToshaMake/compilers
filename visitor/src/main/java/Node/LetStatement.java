package Node;

public class LetStatement implements Node{
    private Identificator identificator;
    private Type type;
    private Expression expression;

    public LetStatement(Identificator identificator, Type type, Expression expression) {
        this.identificator = identificator;
        this.type = type;
        this.expression = expression;
    }

    public LetStatement(Identificator identificator, Type type) {
        this.identificator = identificator;
        this.type = type;
    }

    public LetStatement(Identificator identificator, Expression expression) {
        this.identificator = identificator;
        this.expression = expression;
    }

    public LetStatement(Identificator identificator) {
        this.identificator = identificator;
    }

    public Identificator getIdentificator() {
        return identificator;
    }

    public Type getType() {
        return type;
    }

    public Expression getExpression() {
        return expression;
    }
}
