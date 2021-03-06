/**
 * Created by jo930_000 on 2016-11-06.
 */
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class TestMiniC {
    public static void main(String[] args) throws Exception{
        MiniCLexer lexer = new MiniCLexer((new ANTLRFileStream("inputtest.c")));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniCParser parser = new MiniCParser(tokens);
        ParseTree tree = parser.program();

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new MiniCPrintListener(), tree);
    }
}

