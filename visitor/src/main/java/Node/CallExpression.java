package Node;

import java.util.ArrayList;

public class CallExpression implements Node{
    private ArrayList<Expression> expressions;

    public CallExpression(ArrayList<Expression> expressions) {
        this.expressions = expressions;
    }

    public ArrayList<Expression> getExpressions() {
        return expressions;
    }
}
