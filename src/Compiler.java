import java.io.File;

/*
 * This class
 */
public class Compiler
{
    public static void main(String[] args)
    {
        File file = new File("./ultimate.pas");
        Grammar g = new Grammar(new File("./grammar.txt"));
        Parser p = new Parser(file, g);
        p.SetVerbose(false);
        p.Parse();
    }
}
