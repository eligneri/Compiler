import java.io.*;
import java.util.*;

public class Lexer
{
    //The Lexer class takes a file
    private File file;

    //And then reads it into a string
    public Lexer(File x)
    {
        file = x;
        try
        {
            this.Start();
        } catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

    }

    //Declarations of accepted characters categorized as well as the maximum length of an identifier
    private static final String VALID_CHARS_ALPHA =
            "abcdefghijklmnopqrstuvwxyz";
    private static final String VALID_CHARS_NUMERIC =
            "1234567890";
    private static final String VALID_CHARS_PUNCTUATION =
            ".,;:[]()}{";
    private static final String VALID_CHARS_BLANKS =
            "\t\n ";
    private static final String VALID_CHARS_OPS =
            "<>/*+-=";
    private static final ArrayList<String> Keywords = new ArrayList<>(Arrays.asList("program", "begin", "end"
            , "var", "function", "procedure", "result", "integer", "real", "array", "of", "not", "if", "then", "else", "while", "do"));
    private static final ArrayList<String> KeyOps = new ArrayList<>(Arrays.asList("div", "and", "mod", "or"));
    private static final int IDENTIFIER_MAX_LENGTH = 32;

    //These functions determine whether or not a character belongs to one of the categories of accepted characters
    private static boolean IsAlphaNumeric(char x)
    {
        return IsAlpha(x) || IsNumeric(x);
    }

    private static boolean IsAlpha(char x)
    {
        return VALID_CHARS_ALPHA.indexOf(x) != -1;
    }

    private static boolean IsNumeric(char x)
    {
        return VALID_CHARS_NUMERIC.indexOf(x) != -1;
    }

    private static boolean IsBlank(char x)
    {
        return VALID_CHARS_BLANKS.indexOf(x) != -1;
    }

    private static boolean IsPunc(char x)
    {
        return VALID_CHARS_PUNCTUATION.indexOf(x) != -1;
    }

    private static boolean IsOp(char x)
    {
        return VALID_CHARS_OPS.indexOf(x) != -1;
    }


    //input is the String that holds the file characters
    private String input = "";

    //Store the previous token in order to check it later
    private Token previousToken = new Token(null, null, 0);

    //Reads the input file into a string
    private void Start() throws IOException
    {
        FileReader reader = new FileReader(file);
        int x = reader.read();
        while (x != -1)
        {
            input += (char) x;
            x = reader.read();
        }
        input = input.toLowerCase();
        reader.close();
    }

    //index keeps track of what point in the string we are
    private int Index = 0;

    //Keeps track of what line we are on in the original file
    private int Line = 1;

    //Returns the next char in the string
    private char GetNextChar()
    {
        Index++;
        if (Index > input.length())
        {
            return ' ';
        }
        if (input.charAt(Index - 1) == '\n')
        {
            Line++;
        }
        return input.charAt(Index - 1);
    }

    //used to get back to our initial position after a lookahead
    private void GoBack()
    {
        Index--;
        if (Index >= input.length())
        {
            return;
        }
        if (input.charAt(Index) == '\n')
        {
            Line--;
        }
    }

    //Publicly accessible method for retrieving the next token
    public Token GetNextToken()
    {
        Token token;

        //check to see if we have reached the end of the file
        if (Index >= input.length())
        {
            return new Token("ENDOFFILE", null, Line);
        }

        char c = GetNextChar();

        //scrolls over blanks
        while (IsBlank(c))
        {
            if (Index >= input.length())
            {
                return new Token("ENDOFFILE", null, Line);
            }
            c = GetNextChar();
        }

        //Determine the category of the char c or throw an error if it is not a valid character
        try
        {
            if (IsAlpha(c))
            {
                token = AlphaNFA(c);
            } else if (IsNumeric(c))
            {
                token = NumNFA(c);
            } else if (IsPunc(c))
            {
                token = PuncNFA(c);
            } else if (IsOp(c))
            {
                token = OpNFA(c);
            } else
            {
                throw new LexicalError("Invalid Character");
            }
            previousToken = token;
            return token;
        } catch (LexicalError e)
        {
            System.err.println(e.getMessage() + " on line: " + Line);
            System.exit(1);
        }

        //this should never be reached but java wants a return statement
        return null;
    }

    //determine the token key and value when presented with an alpha numeric character
    private Token AlphaNFA(char c)
    {
        String output = "";
        while (IsAlphaNumeric(c))
        {
            output += c;
            c = GetNextChar();
        }
        GoBack();
        //finds if identifier is a keyword or operator
        if (Keywords.contains(output))
        {
            //goBack();
            return new Token(output.toUpperCase(), null, Line);
        } else if (KeyOps.contains(output))
        {
            //goBack();
            return OpSelector(output);
        }

        //check that the identifier is shorter than the maximum length
        else
        {
            try
            {
                if (output.length() <= IDENTIFIER_MAX_LENGTH)
                {
                    // goBack();
                    return new Token("IDENTIFIER", output, Line);
                } else
                {
                    //goBack();
                    throw new LexicalError("Identifier exceeds maximum length of 32 chars: ");
                }
            } catch (LexicalError e)
            {
                System.err.println((e.getMessage() + " on line " + Line));
                System.exit(1);
            }

        }

        //again, this should never be reached
        return null;
    }

