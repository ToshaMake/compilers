package Node;

public class NegationExpression implements Node{
    private String operation;
    private Expression expression;

    public NegationExpression(String operation, Expression expression) {
        this.operation = operation;
        this.expression = expression;
    }

    public String getOperation() {
        return operation;
    }

    public Expression getExpression() {
        return expression;
    }
}
