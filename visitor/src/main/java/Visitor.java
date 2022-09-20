import Node.*;

import java.util.ArrayList;

public class Visitor extends RustBaseVisitor<Node> {

    @Override
    public Program visitProgram(RustParser.ProgramContext ctx) {
        ArrayList<FunctionDefinition> tree = new ArrayList<>();
        for (int i = 0; i < ctx.functionDefinition().size(); i++) {
            tree.add((FunctionDefinition) this.visit(ctx.functionDefinition(i)));
        }
        return new Program(tree);
    }

    @Override
    public Type visitType(RustParser.TypeContext ctx) {
        Type type = new Type(ctx.getText());
        return type;
    }

    @Override
    public Expression visitTypeCastExpression(RustParser.TypeCastExpressionContext ctx) {
        Expression expression = (Expression) this.visit(ctx.expression());
        Type type = (Type) this.visit(ctx.type());
        TypeCastExpression typeCastExpression = new TypeCastExpression(expression, type);
        return new Expression(typeCastExpression, ctx.getStart().getLine());
    }

    @Override
    public Expression visitPathExpression_(RustParser.PathExpression_Context ctx) {
        return new Expression((PathExpression) this.visit(ctx.pathExpression()), ctx.getStart().getLine());
    }

    @Override
    public Expression visitIndexExpression(RustParser.IndexExpressionContext ctx) {
        Expression expression1 = (Expression) this.visit(ctx.expression(0));
        Expression expression2 = (Expression) this.visit(ctx.expression(1));
        IndexExpression indexExpression = new IndexExpression(expression1, expression2);
        return new Expression(indexExpression, ctx.getStart().getLine());
    }

    @Override
    public Expression visitGroupedExpression(RustParser.GroupedExpressionContext ctx) {
        GroupedExpression groupedExpression = new GroupedExpression((Expression) this.visit(ctx.expression()));
        return new Expression(groupedExpression, ctx.getStart().getLine());
    }

    @Override
    public Expression visitArithmeticOrLogicalExpression(RustParser.ArithmeticOrLogicalExpressionContext ctx) {
        Expression expression1 = (Expression) visit(ctx.expression(0));
        Expression expression2 = (Expression) visit(ctx.expression(1));
        String operation = ctx.op.getText();
        ArithmeticOrLogicalExpression arithmeticOrLogicalExpression = new ArithmeticOrLogicalExpression(expression1, operation, expression2);
        return new Expression(arithmeticOrLogicalExpression, ctx.getStart().getLine());
    }

    @Override
    public Expression visitFieldExpression(RustParser.FieldExpressionContext ctx) {
        Expression expression = (Expression) visit(ctx.expression());
        Identificator identificator = (Identificator) visit(ctx.identificator());
        FieldExpression fieldExpression = new FieldExpression(expression, identificator);
        return new Expression(fieldExpression, ctx.getStart().getLine());
    }

    @Override
    public Expression visitComparisonExpression(RustParser.ComparisonExpressionContext ctx) {
        Expression expression1 = (Expression) visit(ctx.expression(0));
        Expression expression2 = (Expression) visit(ctx.expression(1));
        ComparisonOperator comparisonOperator = (ComparisonOperator) visit(ctx.comparisonOperator());
        ComparisonExpression comparisonExpression = new ComparisonExpression(expression1, comparisonOperator, expression2);
        return new Expression(comparisonExpression, ctx.getStart().getLine());
    }

    @Override
    public Expression visitAssignmentExpression(RustParser.AssignmentExpressionContext ctx) {
        Expression expression1 = (Expression) visit(ctx.expression(0));
        Expression expression2 = (Expression) visit(ctx.expression(1));
        String operation = ctx.op.getText();
        AssignmentExpression assignmentExpression = new AssignmentExpression(expression1, operation, expression2);
        return new Expression(assignmentExpression, ctx.getStart().getLine());
    }

