/*
 * This class is for errors specific to parsing
 */
public class ParseError extends CompilerError
{

    public ParseError(String s)
    {
        super("\nParse Error: " + s);
    }
}
