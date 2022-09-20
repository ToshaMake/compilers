package Node;

public class InfiniteLoopExpression implements Node{
    private BlockExpression blockExpression;

    public InfiniteLoopExpression(BlockExpression blockExpression) {
        this.blockExpression = blockExpression;
    }

    public BlockExpression getBlockExpression() {
        return blockExpression;
    }
}
