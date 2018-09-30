public class ParseError extends CompilerError {

    public ParseError(String s){
        super("\nParse Error: " + s);
    }
}