    //method for determining the token type and value when the first char is a number
    private Token NumNFA(char c)
    {
        String value = "";
        //number can be int
        while (IsNumeric(c))
        {
            value += c;
            c = GetNextChar();
        }

        //of the form X.Y
        if (c == '.')
        {
            if (IsDoubleDot())
            {
                GoBack();
                GoBack();
                return new Token("INTCONSTANT", value, Line);
            }
            //make sure the constant follows lexical rules
            try
            {
                c = GetNextChar();
                if (IsNumeric(c))
                {
                    value += '.';
                } else
                {
                    value += '.';
                    throw new LexicalError("Invalid Constant " + value);
                }
            } catch (LexicalError e)
            {
                System.err.println(e.getMessage() + " on line: " + Line);
                System.exit(1);
            }
            while (IsNumeric(c))
            {
                value += c;
                c = GetNextChar();
            }
            //of the form X.Ye(+/-)z
            if (c == 'e')
            {
                return (Exponent(c, value));
            }
            GoBack();
            return new Token("REALCONSTANT", value, Line);
        }
        //or Xe(+/-)Y
        if (c == 'e')
        {
            return Exponent(c, value);
        }

        GoBack();
        return new Token("INTCONSTANT", value, Line);
    }

    //method for determining token type and value when char is punctuation
    private Token PuncNFA(char c)
    {
        //scrolls past comments and returns the next token in the file
        if (c == '{')
        {
            while (c != '}')
            {
                c = GetNextChar();
            }
            return GetNextToken();
        }
        try
        {
            switch (c)
            {
                case '(':
                    return new Token("LPAREN", null, Line);
                case ')':
                    return new Token("RPAREN", null, Line);
                case '[':
                    return new Token("LBRACKET", null, Line);
                case ']':
                    return new Token("RBRACKET", null, Line);
                case ';':
                    return new Token("SEMICOLON", null, Line);
                case ':':
                    if (GetNextChar() == '=')
                    {
                        return new Token("ASSIGNOP", null, Line);
                    } else
                    {
                        GoBack();
                        return new Token("COLON", null, Line);
                    }
                case ',':
                    return new Token("COMMA", null, Line);
                case '.':
                    if (IsDoubleDot())
                    {
                        return new Token("DOUBLEDOT", null, Line);
                    } else
                    {
                        return new Token("ENDMARKER", null, Line);
                    }
                default:
                    throw new LexicalError("Right Brace not allowed");
            }
        } catch (LexicalError e)
        {
            System.err.println(e.getMessage() + " on line: " + Line);
            System.exit(1);
        }
        //should not be reached
        return null;
    }

    //method for determining token type and value when char is an operation
    private Token OpNFA(char c)
    {
        switch (c)
        {
            case '=':
                return new Token("RELOP", 1, Line);
            case '<':
                char x = GetNextChar();
                if (x == '>')
                {
                    return new Token("RELOP", 2, Line);
                } else if (x == '=')
                {

                    return new Token("RELOP", 5, Line);
                } else
                {
                    GoBack();
                    return new Token("RELOP", 3, Line);
                }
            case '>':
                x = GetNextChar();
                if (x == '=')
                {
                    return new Token("RELOP", 6, Line);
                } else
                {
                    GoBack();
                    return new Token("RELOP", 4, Line);
                }
            case '*':
                return new Token("MULOP", 1, Line);
            case '/':
                return new Token("MULOP", 2, Line);
            case '+':
            case '-':
                return UnaryOrAdd(c);
            //should not be reached
            default:
                return new Token(null, null, 0);
        }
    }

    //method for finding value after encountering an e in a constant
    private Token Exponent(char c, String value)
    {
        value += c;
        c = GetNextChar();
        if (c == '-' || c == '+')
        {
            value += c;
            c = GetNextChar();
        }
        try
        {
            if (!IsNumeric(c) && c != '-' && c != '+')
            {
                throw new LexicalError("Invalid Constant " + value);
            }
        } catch (LexicalError e)
        {
            System.err.println(e.getMessage() + " on line: " + Line);
            System.exit(1);
        }
        while (IsNumeric(c))
        {
            value += c;
            c = GetNextChar();
        }
        GoBack();
        return new Token("REALCONSTANT", value, Line);
    }

    //determines if the + and - symbols are ADDOP's or UNARYOP's
    private Token UnaryOrAdd(char c)
    {
        if (previousToken.GetKey() == "RPAREN" || previousToken.GetKey() == "RBRACKET" ||
                previousToken.GetKey() == "IDENTIFIER" || previousToken.GetKey() == "INTCONSTANT" ||
                previousToken.GetKey() == "REALCONSTANT")
        {
            if (c == '+')
            {
                return new Token("ADDOP", 1, Line);
            } else
            {
                return new Token("ADDOP", 2, Line);
            }

        } else
        {
            if (c == '+')
            {
                return new Token("UNARYPLUS", null, Line);
            } else
            {
                return new Token("UNARYMINUS", null, Line);
            }
        }
    }

    //returns the token with integer value for keyword operations
    private Token OpSelector(String input)
    {
        switch (input)
        {
            case "div":
                return new Token("MULOP", 3, Line);
            case "mod":
                return new Token("MULOP", 4, Line);
            case "and":
                return new Token("MULOP", 5, Line);
            case "or":
                return new Token("ADDOP", 3, Line);
            default:
                return new Token(null, null, 0);
        }
    }

    //determines if a . indicates a double dot or not
    private boolean IsDoubleDot()
    {
        if (Index < input.length())
        {
            char c = GetNextChar();
            if (c == '.')
            {
                return true;
            } else
            {
                GoBack();
                return false;
            }
        } else
        {
            return false;
        }
    }

}
