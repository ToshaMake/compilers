package Node;

public class Statement implements Node{
    private ExpressionStatement expressionStatement;
    private LetStatement letStatement;

    public Statement(ExpressionStatement expressionStatement) {
        this.expressionStatement = expressionStatement;
    }

    public Statement(LetStatement letStatement) {
        this.letStatement = letStatement;
    }

    public ExpressionStatement getExpressionStatement() {
        return expressionStatement;
    }

    public LetStatement getLetStatement() {
        return letStatement;
    }
}
