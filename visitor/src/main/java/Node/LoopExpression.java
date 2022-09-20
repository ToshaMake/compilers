package Node;

public class LoopExpression implements Node{
    private InfiniteLoopExpression infiniteLoopExpression;
    private PredicateLoopExpression predicateLoopExpression;

    public LoopExpression(InfiniteLoopExpression infiniteLoopExpression) {
        this.infiniteLoopExpression = infiniteLoopExpression;
    }

    public LoopExpression(PredicateLoopExpression predicateLoopExpression) {
        this.predicateLoopExpression = predicateLoopExpression;
    }

    public InfiniteLoopExpression getInfiniteLoopExpression() {
        return infiniteLoopExpression;
    }

    public PredicateLoopExpression getPredicateLoopExpression() {
        return predicateLoopExpression;
    }
}
