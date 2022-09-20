import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.antlr.v4.runtime.CharStreams.fromStream;


public class LexerTest {

    List<Token> getTokens(String txt) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(txt.getBytes());
        CharStream token;

        try {
            token = fromStream(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        RustLexer lexer = new RustLexer(token);
        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
        commonTokenStream.fill();
        return commonTokenStream.getTokens();
    }

    @Test
    void TokenMain() {
        List<Token> arrayList = getTokens("fn main(){}");

        List<Integer> tokenList = new ArrayList<>();
        tokenList.add(RustLexer.KW_FN);
        tokenList.add(RustLexer.ID);
        tokenList.add(RustLexer.LPAREN);
        tokenList.add(RustLexer.RPAREN);
        tokenList.add(RustLexer.LCURLYBRACE);
        tokenList.add(RustLexer.RCURLYBRACE);
        tokenList.add(RustLexer.EOF);

        for (int i = 0; i < arrayList.size(); i++) {
            assertEquals(tokenList.get(i), arrayList.get(i).getType());
        }
    }

    @Test
    void TokenLet() {
        List<Token> arrayList = getTokens("let a: i16 = 5");

        List<Integer> tokenList = new ArrayList<>();
        tokenList.add(RustLexer.KW_LET);
        tokenList.add(RustLexer.ID);
        tokenList.add(RustLexer.COLON);
        tokenList.add(RustLexer.INT_SUFFIX);
        tokenList.add(RustLexer.EQ);
        tokenList.add(RustLexer.INT);
        tokenList.add(RustLexer.EOF);

        for (int i = 0; i < arrayList.size(); i++) {
            assertEquals(tokenList.get(i), arrayList.get(i).getType());
        }
    }
    @Test
    void TokenString() {
        List<Token> arrayList = getTokens("\"Hello, world!\"");

        List<Integer> tokenList = new ArrayList<>();

        tokenList.add(RustLexer.STRING);
        tokenList.add(RustLexer.EOF);

        for (int i = 0; i < arrayList.size(); i++) {
            assertEquals(tokenList.get(i), arrayList.get(i).getType());
        }
    }
    @Test
    void TokenKeyWord() {
        List<Token> arrayList = getTokens("let as return continue");

        List<Integer> tokenList = new ArrayList<>();
        tokenList.add(RustLexer.KW_LET);
        tokenList.add(RustLexer.KW_AS);
        tokenList.add(RustLexer.KW_RETURN);
        tokenList.add(RustLexer.KW_CONTINUE);
        tokenList.add(RustLexer.EOF);

        for (int i = 0; i < arrayList.size(); i++) {
            assertEquals(tokenList.get(i), arrayList.get(i).getType());
        }
    }
}