    @Override
    public Expression visitCompoundAssignmentExpression(RustParser.CompoundAssignmentExpressionContext ctx) {
        CompoundAssignOperator compoundAssignOperator = (CompoundAssignOperator) visit(ctx.compoundAssignOperator());
        Expression expression1 = (Expression) visit(ctx.expression(0));
        Expression expression2 = (Expression) visit(ctx.expression(1));
        CompoundAssignmentExpression compoundAssignmentExpression = new CompoundAssignmentExpression(expression1, compoundAssignOperator, expression2);
        return new Expression(compoundAssignmentExpression, ctx.getStart().getLine());
    }

    @Override
    public Expression visitLiteralExpression_(RustParser.LiteralExpression_Context ctx) {
        return new Expression((LiteralExpression) visit(ctx.literalExpression()), ctx.getStart().getLine());
    }

    @Override
    public Expression visitContinueBreakReturnExpression(RustParser.ContinueBreakReturnExpressionContext ctx) {
        String operation = ctx.op.getText();
        if (ctx.expression() != null) {
            Expression expression = (Expression) visit(ctx.expression());
            return new Expression(new ContinueBreakReturnExpression(operation, expression), ctx.getStart().getLine());
        } else {
            return new Expression(new ContinueBreakReturnExpression(operation), ctx.getStart().getLine());
        }
    }

    @Override
    public Expression visitNegationExpression(RustParser.NegationExpressionContext ctx) {
        String operation = ctx.op.getText();
        Expression expression = (Expression) visit(ctx.expression());
        return new Expression(new NegationExpression(operation, expression), ctx.getStart().getLine());
    }

    @Override
    public Expression visitCallExpression(RustParser.CallExpressionContext ctx) {
        ArrayList<Expression> expressions = new ArrayList<>();
        for (int i = 0; i < ctx.expression().size(); i++) {
            expressions.add((Expression) visit(ctx.expression(i)));
        }
        return new Expression(new CallExpression(expressions), ctx.getStart().getLine());
    }

    @Override
    public Expression visitLazyBooleanExpression(RustParser.LazyBooleanExpressionContext ctx) {
        Expression expression1 = (Expression) visit(ctx.expression(0));
        Expression expression2 = (Expression) visit(ctx.expression(1));
        String operation = ctx.op.getText();
        return new Expression(new LazyBooleanExpression(expression1, operation, expression2), ctx.getStart().getLine());
    }

    @Override
    public PathExpression visitPathExpression(RustParser.PathExpressionContext ctx) {
        ArrayList<Identificator> identificators = new ArrayList<>();
        for (int i = 0; i < ctx.identificator().size(); i++) {
            identificators.add((Identificator) visit(ctx.identificator(i)));
        }
        return new PathExpression(identificators);
    }

    @Override
    public LiteralExpression visitLiteralExpression(RustParser.LiteralExpressionContext ctx) {
        String text;
        String type;
        if (ctx.STRING() != null) {
            type = "string";
            text = ctx.getText().substring(1, ctx.getText().length() - 1);
        } else {
            if (ctx.INT() != null) {
                type = "int";
            } else
                type = "bool";
            text = ctx.getText();
        }
        return new LiteralExpression(text, type, ctx.start.getLine());
    }

    @Override
    public ComparisonOperator visitComparisonOperator(RustParser.ComparisonOperatorContext ctx) {
        return new ComparisonOperator(ctx.getText());
    }

    @Override
    public CompoundAssignOperator visitCompoundAssignOperator(RustParser.CompoundAssignOperatorContext ctx) {
        return new CompoundAssignOperator(ctx.getText());
    }

    @Override
    public Identificator visitIdentificator(RustParser.IdentificatorContext ctx) {
        return new Identificator(ctx.getText(), ctx.getStart().getLine());
    }

