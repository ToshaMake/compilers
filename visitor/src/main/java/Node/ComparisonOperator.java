package Node;

public class ComparisonOperator implements Node{
    private String op;

    public ComparisonOperator(String op) {
        this.op = op;
    }

    public String getOp() {
        return op;
    }
}
