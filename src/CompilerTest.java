import java.io.File;

public class CompilerTest {
    public static void main(String args[]){
        /*File file = new File("/Users/Ellis/IdeaProjects/Compiler/Lexer_Test.txt");
        Lexer lex = new Lexer(file);
        Token token = new Token(null,null);
        Token end = new Token("ENDOFFILE", null);
        while(!token.equals(end)){
            token = lex.getNextToken();
            System.out.println(token);
        }*/

       File file = new File("/Users/Ellis/IdeaProjects/Compiler/simple.txt");
       Parser p = new Parser(file);
       p.parse();
    }
}
