package Node;

public class IndexExpression implements  Node{
    private Expression expression1;
    private Expression expression2;

    public IndexExpression(Expression expression1, Expression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    public Expression getExpression1() {
        return expression1;
    }

    public Expression getExpression2() {
        return expression2;
    }
}
