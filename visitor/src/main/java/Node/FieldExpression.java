package Node;

public class FieldExpression implements Node {

    private Expression expression;
    private Identificator identificator;

    public FieldExpression(Expression expression, Identificator identificator) {
        this.expression = expression;
        identificator = identificator;
    }

    public Expression getExpression() {
        return expression;
    }

    public Identificator getIdentificator() {
        return identificator;
    }
}
