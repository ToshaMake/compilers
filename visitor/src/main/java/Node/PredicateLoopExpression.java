package Node;

public class PredicateLoopExpression implements Node{
    private Expression expression;
    private BlockExpression blockExpression;

    public PredicateLoopExpression(Expression expression, BlockExpression blockExpression) {
        this.expression = expression;
        this.blockExpression = blockExpression;
    }

    public Expression getExpression() {
        return expression;
    }

    public BlockExpression getBlockExpression() {
        return blockExpression;
    }
}
