package Node;
import  Node.Node;

public class Type implements Node {
    private String text;

    public Type(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
