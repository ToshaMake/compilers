package Node;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Identificator implements Node {
    private String text;

    public Identificator(String text, int position) {
        this.text = text;
        this.position = position;
    }

    public String getText() {
        return text;
    }

    private int position;
    @JsonIgnore
    public int getPosition() {
        return position;
    }
}
