package Node;

import java.util.ArrayList;

public class ArrayExpression implements Node{
    private ArrayList<Parameter> parameters;

    public ArrayExpression(ArrayList<Parameter> parametrs) {
        this.parameters = parametrs;
    }

    public ArrayList<Parameter> getExpressions() {
        return parameters;
    }
}
