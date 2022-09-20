package Node;

public class FunctionDefinition implements Node{
    private Identificator identificator;
    private FunctionParameters functionParameters;
    private FunctionReturnType functionReturnType;
    private BlockExpression blockExpression;

    public FunctionDefinition(Identificator identificator, FunctionParameters functionParameters, FunctionReturnType functionReturnType, BlockExpression blockExpression) {
        this.identificator = identificator;
        this.functionParameters = functionParameters;
        this.functionReturnType = functionReturnType;
        this.blockExpression = blockExpression;
    }

    public FunctionDefinition(Identificator identificator) {
        this.identificator = identificator;
    }

    public FunctionDefinition(Identificator identificator, FunctionParameters functionParameters) {
        this.identificator = identificator;
        this.functionParameters = functionParameters;
    }

    public FunctionDefinition(Identificator identificator, FunctionParameters functionParameters, FunctionReturnType functionReturnType) {
        this.identificator = identificator;
        this.functionParameters = functionParameters;
        this.functionReturnType = functionReturnType;
    }

    public FunctionDefinition(Identificator identificator, FunctionReturnType functionReturnType, BlockExpression blockExpression) {
        this.identificator = identificator;
        this.functionReturnType = functionReturnType;
        this.blockExpression = blockExpression;
    }

    public FunctionDefinition(Identificator identificator, FunctionReturnType functionReturnType) {
        this.identificator = identificator;
        this.functionReturnType = functionReturnType;
    }

    public FunctionDefinition(Identificator identificator, BlockExpression blockExpression) {
        this.identificator = identificator;
        this.blockExpression = blockExpression;
    }

    public FunctionDefinition(Identificator identificator, FunctionParameters functionParameters,  BlockExpression blockExpression) {
        this.identificator = identificator;
        this.functionParameters = functionParameters;
        this.blockExpression = blockExpression;
    }

    public BlockExpression getBlockExpression() {
        return blockExpression;
    }

    public FunctionReturnType getFunctionReturnType() {
        return functionReturnType;
    }

    public FunctionParameters getFunctionParameters() {
        return functionParameters;
    }

    public Identificator getIdentificator() {
        return identificator;
    }
}
