package Node;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public class PathExpression implements Node{
    private ArrayList<Identificator> path;

    public PathExpression(ArrayList<Identificator> path) {
        this.path = path;
    }

    public ArrayList<Identificator> getPath() {
        return path;
    }

    @JsonIgnore
    public String type;
}
