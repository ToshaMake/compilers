package Node;

public class TypeCastExpression implements Node{
    private Expression expression;
    private Type type;

    public TypeCastExpression(Expression expression, Type type) {
        this.expression = expression;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public Expression getExpression() {
        return expression;
    }
}
