import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;


import static org.antlr.v4.runtime.CharStreams.fromStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {

    RustParser getParser(String txt) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(txt.getBytes());
        CharStream token = null;

        try {
            token = fromStream(inputStream);
        } catch (IOException e) {
            System.out.println("error");
        }

        RustLexer lexer = new RustLexer(token);
        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
        RustParser parser = new RustParser(commonTokenStream);
        return parser;
    }

    @Test
    void FuncDef() {
        RustParser result = getParser("fn main(){println!()}");
        assertEquals(true, result.functionDefinition().getText().equals("fnmain(){println!()}"));
    }

    @Test
    void ifExpr() {
        RustParser result = getParser("if (a<2) {a}");
        assertEquals(true, result.ifExpression().getText().equals("if(a<2){a}"));
    }

    @Test
    void While() {
        RustParser result = getParser("while true{}");
        assertEquals(true, result.predicateLoopExpression().getText().equals("whiletrue{}"));
    }
    @Test
    void Block() {
        RustParser result = getParser("{let a: i32 = 4;}");
        assertEquals(true, result.blockExpression().getText().equals("{leta:i32=4;}"));
    }
}