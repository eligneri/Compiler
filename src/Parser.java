import java.util.Arrays;
import java.util.LinkedList;
import java.io.File;

/*
 * Uses a lexer to run through a program and determine if it is a valid program in the language
 */
public class Parser
{
    //Stack implemented as a linkedlist because it makes the printout look better. For some reason a Stack Object will print bottom up
    private LinkedList<String> Stack = new LinkedList<>();
    private ParseTable Table = new ParseTable();
    private Grammar G;
    private Lexer Lex;
    private boolean Verbose = false;
    private SemanticAction Actions = new SemanticAction();

    public Parser(File file, Grammar gram)
    {
        Lex = new Lexer(file);
        G = gram;
        Stack.push("ENDOFFILE");
        Stack.push("<Goal>");
    }

    //Keeps track of how many steps the parse has taken
    private int Step = 1;

    //Entry point to this class
    public void Parse()
    {
        Token prevtok = null;
        Token tok = Lex.GetNextToken();
        String key = tok.GetKey();
        while (!Stack.isEmpty())
        {
            try
            {
                if (Verbose)
                {
                    System.out.println("\n>>>  " + Step + "  <<<\nStack ::==> " + DumpStack());
                }
                String stac = Stack.peek();

                //Case 1: The key equals what is expected, pop the stack off and get a new token

                if (key.equals(stac.toUpperCase()))
                {
                    if (Verbose)
                    {
                        System.out.println("Popped " + stac + " with token " + key + " *MATCH* {consume token}");
                    }
                    Stack.pop();
                    prevtok = tok;
                    tok = Lex.GetNextToken();
                    key = tok.GetKey();
                }

                //Case 2: Pop a nonterminal off the stack, push the production rules based on parse table

                else if (IsNonTerminal(stac))
                {
                    if (Verbose)
                    {
                        System.out.print("Popped " + stac + " with token " + key + " *PUSH* ");
                    }
                    int i = GetRule(key, stac);
                    if (i == 999)
                    {
                        throw new ParseError("Invalid Symbol. Unexpected " + tok.GetSymbol() + " on line " +
                                tok.GetLine());

                    }
                    Stack.pop();
                    StackAdd(i);
                }

                //Case 3: Pop a number corresponding to a semantic action, execute semaction action

                else if (IsSemantic(stac))
                {
                    if (Verbose)
                    {
                        System.out.println("Popped " + stac + " with token " + key + " *SEMANTIC ACTION* [" +
                                stac.substring(1) + "]");
                    }
                    Actions.Execute(stac.substring(1), prevtok);
                    Stack.pop();
                }

                //Case 4: None of the above, throw an error.

                else if (!IsNonTerminal(stac))
                {
                    throw new ParseError("Invalid Symbol. Expected " + stac + " on line " + tok.GetLine() +
                            " instead of " + tok.GetSymbol());
                }
                Step++;
            } catch (ParseError e)
            {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }

        System.out.println("\nParse Accepted");
        Actions.IntermediateCodePrint();
    }

    //checks if string is a non terminal based on conventions of Backus-Naur
    private boolean IsNonTerminal(String s)
    {
        return s.charAt(0) == '<';
    }

    //checks if string is a semantic action based on all actions starting with symbol '#'
    private boolean IsSemantic(String s)
    {
        return s.charAt(0) == '#';
    }

    //returns the production rule based on parse table
    private int GetRule(String key, String stac)
    {
        return Table.GetValue(key, stac);
    }

    //adds production rules to the stack
    private void StackAdd(int i)
    {
        if (i > 0)
        {
            String first = G.GetProduction(i);
            String[] productions = first.split("\\s+");
            for (int j = productions.length - 1; j >= 0; j--)
            {
                Stack.push(productions[j]);
            }
            if (Verbose)
            {
                System.out.print(Arrays.toString(productions) + "\n");
            }
        } else
        {
            if (Verbose)
            {
                System.out.print(" *EPSILON*\n");
            }
        }
    }

    //prints the stack
    private String DumpStack()
    {
        return Stack.toString();
    }

    //toggles debugging information
    public void SetVerbose(boolean verbose)
    {
        this.Verbose = verbose;
        Actions.SetVerbose(verbose);
    }

}


