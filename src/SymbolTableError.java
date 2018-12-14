/*
 * This class is for errors specific to symbol tables
 */
public class SymbolTableError extends CompilerError {

    public SymbolTableError(String s){
        super("Symbol Table Error: " + s);
    }
}
