package Node;

public class ArithmeticOrLogicalExpression implements Node{
    private Expression expression1;
    private String operation;
    private Expression expression2;

    public ArithmeticOrLogicalExpression(Expression expression1, String operation, Expression expression2) {
        this.expression1 = expression1;
        this.operation = operation;
        this.expression2 = expression2;
    }

    public String getOperation() {
        return operation;
    }

    public Expression getExpression1() {
        return expression1;
    }

    public Expression getExpression2() {
        return expression2;
    }
}
