package Node;

public class Parameter implements Node {
    private LiteralExpression literalExpression;
    private PathExpression pathExpression;

    public Parameter(LiteralExpression literalExpression) {
        this.literalExpression = literalExpression;
    }

    public Parameter(PathExpression pathExpression) {
        this.pathExpression = pathExpression;
    }

    public LiteralExpression getLiteralExpression() {
        return literalExpression;
    }

    public PathExpression getPathExpression() {
        return pathExpression;
    }
}
