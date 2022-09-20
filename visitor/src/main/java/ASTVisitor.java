import Node.*;
import symtable.SymTable;
import symtable.Symbol;
import symtable.scope.LocalScope;
import symtable.scope.Scope;
import symtable.types.MethodSymbol;
import symtable.types.VariableSymbol;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ASTVisitor {
    public symtable.SymTable SymTable = new SymTable();

    private Scope currentScope;

    private String func;

    private StringBuilder code = new StringBuilder();

    public String getCode() {
        return String.valueOf(code);
    }

    Map<String, String> codeGen = new TreeMap<String, String>();
    Map<String, Integer> stringSize = new TreeMap<>();

    Map<String, ArrayList<String>> funcParam = new TreeMap<>();


    public void visit(Program program) {
        currentScope = SymTable.globals;
        code.append("target triple = \"x86_64-pc-linux-gnu\"\n\n");
        code.insert(38, "declare dso_local i32 @__isoc99_scanf(i8*, ...)\n");
        code.insert(38, "declare i32 @printf(i8*, ...)\n");
        code.insert(38, "declare void @llvm.memcpy.p0i8.p0i8.i64(i8* noalias nocapture writeonly, i8* noalias nocapture readonly, i64, i1 immarg)\n");
        for (int i = 0; i < program.getFunctionDefinitions().size(); i++) {
            visit(program.getFunctionDefinitions().get(i));
        }
    }

    private int number = 1;
    private int strnum = 1;
    private int array = 1;

    private void visit(FunctionDefinition functionDefinition) {
        String id = functionDefinition.getIdentificator().getText();
        MethodSymbol methodSymbol;
        if (functionDefinition.getFunctionReturnType() != null) {
            String type = functionDefinition.getFunctionReturnType().getType().getText();
            func = type;
            methodSymbol = new MethodSymbol(id, type, currentScope);
        } else {
            methodSymbol = new MethodSymbol(id, currentScope);
        }
        currentScope.define(methodSymbol);
        currentScope = new LocalScope(currentScope);
        ArrayList<String> parameters = new ArrayList<>();
        if (functionDefinition.getFunctionParameters() != null) {
            code.append(funcDefBegin(id, func, functionDefinition.getFunctionParameters().getFunctionParameters()));
            for (FunctionParameter parameter : functionDefinition.getFunctionParameters().getFunctionParameters()) {
                currentScope.define(new VariableSymbol(parameter.getIdentificator().getText(), parameter.getType().getText()));
                code.append(alloca(parameter.getType().getText()));
                code.append(store("%" + parameter.getIdentificator().getText(), parameter.getType().getText(), "%tmp" + number));
                codeGen.put(parameter.getIdentificator().getText(), "%tmp" + number);
                number++;
                parameters.add(parameter.getType().getText());
            }
        } else {
            code.append(funcDefBegin(id, func, null));
        }
        if (functionDefinition.getBlockExpression() != null) {
            visit(functionDefinition.getBlockExpression());
        }
        funcParam.put(id, parameters);
        currentScope = currentScope.getEnclosingScope();
        func = null;
    }

    private void visit(BlockExpression blockExpression) {
        if (blockExpression.getStatements() != null) {
            visit(blockExpression.getStatements());
        }
    }

    private void visit(Statements statements) {
        if (statements.getStatements() != null) {
            for (Statement statement : statements.getStatements()) {
                visit(statement);
            }
            if (statements.getExpression() != null) {
                visit(statements.getExpression());
            }
        } else {
            if (statements.getExpression() != null) {
                visit(statements.getExpression());
            }
        }
    }

    private void visit(Statement statement) {
        if (statement.getExpressionStatement() != null) {
            visit(statement.getExpressionStatement());
        } else if (statement.getLetStatement() != null) {
            visit(statement.getLetStatement());
        }
    }

    private void visit(Expression expression) {
        if (expression.getAssignmentExpression() != null) {
            visit(expression.getAssignmentExpression());
            getType(expression);
            getaddr(expression);
        }
        if (expression.getPathExpression() != null) {
            visit(expression.getPathExpression());
            getType(expression);
        }
        if (expression.getExpressionWithBlock() != null) {
            visit(expression.getExpressionWithBlock());
        }
        if (expression.getCallExpression() != null) {
            visit(expression.getCallExpression());
        }
        if (expression.getIndexExpression() != null) {
            visit(expression.getIndexExpression());
        }
        if (expression.getNegationExpression() != null) {
            visit(expression.getNegationExpression());
        }
        if (expression.getArithmeticOrLogicalExpression() != null) {
            visit(expression.getArithmeticOrLogicalExpression());
            getType(expression);
        }
        if (expression.getComparisonExpression() != null) {
            visit(expression.getComparisonExpression());
            getType(expression);
        }
        if (expression.getCompoundAssignmentExpression() != null) {
            visit(expression.getCompoundAssignmentExpression());
            getType(expression);
        }
        if (expression.getContinueBreakReturnExpression() != null) {
            visit(expression.getContinueBreakReturnExpression());
            getType(expression);
        }
        if (expression.getGroupedExpression() != null) {
            visit(expression.getGroupedExpression());
        }
        if (expression.getTypeCastExpression() != null) {
            visit(expression.getTypeCastExpression());
            getType(expression);
        }
        if (expression.getLazyBooleanExpression() != null) {
            visit(expression.getLazyBooleanExpression());
            getType(expression);
        }
    }

    private void visit(CallExpression callExpression) {
        for (Expression expression : callExpression.getExpressions()) {
            visit(expression);
        }
        ArrayList<Expression> expressions = callExpression.getExpressions();
        PathExpression path = expressions.get(0).getPathExpression();
        ArrayList<String> strings = new ArrayList<>();
        if (path != null) {
            if (path.getPath().get(0).getText().equals("println!")) {
                int size = 1;
                int count = 0;
                String strvalue = expressions.get(1).getLiteralExpression().getExpr();
                String substr = strvalue;
                for (int i = 0; i < strvalue.length() - 1; i++) {
                    if (strvalue.charAt(i) == '\\') {
                        if (strvalue.charAt(i + 1) == 'n') {
                            size--;
                        }
                    }
                    if (strvalue.charAt(i) == '{') {
                        if (strvalue.charAt(i + 1) == '}') {
                            count++;
                        }
                    }
                }
                substr = substr.replaceAll("[{][}]", "");
                if (count != expressions.size() - 2) {
                    throw new IllegalArgumentException("println!: invalid arguments in " + callExpression.getExpressions().get(0).getPosition() + " line");
                }
                size += substr.length();
                for (int i = 2; i < expressions.size(); i++) {
                    Expression expr = expressions.get(i);
                    if (expr.getPathExpression() != null) {
                        String type = getType(expr);
                        String addr = getaddr(expr);
                        if (type.equals("string")) {
                            type = "i8*";
                        } else if (!type.equals("i32")) {
                            throw new IllegalArgumentException("println!: invalid parameter in " + expr.getPosition() + " line");
                        }
                        String text = type + " " + "%tmp" + number;
                        strings.add(text);
                        String regex = "[{][}]";
                        if (type.equals("i32")) {
                            size += 2;
                            strvalue = strvalue.replaceFirst(regex, "%d");
                            code.append(load(addr, type, "%tmp" + number));
                        } else if (type.equals("i8*")) {
                            size += 2;
                            strvalue = strvalue.replaceFirst(regex, "%s");
                            int stingSize = stringSize.get(addr).intValue();
                            code.append(gep(addr, "[" + stingSize + " x i8]", "0"));
                        }
                    } else if (expr.getLiteralExpression() != null) {
                        String type = getType(expr);
                        String addr = getaddr(expr);
                        if (type.equals("string")) {
                            type = "i8*";
                        }
                        String text = type + " " + "%tmp" + number;
                        strings.add(text);
                        String regex = "[{][}]";
                        if (type.equals("i32")) {
                            size += 2;
                            strvalue = strvalue.replaceFirst(regex, "%d");
                            code.append(load(addr, type, "%tmp" + number));
                        } else if (type.equals("i8*")) {
                            size += 2;
                            strvalue = strvalue.replaceFirst(regex, "%s");
                            code.append(gep(addr, "[" + (expr.getLiteralExpression().getExpr().length() + 1) + " x i8]", "0"));
                        }
                    } else if (expr.getIndexExpression() != null) {
                        String type = getType(expr);
                        String addr = getaddr(expr);
                        String text = type + " " + "%tmp" + number;
                        strings.add(text);
                        if (type.equals("string")) {
                            type = "i8*";
                        }
                        String regex = "[{][}]";
                        if (type.equals("i32")) {
                            size += 2;
                            strvalue = strvalue.replaceFirst(regex, "%d");
                            code.append(load(addr, type, "%tmp" + number));
                        } else if (type.equals("i8")) {
                            size += 2;
                            strvalue = strvalue.replaceFirst(regex, "%c");
                            code.append(load(addr, type, "%tmp" + number));
                            code.append("%tmp" + (number + 1) + " = sext i8 " + "%tmp" + number + " to i32\n");
                            number++;
                        }
                    } else {
                        throw new IllegalArgumentException("println!: invalid arguments in " + callExpression.getExpressions().get(0).getPosition() + " line");
                    }
                    number++;
                }

                strvalue = strvalue + "\\00";
                strvalue = strvalue.replace("\\n", "\\0A");
                String str = " = private unnamed_addr constant [" + size + " x i8] c\"" + strvalue + "\"";
                code.insert(38, "@.str." + strnum + str + "\n");
                code.append(printf("@.str." + strnum, strings, size));
                strnum++;
            } else if (path.getPath().get(0).getText().equals("read_line")) {
                if (expressions.size() > 2) {
                    throw new IllegalArgumentException("read_line: to many arguments expected: 1 receive: " + (expressions.size() - 1) + " in " + callExpression.getExpressions().get(0).getPosition() + " line");
                }
                for (int i = 1; i < expressions.size(); i++) {
                    if (expressions.get(i).getPathExpression() != null) {
                        String addr = getaddr(expressions.get(i));
                        String type = getType(expressions.get(i));
                        if (!type.equals("i32")) {
                            throw new IllegalArgumentException("read_line: invalid argument in " + callExpression.getExpressions().get(0).getPosition() + " line\"");
                        }
                        code.insert(38, "@.str." + strnum + " = private unnamed_addr constant [3 x i8] c\"%d\\00\", align 1\n");
                        code.append(read_line("%tmp" + (number - 1), "@.str." + strnum));
                        strnum++;
                    } else {
                        throw new IllegalArgumentException("read_line: illegal arguments in " + callExpression.getExpressions().get(0).getPosition() + " line");
                    }
                }
            }else {
                getaddr(callExpression);
            }
        }
    }

    private void visit(IndexExpression indexExpression) {
        visit(indexExpression.getExpression1());
        visit(indexExpression.getExpression2());
    }

    private void visit(NegationExpression negationExpression) {
        visit(negationExpression.getExpression());
    }

    private void visit(TypeCastExpression typeCastExpression) {
        visit(typeCastExpression.getExpression());
    }

    private void visit(ArithmeticOrLogicalExpression arithmeticOrLogicalExpression) {
        visit(arithmeticOrLogicalExpression.getExpression1());
        visit(arithmeticOrLogicalExpression.getExpression2());
    }

    private void visit(ComparisonExpression comparisonExpression) {
        visit(comparisonExpression.getExpression1());
        visit(comparisonExpression.getExpression2());
    }

    private void visit(LazyBooleanExpression lazyBooleanExpression) {
        visit(lazyBooleanExpression.getExpression1());
        visit(lazyBooleanExpression.getExpression2());
    }

    private void visit(CompoundAssignmentExpression compoundAssignmentExpression) {
        visit(compoundAssignmentExpression.getExpression1());
        visit(compoundAssignmentExpression.getExpression2());
    }

    private void visit(ContinueBreakReturnExpression continueBreakReturnExpression) {
        if (continueBreakReturnExpression.getExpression() != null) {
            visit(continueBreakReturnExpression.getExpression());
        }
        getaddr(continueBreakReturnExpression);
    }

    private void visit(GroupedExpression groupedExpression) {
        visit(groupedExpression.getExpression());
    }

    private void visit(LetStatement letStatement) {
        if (letStatement.getType() != null) {
            currentScope.define(new VariableSymbol(letStatement.getIdentificator().getText(), letStatement.getType().getText()));
        } else {
            currentScope.define(new Symbol(letStatement.getIdentificator().getText()));
        }
        String type1 = letStatement.getType().getText();
        if (letStatement.getExpression() != null) {
            String type2 = getType(letStatement.getExpression());
            if (!type1.equals(type2)) {
                throw new IllegalArgumentException("unsupported operand type for " + "=" + ": " + type1 + " and " + type2 + " in " + letStatement.getExpression().getPosition() + " line");
            }
            visit(letStatement.getExpression());
            getaddr(letStatement.getExpression());

            if (letStatement.getExpression().getArrayExpression() != null) {
                codeGen.put(letStatement.getIdentificator().getText(), "%tmp" + (number - 2));
            } else if (letStatement.getExpression().getLiteralExpression() != null) {
                if (letStatement.getExpression().getLiteralExpression().getType().equals("i32")) {
                    codeGen.put(letStatement.getIdentificator().getText(), "%tmp" + (number - 1));
                } else {
                    codeGen.put(letStatement.getIdentificator().getText(), "%tmp" + (number - 2));
                }
            } else if (letStatement.getExpression().getPathExpression() != null) {
                Symbol symbol = currentScope.resolve(letStatement.getExpression().getPathExpression().getPath().get(0).getText(), letStatement.getExpression().getPosition());
                if (symbol.getType().equals("i32")) {
                    String addr = getaddr(letStatement.getExpression());
                    code.append(alloca("i32"));
                    number++;
                    code.append(load(addr, "i32", "%tmp" + number));
                    code.append(store("%tmp" + number, "i32", "%tmp" + (number - 1)));
                    number++;
                    codeGen.put(letStatement.getIdentificator().getText(), "%tmp" + (number - 2));
                } else if (symbol.getType().equals("string")) {
                    String addr = getaddr(letStatement.getExpression());
                    String retaddr = "%tmp" + number;
                    String type = "[" + stringSize.get(addr) + " x " + "i8" + "]";
                    code.append(alloca(type));
                    number++;
                    code.append(bitcast("%tmp" + (number - 1), type, "i8*"));
                    number++;
                    code.append(bitcast(addr, type, "i8*"));
                    code.append("call void @llvm.memcpy.p0i8.p0i8.i64(i8* " + "%tmp" + (number - 1) + ", i8* " + "%tmp" + number + " , i64 " + stringSize.get(addr) + ", i1 false)\n");
                    number++;
                    number++;
                    array++;
                    stringSize.put(retaddr, stringSize.get(addr));
                    codeGen.put(letStatement.getIdentificator().getText(), retaddr);
                } else {
                    throw new IllegalArgumentException("initialization error in "+letStatement.getExpression().getPosition()+" line");
                }
            } else {
                codeGen.put(letStatement.getIdentificator().getText(), "%tmp" + (number - 1));
            }
        } else {
            throw new IllegalArgumentException("not initialized variable " + letStatement.getIdentificator().getText());
        }
    }

    private void visit(ExpressionStatement expressionStatement) {
        if (expressionStatement.getExpression() != null) {
            visit(expressionStatement.getExpression());
        } else if (expressionStatement.getExpressionWithBlock() != null) {
            visit(expressionStatement.getExpressionWithBlock());
        }
    }

    private void visit(ExpressionWithBlock expressionWithBlock) {
        if (expressionWithBlock.getIfExpression() != null) {
            visit(expressionWithBlock.getIfExpression());
        } else if (expressionWithBlock.getLoopExpression() != null) {
            visit(expressionWithBlock.getLoopExpression());
        }
    }

    private void visit(IfExpression ifExpression) {
        currentScope = new LocalScope(currentScope);
        visit(ifExpression.getExpression());
        String value = getaddr(ifExpression.getExpression());
        String iftrue = "%tmp" + number;
        String ifblock = "tmp" + number + ":\n";
        code.append(ifblock);
        number++;
        visit(ifExpression.getIfBlockExpression());
        String iffalse = "%tmp" + number;
        String elseblock = "tmp" + number + ":\n";
        String brtoout = "tmp" + number + ":";
        code.append(elseblock);
        number++;
        if (ifExpression.getIfExpression() != null) {
            visit(ifExpression.getIfExpression());
        } else if (ifExpression.getElseBlockExpression() != null) {
            visit(ifExpression.getElseBlockExpression());
            elseblock = "tmp" + number + ":\n";
            code.append(brWithoutConditional("%tmp" + number));
            code.append(elseblock);
            number++;
        }

        code.insert(code.indexOf(brtoout), brWithoutConditional("%tmp" + (number - 1)));
        code.insert(code.indexOf(ifblock), brWithConditional(value, iftrue, iffalse));
        currentScope = currentScope.getEnclosingScope();
    }

    private void visit(LoopExpression loopExpression) {
        if (loopExpression.getInfiniteLoopExpression() != null) {
            visit(loopExpression.getInfiniteLoopExpression());
        } else if (loopExpression.getPredicateLoopExpression() != null) {
            visit(loopExpression.getPredicateLoopExpression());
        }
    }

    private void visit(InfiniteLoopExpression infiniteLoopExpression) {
        currentScope = new LocalScope(currentScope);
        visit(infiniteLoopExpression.getBlockExpression());
        currentScope = currentScope.getEnclosingScope();
    }

    private void visit(PredicateLoopExpression predicateLoopExpression) {
        currentScope = new LocalScope(currentScope);
        visit(predicateLoopExpression.getExpression());
        code.append(brWithoutConditional("%tmp" + number));
        String whilebegin = "tmp" + number + ":\n";
        String whileblock = "%tmp" + number;
        code.append(whilebegin);
        number++;
        String value = getaddr(predicateLoopExpression.getExpression());
        String ifblock = "tmp" + number + ":\n";
        String iftrue = "%tmp" + number;
        code.append(ifblock);
        number++;

        visit(predicateLoopExpression.getBlockExpression());
        String iffalse = "%tmp" + number;
        String elseblock = "tmp" + number + ":\n";
        code.append(brWithoutConditional(whileblock));
        code.append(elseblock);
        number++;

        code.insert(code.indexOf(ifblock), brWithConditional(value, iftrue, iffalse));
        currentScope = currentScope.getEnclosingScope();
    }

    private void visit(AssignmentExpression assignmentExpression) {
        visit(assignmentExpression.getExpression1());
        visit(assignmentExpression.getExpression2());
    }

    private void visit(PathExpression pathExpression) {
        for (Identificator id : pathExpression.getPath()) {
            currentScope.resolve(id.getText(), id.getPosition());
        }
    }

    private String getType(Expression expression) {
        if (expression.getLiteralExpression() != null) {
            return expression.getLiteralExpression().getType();
        }
        if (expression.getNegationExpression() != null) {
            return getType(expression.getNegationExpression().getExpression());
        }
        if (expression.getGroupedExpression() != null) {
            return getType(expression.getGroupedExpression().getExpression());
        }
        if (expression.getAssignmentExpression() != null) {
            String type1 = getType(expression.getAssignmentExpression().getExpression1());
            String type2 = getType(expression.getAssignmentExpression().getExpression2());
            String op = expression.getAssignmentExpression().getOperation();
            if (!type1.equals(type2)) {
                throw new IllegalArgumentException("unsupported operand type for " + op + ": " + type1 + " and " + type2 + " in " + expression.getPosition() + " line");
            } else {
                return type1;
            }
        }
        if (expression.getArithmeticOrLogicalExpression() != null) {
            String type1 = getType(expression.getArithmeticOrLogicalExpression().getExpression1());
            String type2 = getType(expression.getArithmeticOrLogicalExpression().getExpression2());
            String op = expression.getArithmeticOrLogicalExpression().getOperation();
            if (!type1.equals(type2)) {
                throw new IllegalArgumentException("unsupported operand type for " + op + ": " + type1 + " and " + type2 + " in " + expression.getPosition() + " line");
            } else {
                return type1;
            }
        }
        if (expression.getPathExpression() != null) {
            Identificator id = expression.getPathExpression().getPath().get(0);
            Symbol symbol = currentScope.resolve(id.getText(), id.getPosition());
            return symbol.getType();
        }
        if (expression.getComparisonExpression() != null) {
            String type1 = getType(expression.getComparisonExpression().getExpression1());
            String type2 = getType(expression.getComparisonExpression().getExpression2());
            String op = expression.getComparisonExpression().getComparisonOperator().getOp();
            if (!type1.equals(type2)) {
                throw new IllegalArgumentException("unsupported operand type for " + op + ": " + type1 + " and " + type2 + " in " + expression.getPosition() + " line");
            } else {
                return type1;
            }
        }
        if (expression.getCompoundAssignmentExpression() != null) {
            String type1 = getType(expression.getCompoundAssignmentExpression().getExpression1());
            String type2 = getType(expression.getCompoundAssignmentExpression().getExpression2());
            String op = expression.getCompoundAssignmentExpression().getOperation().getText();
            if (!type1.equals(type2)) {
                throw new IllegalArgumentException("unsupported operand type for " + op + ": " + type1 + " and " + type2 + " in " + expression.getPosition() + " line");
            } else {
                return type1;
            }
        }
        if (expression.getContinueBreakReturnExpression() != null) {
            String type = getType(expression.getContinueBreakReturnExpression().getExpression());
            if (!func.equals(type)) {
                throw new IllegalArgumentException("invalid return type");
            }
            return type;
        }
        if (expression.getTypeCastExpression() != null) {
            String type = getType(expression.getTypeCastExpression().getExpression());
            if (type.equals("string") && expression.getTypeCastExpression().getType().getText().equals("int")) {
                throw new IllegalArgumentException("Can not convert string to int in " + expression.getPosition() + " line");
            }
            if (type.equals("int") && expression.getTypeCastExpression().getType().getText().equals("string")) {
                throw new IllegalArgumentException("Can not convert int to string in " + expression.getPosition() + " line");
            }
            return expression.getTypeCastExpression().getType().getText();
        }
        if (expression.getArrayExpression() != null) {
            ArrayList<String> types = new ArrayList<>();
            for (Parameter parameter : expression.getArrayExpression().getExpressions()) {
                if (parameter.getLiteralExpression() != null) {
                    types.add(parameter.getLiteralExpression().getType());
                }
                if (parameter.getPathExpression() != null) {
                    Identificator id = parameter.getPathExpression().getPath().get(0);
                    Symbol symbol = currentScope.resolve(id.getText(), id.getPosition());
                    types.add(symbol.getType());
                }
            }
            for (int i = 1; i < types.size(); i++) {
                if (!types.get(0).equals(types.get(i))) {
                    throw new IllegalArgumentException("invalid array parametr");
                }
            }
            return "[" + types.get(0) + ";" + types.size() + "]";
        }
        if (expression.getIndexExpression() != null) {
            if (expression.getIndexExpression().getExpression1().getPathExpression() != null) {
                String type = getType(expression.getIndexExpression().getExpression1());
                if (type.equals("string")) {
                    return "i8";
                } else {
                    return "i32";
                }
            } else {
                throw new IllegalArgumentException("invalid index expression in " + expression.getPosition() + " line");
            }
        }
        if (expression.getCallExpression() != null) {
            if (expression.getCallExpression().getExpressions().get(0).getPathExpression() == null) {
                throw new IllegalArgumentException("invalid function name");
            }
            if (!expression.getCallExpression().getExpressions().get(0).getPathExpression().getPath().get(0).getText().equals("println!")) {
                Symbol symbol = currentScope.resolve(expression.getCallExpression().getExpressions().get(0).getPathExpression().getPath().get(0).getText(), expression.getPosition());
                return symbol.getType();
            }
            return null;
        }
        return null;
    }

    private String alloca(String type) {
        String text = "%tmp" + number + " = alloca " + type + "\n";
        return text;
    }

    private String store(String value, String type, String addr) {
        String text = "store " + type + " " + value + ", " + type + "* " + addr + "\n";
        return text;
    }

    private String load(String value, String type, String addr) {
        String text = addr + " = load " + type + ", " + type + "* " + value + "\n";
        return text;
    }

    private String printf_int(String type) {
        int size = 1;
        if (type.equals("i32")) {
            size = 4;
        } else if (type.equals("i16")) {
            size = 2;
        } else if (type.equals("i8")) {
            size = 1;
        }
        String text = "declare i32 @printf(i8*, ...) \n " +
                "@.str = private unnamed_addr constant [" + size + " x i8] c\"%d\\0A\\00\"" + "\n\n" +
                "define void @println(" + type + " %x) {\n" +
                "  %rv = call i32 (i8*, ...) @printf(i8* getelementptr ([" + size + " x i8], [" + size + " x i8]* @.str, i64 0, i64 0)," + type + "%x)\n" +
                "  ret void\n" +
                "}\n\n";
        return text;
    }

    private String printf(String str, ArrayList<String> strings, int size) {
        String text = "call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([" + size + " x i8], [" + size + " x i8]* " + str + ", i64 0, i64 0)";
        for (int i = 0; i < strings.size(); i++) {
            if (i == 0) {
                text = text + ", ";
            }
            text = text + strings.get(i);
            if (i != strings.size() - 1) {
                text = text + ", ";
            }
        }
        text = text + ")\n";
        return text;
    }

    private String funcDefBegin(String name, String type, ArrayList<FunctionParameter> parametrs) {
        String text = "define " + type + " @" + name + "(";
        if (parametrs != null) {
            for (int i = 0; i < parametrs.size(); i++) {
                text = text + parametrs.get(i).getType().getText() + " %" + parametrs.get(i).getIdentificator().getText();
                if (i != parametrs.size() - 1) {
                    text = text + ", ";
                }
            }
        }
        text = text + ") { \n";
        return text;
    }

    private String funcDefEnd(String value, String type) {
        String text = "ret " + type + " " + value + "\n}\n\n";
        return text;
    }

    private String add(String addr1, String addr2, String type) {
        String text = "%tmp" + number + " = add nsw " + type + " " + addr1 + ", " + addr2 + "\n";
        return text;
    }

    private String sub(String addr1, String addr2, String type) {
        String text = "%tmp" + number + " = sub nsw " + type + " " + addr1 + ", " + addr2 + "\n";
        return text;
    }

    private String mul(String addr1, String addr2, String type) {
        String text = "%tmp" + number + " = mul nsw " + type + " " + addr1 + ", " + addr2 + "\n";
        return text;
    }

    private String div(String addr1, String addr2, String type) {
        String text = "%tmp" + number + " = sdiv " + type + " " + addr1 + ", " + addr2 + "\n";
        return text;
    }

    private String srem(String addr1, String addr2, String type) {
        String text = "%tmp" + number + " = srem " + type + " " + addr1 + ", " + addr2 + "\n";
        return text;
    }

    private String icmp(String command, String addr1, String addr2, String type) {
        String text = "%tmp" + number + " = icmp " + command + " " + type + " " + addr1 + ", " + addr2 + "\n";
        return text;
    }

    private String brWithConditional(String value, String iftrue, String iffalse) {
        String text = "br i1 " + value + ", label " + iftrue + ", label " + iffalse + "\n";
        return text;
    }

    private String brWithoutConditional(String dest) {
        String text = "br label " + dest + "\n";
        return text;
    }

    private String bitcast(String value, String valueType, String toType) {
        String text = "%tmp" + number + " = bitcast " + valueType + "* " + value + " to " + toType + "\n";
        return text;
    }

    private String gep(String value, String type, String index) {
        String text = "%tmp" + number + " = getelementptr inbounds " + type + ", " + type + "* " + value + ", i32 0, i32 " + index + "\n";
        return text;
    }

    private String call(String type, String name, ArrayList<String> strings) {
        String text = "%tmp" + number + " = call " + type + " @" + name + "(";
        for (int i = 0; i < strings.size(); i++) {
            text = text + strings.get(i);
            if (i != strings.size() - 1) {
                text = text + ", ";
            }
        }
        text = text + ")\n";
        return text;
    }

    private String read_line(String addr, String str) {
        String text = "call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* " + str + ", i64 0, i64 0), i32* " + addr + ")\n";
        return text;
    }

    private String getaddr(Expression expression) {
        if (expression.getLiteralExpression() != null) {
            String addr = getaddr(expression.getLiteralExpression());
            return addr;
        }
        if (expression.getArithmeticOrLogicalExpression() != null) {
            String type = getType(expression.getArithmeticOrLogicalExpression().getExpression1());
            String addr1 = getaddr(expression.getArithmeticOrLogicalExpression().getExpression1());
            String addr2 = getaddr(expression.getArithmeticOrLogicalExpression().getExpression2());
            String op = expression.getArithmeticOrLogicalExpression().getOperation();
            String newAddr1 = "%tmp" + number;
            if (type.equals("i32")){
                code.append(load(addr1, type, newAddr1));
                number++;
                String newAddr2 = "%tmp" + number;
                code.append(load(addr2, type, newAddr2));
                number++;
                if (op.equals("+")) {
                    code.append(add(newAddr1, newAddr2, type));
                } else if (op.equals("-")) {
                    code.append(sub(newAddr1, newAddr2, type));
                } else if (op.equals("*")) {
                    code.append(mul(newAddr1, newAddr2, type));
                } else if (op.equals("/")) {
                    code.append(div(newAddr1, newAddr2, type));
                } else if (op.equals("%")) {
                    code.append(srem(newAddr1, newAddr2, type));
                }
                number++;
                code.append(alloca(type));
                code.append(store("%tmp" + (number - 1), type, "%tmp" + number));
            }else {
                throw new IllegalArgumentException("invalid operation in " + expression.getPosition()+ " line");
            }
            return "%tmp" + number++;
        }
        if (expression.getNegationExpression() != null) {
            String addr = getaddr(expression.getNegationExpression().getExpression());
            String type = getType(expression.getNegationExpression().getExpression());
            String newAddr1 = "%tmp" + number;
            number++;
            code.append(load(addr, type, newAddr1));
            code.append(sub("0", newAddr1, type));
            number++;
            code.append(alloca(type));
            code.append(store("%tmp" + (number - 1), type, "%tmp" + number));
            return "%tmp" + number++;
        }
        if (expression.getPathExpression() != null) {
            return codeGen.get(expression.getPathExpression().getPath().get(0).getText());
        }
        if (expression.getAssignmentExpression() != null) {
            String type = getType(expression.getAssignmentExpression().getExpression1());
            String addr1 = getaddr(expression.getAssignmentExpression().getExpression1());
            String addr2 = getaddr(expression.getAssignmentExpression().getExpression2());
            if (type.equals("string")) {
                type = "[" + stringSize.get(addr2) + " x i8]";
                code.append(bitcast(addr2, type, "i8*"));
                number++;
                type = "[" + stringSize.get(addr1) + " x i8]";
                code.append(bitcast(addr1, type, "i8*"));
                code.append("call void @llvm.memcpy.p0i8.p0i8.i64(i8* " + "%tmp" + number + ", i8* " + "%tmp" + (number - 1) + " , i64 " + stringSize.get(addr2) + ", i1 false)\n");
                number++;
            } else if(type.equals("i32") || type.equals("i8")){
                String newAddr2 = "%tmp" + number;
                code.append(load(addr2, type, newAddr2));
                number++;
                code.append(store(newAddr2, type, addr1));
            }else {
                throw new IllegalArgumentException("can not reassign array in " + expression.getPosition()+" line");
            }
            return addr1;
        }
        if (expression.getGroupedExpression() != null) {
            return getaddr(expression.getGroupedExpression().getExpression());
        }
        if (expression.getComparisonExpression() != null) {
            String type = getType(expression.getComparisonExpression().getExpression1());
            String addr1 = getaddr(expression.getComparisonExpression().getExpression1());
            String addr2 = getaddr(expression.getComparisonExpression().getExpression2());
            String op = expression.getComparisonExpression().getComparisonOperator().getOp();
            String newAddr1 = "%tmp" + number;
            code.append(load(addr1, type, newAddr1));
            number++;
            String newAddr2 = "%tmp" + number;
            code.append(load(addr2, type, newAddr2));
            number++;
            if (op.equals("==")) {
                code.append(icmp("eq", newAddr1, newAddr2, type));
            } else if (op.equals("!=")) {
                code.append(icmp("ne", newAddr1, newAddr2, type));
            } else if (op.equals("<")) {
                code.append(icmp("slt", newAddr1, newAddr2, type));
            } else if (op.equals("<=")) {
                code.append(icmp("sle", newAddr1, newAddr2, type));
            } else if (op.equals(">")) {
                code.append(icmp("sgt", newAddr1, newAddr2, type));
            } else if (op.equals(">=")) {
                code.append(icmp("sge", newAddr1, newAddr2, type));
            }
            return "%tmp" + number++;
        }
        if (expression.getArrayExpression() != null) {
            String type;
            if (expression.getArrayExpression().getExpressions().get(0).getLiteralExpression() != null) {
                type = expression.getArrayExpression().getExpressions().get(0).getLiteralExpression().getType();
            } else {
                Symbol symbol = currentScope.resolve(expression.getArrayExpression().getExpressions().get(0).getPathExpression().getPath().get(0).getText(), expression.getPosition());
                type = symbol.getType();
            }
            return getaddr(expression.getArrayExpression(), type);
        }

        if (expression.getIndexExpression() != null) {
            String type1 = getType(expression.getIndexExpression().getExpression1());
            String addr = getaddr(expression.getIndexExpression().getExpression1());
            String size;
            String type;
            if (type1.equals("string")) {
                size = String.valueOf(stringSize.get(addr).intValue());
                type = "i8";
            } else {
                size = type1.substring(5, type1.length() - 1);
                if (type1.equals("[i32;" + size + "]")) {
                    type = "i32";
                } else {
                    throw new IllegalArgumentException("invalid array id in " + expression.getPosition() + " line");
                }
            }
            String type2 = getType(expression.getIndexExpression().getExpression2());
            if (!type2.equals("i32")) {
                throw new IllegalArgumentException("invalid index expression argument in " + expression.getPosition() + " line");
            }
            type = "[" + size + " x " + type + "]";
            String addr1 = getaddr(expression.getIndexExpression().getExpression1());
            String addr2 = getaddr(expression.getIndexExpression().getExpression2());
            String newAddr2 = "%tmp" + number;
            code.append(load(addr2, type2, newAddr2));
            number++;
            code.append(gep(addr1, type, newAddr2));
            return "%tmp" + number++;
        }
        if (expression.getCallExpression() != null) {
            return getaddr(expression.getCallExpression());
        }

        return null;
    }

    private String println(String addr, String type) {
        String text = "call void @println(" + type + " " + addr + ")\n";
        return text;
    }

    private String getaddr(ArrayExpression arrayExpression, String type) {
        String addr = "%tmp" + number;
        String type_ = "[" + arrayExpression.getExpressions().size() + " x " + type + "]";
        code.append(alloca(type_));
        String text = "@__const.main." + array + " = private unnamed_addr constant " + type_ + " [";
        for (int i = 0; i < arrayExpression.getExpressions().size(); i++) {
            if (arrayExpression.getExpressions().get(i).getPathExpression() != null) {
                Symbol symbol = currentScope.resolve(arrayExpression.getExpressions().get(i).getPathExpression().getPath().get(0).getText(), 0);
                text = text + symbol.getType() + " " + symbol.getName();
            }
            if (arrayExpression.getExpressions().get(i).getLiteralExpression() != null) {
                text = text + arrayExpression.getExpressions().get(i).getLiteralExpression().getType() + " " + arrayExpression.getExpressions().get(i).getLiteralExpression().getExpr();
            }
            if (i != arrayExpression.getExpressions().size() - 1) {
                text = text + ", ";
            }
        }
        text = text + "]\n";
        code.insert(38, text);
        number++;
        code.append(bitcast("%tmp" + (number - 1), type_, "i8*"));
        code.append("call void @llvm.memcpy.p0i8.p0i8.i64(i8* " + "%tmp" + number + ", i8* bitcast (" + type_ + "* @__const.main." + array + " to i8*), i64 " + (arrayExpression.getExpressions().size() * 4) + ", i1 false)\n");
        number++;
        array++;
        stringSize.put(addr, arrayExpression.getExpressions().size());
        return addr;
    }

    private String getaddr(LiteralExpression literalExpression) {
        if (literalExpression.getType().equals("i32")) {
            String addr = "%tmp" + number;
            code.append(alloca(literalExpression.getType()));
            code.append(store(literalExpression.getExpr(), literalExpression.getType(), addr));
            number++;
            return addr;
        } else if (literalExpression.getType().equals("string")) {
            String addr = "%tmp" + number;
            String type = "[" + (literalExpression.getExpr().length() + 1) + " x " + "i8" + "]";
            code.append(alloca(type));
            String text = "@__const.main." + array + " = private unnamed_addr constant " + type + " c" + "\"" + literalExpression.getExpr() + "\\00\"\n";
            code.insert(38, text);
            number++;
            code.append(bitcast("%tmp" + (number - 1), type, "i8*"));
            code.append("call void @llvm.memcpy.p0i8.p0i8.i64(i8* " + "%tmp" + number + ", i8* bitcast (" + type + "* @__const.main." + array + " to i8*), i64 " + (literalExpression.getExpr().length() + 1) + ", i1 false)\n");
            number++;
            array++;
            stringSize.put("%tmp" + (number - 2), literalExpression.getExpr().length() + 1);
            return addr;
        }
        return null;
    }

    private String getaddr(ContinueBreakReturnExpression continueBreakReturnExpression) {
        String addr = getaddr(continueBreakReturnExpression.getExpression());
        String type = getType(continueBreakReturnExpression.getExpression());
        if (!type.equals(func)) {
            throw new IllegalArgumentException("invalid return type");
        }
        code.append(load(addr, type, "%tmp" + number));
        code.append(funcDefEnd("%tmp" + number, type));
        number++;
        return null;
    }

    private String getaddr(CallExpression callExpression) {
        ArrayList<Expression> expressions = callExpression.getExpressions();
        PathExpression path = expressions.get(0).getPathExpression();
        ArrayList<String> strings = new ArrayList<>();
        if (path != null) {
            if (path.getPath().get(0).getText().equals("println!")) {
                throw new IllegalArgumentException("invalid operation with println! in " + expressions.get(0).getPosition() + " line");
            } else {
                String id = path.getPath().get(0).getText();
                String type = getType(expressions.get(0));
                ArrayList<String> parametersType = funcParam.get(id);
                if (parametersType.size() != expressions.size()-1){
                    throw new IllegalArgumentException("invalid function argument: expected " + parametersType.size() + " received " + (expressions.size()-1) + " in " + expressions.get(1).getPosition()+ " line");
                }
                for (int i = 1; i < expressions.size(); i++) {
                    Expression expression = expressions.get(i);
                    String type_ = getType(expression);
                    if(!type_.equals(parametersType.get(i-1))){
                        throw new IllegalArgumentException("invalid type parameter in function "+ id + " in "+ expression.getPosition() + " line; expected " + parametersType.get(i-1) + " received " + type_);
                    }
                    String text = type_ + " " + "%tmp" + number;
                    strings.add(text);
                    String addr = getaddr(expression);
                    code.append(load(addr, type, "%tmp" + number));
                    number++;
                }
                code.append(call(type, id, strings));
                number++;
                code.append(alloca(type));
                number++;
                code.append(store("%tmp" + (number - 2), type, "%tmp" + (number - 1)));
                return "%tmp" + (number - 1);
            }
        } else {
            throw new IllegalArgumentException("invalid function id in " + callExpression.getExpressions().get(0).getPosition() + " line");
        }
    }
}