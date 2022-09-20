package Node;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Expression implements Node{

    private PathExpression pathExpression;
    private LiteralExpression literalExpression;
    private FieldExpression fieldExpression;
    private CallExpression callExpression;
    private IndexExpression indexExpression;
    private NegationExpression negationExpression;
    private TypeCastExpression typeCastExpression;
    private ArithmeticOrLogicalExpression arithmeticOrLogicalExpression;
    private ComparisonExpression comparisonExpression;
    private LazyBooleanExpression lazyBooleanExpression;
    private AssignmentExpression assignmentExpression;
    private CompoundAssignmentExpression compoundAssignmentExpression;
    private ContinueBreakReturnExpression continueBreakReturnExpression;
    private GroupedExpression groupedExpression;
    private ExpressionWithBlock expressionWithBlock;
    private ArrayExpression arrayExpression;

    public Expression(ArrayExpression arrayExpression, int position) {
        this.arrayExpression = arrayExpression;
        this.position = position;
    }

    public Expression(ExpressionWithBlock expressionWithBlock, int position) {
        this.expressionWithBlock = expressionWithBlock;
        this.position = position;
    }

    public Expression(GroupedExpression groupedExpression, int position) {
        this.groupedExpression = groupedExpression;
        this.position = position;
    }

    public Expression(ContinueBreakReturnExpression continueBreakReturnExpression, int position) {
        this.continueBreakReturnExpression = continueBreakReturnExpression;
        this.position = position;
    }

    public Expression(CompoundAssignmentExpression compoundAssignmentExpression, int position) {
        this.compoundAssignmentExpression = compoundAssignmentExpression;
        this.position = position;
    }

    public Expression(AssignmentExpression assignmentExpression, int position) {
        this.assignmentExpression = assignmentExpression;
        this.position = position;
    }

    public Expression(LazyBooleanExpression lazyBooleanExpression, int position) {
        this.lazyBooleanExpression = lazyBooleanExpression;
        this.position = position;
    }

    public Expression(ComparisonExpression comparisonExpression, int position) {
        this.comparisonExpression = comparisonExpression;
        this.position = position;
    }

    public Expression(ArithmeticOrLogicalExpression arithmeticOrLogicalExpression, int position) {
        this.arithmeticOrLogicalExpression = arithmeticOrLogicalExpression;
        this.position = position;
    }

    public Expression(TypeCastExpression typeCastExpression, int position) {
        this.typeCastExpression = typeCastExpression;
        this.position = position;
    }

    public Expression(NegationExpression negationExpression, int position) {
        this.negationExpression = negationExpression;
        this.position = position;
    }

    public Expression(IndexExpression indexExpression, int position) {
        this.indexExpression = indexExpression;
        this.position = position;
    }

    public Expression(CallExpression callExpression, int position) {
        this.callExpression = callExpression;
        this.position = position;
    }

    public Expression(PathExpression pathExpression, int position) {
        this.pathExpression = pathExpression;
        this.position = position;
    }

    public Expression(LiteralExpression literalExpression, int position) {
        this.literalExpression = literalExpression;
        this.position = position;
    }

    public Expression(FieldExpression fieldExpression, int position) {
        this.fieldExpression = fieldExpression;
        this.position = position;
    }

    public PathExpression getPathExpression() {
        return pathExpression;
    }

    public LiteralExpression getLiteralExpression() {
        return literalExpression;
    }

    public FieldExpression getFieldExpression() {
        return fieldExpression;
    }

    public CallExpression getCallExpression() {
        return callExpression;
    }

    public IndexExpression getIndexExpression() {
        return indexExpression;
    }

    public NegationExpression getNegationExpression() {
        return negationExpression;
    }

    public TypeCastExpression getTypeCastExpression() {
        return typeCastExpression;
    }

    public ArithmeticOrLogicalExpression getArithmeticOrLogicalExpression() {
        return arithmeticOrLogicalExpression;
    }

    public ComparisonExpression getComparisonExpression() {
        return comparisonExpression;
    }

    public LazyBooleanExpression getLazyBooleanExpression() {
        return lazyBooleanExpression;
    }

    public AssignmentExpression getAssignmentExpression() {
        return assignmentExpression;
    }

    public CompoundAssignmentExpression getCompoundAssignmentExpression() {
        return compoundAssignmentExpression;
    }

    public ContinueBreakReturnExpression getContinueBreakReturnExpression() {
        return continueBreakReturnExpression;
    }

    public GroupedExpression getGroupedExpression() {
        return groupedExpression;
    }

    public ExpressionWithBlock getExpressionWithBlock() {
        return expressionWithBlock;
    }

    public ArrayExpression getArrayExpression() {
        return arrayExpression;
    }

    private int position;
    @JsonIgnore
    public int getPosition() {
        return position;
    }

}
