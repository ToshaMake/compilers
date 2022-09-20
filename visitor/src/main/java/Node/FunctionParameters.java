package Node;

import java.util.ArrayList;

public class FunctionParameters implements Node{
    private ArrayList<FunctionParameter> functionParameters;

    public FunctionParameters(ArrayList<FunctionParameter> functionParameters) {
        this.functionParameters = functionParameters;
    }

    public ArrayList<FunctionParameter> getFunctionParameters() {
        return functionParameters;
    }
}