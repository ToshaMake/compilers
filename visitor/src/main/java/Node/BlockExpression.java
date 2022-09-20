package Node;

public class BlockExpression implements Node{
    private Statements statements;

    public BlockExpression(Statements statements) {
        this.statements = statements;
    }

    public Statements getStatements() {
        return statements;
    }
}
