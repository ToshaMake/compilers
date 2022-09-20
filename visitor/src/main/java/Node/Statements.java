package Node;

import java.util.ArrayList;

public class Statements implements Node {
    private ArrayList<Statement> statements;
    private Expression expression;

    public Statements(Expression expression) {
        this.expression = expression;
    }

    public Statements(ArrayList<Statement> statements, Expression expression) {
        this.statements = statements;
        this.expression = expression;
    }

    public Statements(ArrayList<Statement> statements) {
        this.statements = statements;
    }

    public ArrayList<Statement> getStatements() {
        return statements;
    }

    public Expression getExpression() {
        return expression;
    }
}
