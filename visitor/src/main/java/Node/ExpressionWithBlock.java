package Node;

public class ExpressionWithBlock implements Node{
    private IfExpression ifExpression;
    private LoopExpression loopExpression;

    public ExpressionWithBlock(IfExpression ifExpression) {
        this.ifExpression = ifExpression;
    }
    public ExpressionWithBlock(LoopExpression loopExpression) {
        this.loopExpression = loopExpression;
    }

    public IfExpression getIfExpression() {
        return ifExpression;
    }

    public LoopExpression getLoopExpression() {
        return loopExpression;
    }
}
