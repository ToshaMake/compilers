package Node;

public class AssignmentExpression implements Node{
    private Expression expression1;
    private String operation;
    private Expression expression2;

    public AssignmentExpression(Expression expression1, String operation, Expression expression2) {
        this.expression1 = expression1;
        this.operation = operation;
        this.expression2 = expression2;
    }

    public Expression getExpression1() {
        return expression1;
    }

    public String getOperation() {
        return operation;
    }

    public Expression getExpression2() {
        return expression2;
    }
}
