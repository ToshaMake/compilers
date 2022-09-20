package Node;

public class GroupedExpression implements Node{
    private Expression expression;

    public GroupedExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }
}
