package Node;

public class CompoundAssignmentExpression implements Node{
    private Expression expression1;
    private CompoundAssignOperator operation;
    private Expression expression2;

    public CompoundAssignmentExpression(Expression expression1, CompoundAssignOperator operation, Expression expression2) {
        this.expression1 = expression1;
        this.operation = operation;
        this.expression2 = expression2;
    }

    public Expression getExpression1() {
        return expression1;
    }

    public CompoundAssignOperator getOperation() {
        return operation;
    }

    public Expression getExpression2() {
        return expression2;
    }
}
