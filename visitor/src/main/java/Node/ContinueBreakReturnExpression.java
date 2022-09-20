package Node;

public class ContinueBreakReturnExpression implements Node{
    private String operation;
    private Expression expression;

    public ContinueBreakReturnExpression(String operation) {
        this.operation = operation;
    }

    public ContinueBreakReturnExpression(String operation, Expression expression) {
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
