package Node;

public class CompoundAssignOperator implements Node{
    private String text;

    public CompoundAssignOperator(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
