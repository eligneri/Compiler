import java.io.File;

public class CompilerTest {
    //for testing, pls delete
    public static void main(String args[]){
        File file = new File("/Users/Ellis/Downloads/lextest_2.txt");
        Lexer lex = new Lexer(file);
        Token token = new Token(null,null);
        Token end = new Token("ENDOFFILE", null);
        while(!token.equals(end)){
            token = lex.getNextToken();
            System.out.println(token);
        }
        //lex.
    }
}
