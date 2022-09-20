package Node;

public class IfExpression implements Node{
    private Expression expression;
    private BlockExpression ifBlockExpression;
    private BlockExpression elseBlockExpression;
    private IfExpression ifExpression;

    public IfExpression(Expression expression, BlockExpression ifBlockExpression, BlockExpression elseBlockExpression) {
        this.expression = expression;
        this.ifBlockExpression = ifBlockExpression;
        this.elseBlockExpression = elseBlockExpression;
    }

    public IfExpression(Expression expression, BlockExpression ifBlockExpression, IfExpression ifExpression) {
        this.expression = expression;
        this.ifBlockExpression = ifBlockExpression;
        this.ifExpression = ifExpression;
    }

    public IfExpression(Expression expression, BlockExpression ifBlockExpression) {
        this.expression = expression;
        this.ifBlockExpression = ifBlockExpression;
    }

    public Expression getExpression() {
        return expression;
    }

    public BlockExpression getIfBlockExpression() {
        return ifBlockExpression;
    }

    public BlockExpression getElseBlockExpression() {
        return elseBlockExpression;
    }

    public IfExpression getIfExpression() {
        return ifExpression;
    }
}
