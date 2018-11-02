import java.io.File;

public class CompilerTest {
    public static void main(String args[]){
        /*File file = new File("/Users/Ellis/IdeaProjects/Compiler/Lexer_Test.txt");
        Lexer lex = new Lexer(file);
        Token token = new Token(null,null, 0);
        Token end = new Token("ENDOFFILE", null, 0);
        while(!token.equals(end)){
            token = lex.getNextToken();
            System.out.println(token);
        }*/

       File file = new File("/Users/Ellis/IdeaProjects/Compiler/phase2-1_ns.vas.txt");
       Parser p = new Parser(file);
       p.setVerbose(false);
       p.parse();

      /*SymbolTable table = new SymbolTable(6);
      table.insert(new IODeviceEntry("read"));
      table.insert(new ProcedureEntry("input", 0));
      table.insert(new VariableEntry("a"));
      table.insert(new ArrayEntry("test"));
      table.insert(new ConstantEntry("hello"));
      table.insert(new FunctionEntry("function"));
      table.dumpTable();
      */
    }
}
