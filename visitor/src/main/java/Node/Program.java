package Node;

import java.util.ArrayList;

public class Program implements Node {

    private ArrayList<FunctionDefinition> functionDefinitions;

    public Program(ArrayList<FunctionDefinition> functionDefinitions) {
        this.functionDefinitions = functionDefinitions;
    }
    public ArrayList<FunctionDefinition> getFunctionDefinitions() {
        return functionDefinitions;
    }
}
