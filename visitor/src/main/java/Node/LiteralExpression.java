package Node;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LiteralExpression implements Node{
    private String expr;

    public LiteralExpression(String expr, String type, int position) {
        this.expr = expr;
        this.type = type;
        this.position = position;
    }

    public String getExpr() {
        return expr;
    }
    private String type;
    private int position;
    @JsonIgnore
    public String getType() {
        if (type.equals("int"))
            return "i32";
        return type;
    }
    @JsonIgnore
    public int getPosition() {
        return position;
    }
}
