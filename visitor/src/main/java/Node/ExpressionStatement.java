package Node;

public class ExpressionStatement implements Node{
    private Expression expression;
    private ExpressionWithBlock expressionWithBlock;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    public ExpressionStatement(ExpressionWithBlock expressionWithBlock) {
        this.expressionWithBlock = expressionWithBlock;
    }

    public Expression getExpression() {
        return expression;
    }

    public ExpressionWithBlock getExpressionWithBlock() {
        return expressionWithBlock;
    }
}
