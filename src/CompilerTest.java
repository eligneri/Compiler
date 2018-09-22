import java.io.File;

public class CompilerTest {
    public static void main(String args[]){
        File file = new File("/home/eligneri/cs331/Lexer_Test.txt");
        Lexer lex = new Lexer(file);
        Token token = new Token(null,null);
        Token end = new Token("ENDOFFILE", null);
        while(!token.equals(end)){
            token = lex.getNextToken();
            System.out.println(token);
        }
    }
}