    @Override
    public FunctionDefinition visitFunctionDefinition(RustParser.FunctionDefinitionContext ctx) {
        Identificator identificator = (Identificator) this.visit(ctx.identificator());

        if (ctx.functionParameters() == null) {
            if (ctx.functionReturnType() == null) {
                if (ctx.blockExpression() == null) {
                    return new FunctionDefinition(identificator);
                } else {
                    BlockExpression blockExpression = (BlockExpression) this.visit(ctx.blockExpression());
                    return new FunctionDefinition(identificator, blockExpression);
                }
            } else {
                FunctionReturnType functionReturnType = (FunctionReturnType) this.visit(ctx.functionReturnType());
                if (ctx.blockExpression() == null) {
                    return new FunctionDefinition(identificator, functionReturnType);
                } else {
                    BlockExpression blockExpression = (BlockExpression) this.visit(ctx.blockExpression());
                    return new FunctionDefinition(identificator, functionReturnType, blockExpression);
                }
            }
        } else {
            FunctionParameters functionParameters = (FunctionParameters) this.visit(ctx.functionParameters());
            if (ctx.functionReturnType() == null) {
                if (ctx.blockExpression() == null) {
                    return new FunctionDefinition(identificator, functionParameters);
                } else {
                    BlockExpression blockExpression = (BlockExpression) this.visit(ctx.blockExpression());
                    return new FunctionDefinition(identificator, functionParameters, blockExpression);
                }
            } else {
                FunctionReturnType functionReturnType = (FunctionReturnType) this.visit(ctx.functionReturnType());
                if (ctx.blockExpression() == null) {
                    return new FunctionDefinition(identificator, functionParameters, functionReturnType);
                } else {
                    BlockExpression blockExpression = (BlockExpression) this.visit(ctx.blockExpression());
                    return new FunctionDefinition(identificator, functionParameters, functionReturnType, blockExpression);
                }
            }
        }
    }

    @Override
    public FunctionParameters visitFunctionParameters(RustParser.FunctionParametersContext ctx) {
        ArrayList<FunctionParameter> functionParameters = new ArrayList<>();
        for (int i = 0; i < ctx.functionParameter().size(); i++) {
            functionParameters.add((FunctionParameter) this.visit(ctx.functionParameter(i)));
        }
        return new FunctionParameters(functionParameters);
    }

    @Override
    public FunctionReturnType visitFunctionReturnType(RustParser.FunctionReturnTypeContext ctx) {
        Type type = (Type) this.visit(ctx.type());
        return new FunctionReturnType(type);
    }

    @Override
    public FunctionParameter visitFunctionParameter(RustParser.FunctionParameterContext ctx) {
        return new FunctionParameter((Identificator) this.visit(ctx.identificator()), (Type) this.visit(ctx.type()));
    }

    @Override
    public BlockExpression visitBlockExpression(RustParser.BlockExpressionContext ctx) {
        if (ctx.statements() != null) {
            Statements statements = (Statements) this.visit(ctx.statements());
            return new BlockExpression(statements);
        } else
            return null;
    }

    @Override
    public Statements visitStatements(RustParser.StatementsContext ctx) {
        if (ctx.statement() != null) {
            ArrayList<Statement> statements = new ArrayList<>();
            for (int i = 0; i < ctx.statement().size(); i++) {
                statements.add((Statement) this.visit(ctx.statement(i)));
            }
            if (ctx.expression() == null) {
                return new Statements(statements);
            } else {
                Expression expression = (Expression) this.visit(ctx.expression());
                return new Statements(statements, expression);
            }
        } else {
            if (ctx.expression() != null) {
                Expression expression = (Expression) this.visit(ctx.expression());
                return new Statements(expression);
            }
        }
        return null;
    }

    @Override
    public Statement visitStatement(RustParser.StatementContext ctx) {
        if (ctx.expressionStatement() != null) {
            ExpressionStatement expressionStatement = (ExpressionStatement) this.visit(ctx.expressionStatement());
            return new Statement(expressionStatement);
        } else {
            LetStatement letStatement = (LetStatement) this.visit(ctx.letStatement());
            return new Statement(letStatement);
        }
    }

    @Override
    public LetStatement visitLetStatement(RustParser.LetStatementContext ctx) {
        Identificator identificator = (Identificator) this.visit(ctx.identificator());

        if (ctx.type() != null) {
            Type type = (Type) this.visit(ctx.type());
            if (ctx.expression() != null) {
                Expression expression = (Expression) this.visit(ctx.expression());
                return new LetStatement(identificator, type, expression);
            } else
                return new LetStatement(identificator, type);
        }
        if (ctx.expression() != null) {
            Expression expression = (Expression) this.visit(ctx.expression());
            return new LetStatement(identificator, expression);
        } else
            return new LetStatement(identificator);
    }

