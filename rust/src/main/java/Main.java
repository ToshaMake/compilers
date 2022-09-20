import Node.Program;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        if (args == null || args.length < 1) {
            System.err.println("Name required");
            System.exit(1);
        }
        File codeDir = new File("code");
        if (!codeDir.exists())
            codeDir.mkdirs();
        File jsonDir = new File("json");
        if (!jsonDir.exists())
            jsonDir.mkdirs();
        try {
            RustLexer lexer = new RustLexer(CharStreams.fromFileName(args[0]));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            if (args.length == 2) {
                if (args[1].equals("--dump-tokens")) {
                    print(tokens, lexer);
                    print(tokens, lexer);
                }
            }

            RustParser parser = new RustParser(tokens);
            ParseTree tree = parser.program();
            Visitor visitor = new Visitor();
            Program ast = (Program) visitor.visit(tree);
            ASTVisitor astVisitor = new ASTVisitor();
            astVisitor.visit(ast);

            if (args.length == 2) {
                if (args[1].equals("--dump-ast")) {
                    String rust = ".rs";
                    String json = ".json";
                    String name = args[0].toString();
                    name.substring(name.lastIndexOf('/') + 1);
                    name = name.replace("example", "json");
                    name = name.replace(rust, json);
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(name), ast);
                }

                if (args[1].equals("--dump-asm")) {
                    System.out.println(astVisitor.getCode());
                }
            }
            String rust = ".rs";
            String ll = ".ll";
            String name = args[0].toString();
            name = name.replace("example", "code");
            name.substring(name.lastIndexOf('/') + 1);
            name = name.replace(rust, ll);
            FileWriter file = new FileWriter(name, false);
            file.write(astVisitor.getCode());
            file.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void print(CommonTokenStream tokens, RustLexer lexer) {
        for (int i = 1; i <= tokens.getNumberOfOnChannelTokens(); i++) {
            System.out.println("Loc=<" + tokens.LT(i).getLine() + ":" + tokens.LT(i).getCharPositionInLine() + ">   " +
                    lexer.getVocabulary().getSymbolicName(tokens.LT(i).getType()) + "'" + tokens.LT(i).getText() + "'");
        }
    }
}
