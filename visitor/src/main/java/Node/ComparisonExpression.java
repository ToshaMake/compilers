package Node;

public class ComparisonExpression implements Node{
    private Expression expression1;
    private ComparisonOperator comparisonOperator;
    private Expression expression2;

    public ComparisonExpression(Expression expression1, ComparisonOperator comparisonOperator, Expression expression2) {
        this.expression1 = expression1;
        this.comparisonOperator = comparisonOperator;
        this.expression2 = expression2;
    }

    public Expression getExpression1() {
        return expression1;
    }

    public ComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

    public Expression getExpression2() {
        return expression2;
    }
}