    @Override
    public ExpressionStatement visitExpressionStatement(RustParser.ExpressionStatementContext ctx) {
        if (ctx.expression() != null) {
            Expression expression = (Expression) this.visit(ctx.expression());
            return new ExpressionStatement(expression);
        }
        if (ctx.expressionWithBlock() != null) {
            ExpressionWithBlock expressionWithBlock = (ExpressionWithBlock) this.visit(ctx.expressionWithBlock());
            return new ExpressionStatement(expressionWithBlock);
        }
        return null;
    }

    @Override
    public Expression visitExpressionWithBlock_(RustParser.ExpressionWithBlock_Context ctx) {
        return new Expression((ExpressionWithBlock) visit(ctx.expressionWithBlock()), ctx.getStart().getLine());
    }

    @Override
    public ExpressionWithBlock visitExpressionWithBlock(RustParser.ExpressionWithBlockContext ctx) {
        if (ctx.ifExpression() != null) {
            IfExpression ifExpression = (IfExpression) this.visit(ctx.ifExpression());
            return new ExpressionWithBlock(ifExpression);
        } else {
            LoopExpression loopExpression = (LoopExpression) this.visit(ctx.loopExpression());
            return new ExpressionWithBlock(loopExpression);
        }
    }

    @Override
    public LoopExpression visitLoopExpression(RustParser.LoopExpressionContext ctx) {
        if (ctx.infiniteLoopExpression() != null) {
            InfiniteLoopExpression infiniteLoopExpression = (InfiniteLoopExpression) this.visit(ctx.infiniteLoopExpression());
            return new LoopExpression(infiniteLoopExpression);
        } else {
            PredicateLoopExpression predicateLoopExpression = (PredicateLoopExpression) this.visit(ctx.predicateLoopExpression());
            return new LoopExpression(predicateLoopExpression);
        }
    }

    @Override
    public InfiniteLoopExpression visitInfiniteLoopExpression(RustParser.InfiniteLoopExpressionContext ctx) {
        return new InfiniteLoopExpression((BlockExpression) this.visit(ctx.blockExpression()));
    }

    @Override
    public PredicateLoopExpression visitPredicateLoopExpression(RustParser.PredicateLoopExpressionContext ctx) {
        return new PredicateLoopExpression((Expression) this.visit(ctx.expression()), (BlockExpression) this.visit(ctx.blockExpression()));
    }

    @Override
    public IfExpression visitIfExpression(RustParser.IfExpressionContext ctx) {
        Expression expression = (Expression) this.visit(ctx.expression());
        BlockExpression ifBlockExpression = (BlockExpression) this.visit(ctx.blockExpression(0));

        if (ctx.blockExpression(1) == null && ctx.ifExpression() == null)
            return new IfExpression(expression, ifBlockExpression);
        else if (ctx.blockExpression(1) != null) {
            BlockExpression elseBlockExpression = (BlockExpression) this.visit(ctx.blockExpression(1));
            return new IfExpression(expression, ifBlockExpression, elseBlockExpression);
        } else {
            IfExpression ifExpression = (IfExpression) this.visit(ctx.ifExpression());
            return new IfExpression(expression, ifBlockExpression, ifExpression);
        }
    }

    @Override
    public Expression visitArrayExpression(RustParser.ArrayExpressionContext ctx) {
        ArrayList<Parameter> parameters = new ArrayList<>();
        if (ctx.parameter() != null) {
            for (int i = 0; i < ctx.parameter().size(); i++) {
                parameters.add((Parameter) this.visit(ctx.parameter(i)));
            }
        }
        return new Expression(new ArrayExpression(parameters), ctx.getStart().getLine());
    }

    @Override
    public ArrayType visitArraytype(RustParser.ArraytypeContext ctx) {
        return new ArrayType((Type) this.visit(ctx.type()), ctx.INT().getText());
    }

    @Override
    public Parameter visitParameter(RustParser.ParameterContext ctx) {
        if (ctx.pathExpression() != null) {
            return new Parameter((PathExpression) this.visit(ctx.pathExpression()));
        } else {
            return new Parameter((LiteralExpression) this.visit(ctx.literalExpression()));
        }
    }
}
