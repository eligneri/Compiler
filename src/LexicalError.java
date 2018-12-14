/*
 *This class is for errors specific to lexing
 */
public class LexicalError extends CompilerError
{

    public LexicalError(String s)
    {
        super("Lexical error: " + s);
    }
}
