/*
 * This class defines a Token, which is what the lexer turns a file into
 */
public class Token
{
    private String Key;
    private String Value;
    private Integer IntValue;
    private Integer Line;

    public Token(String x, String y, int z)
    {
        Key = x;
        Value = y;
        Line = z;
    }

    public Token(String x, int y, int z)
    {
        Key = x;
        IntValue = y;
        Line = z;
    }

    @Override
    public String toString()
    {

        if (Value != null)
        {
            return "[\"" + Key + "\", \"" + Value + "\"]";
        } else
        {
            return "[\"" + Key + "\", " + IntValue + "]";
        }
    }

    //Returns the symbol of the toke
    public String GetSymbol()
    {
        if (Key == "IDENTIFIER" || Key == "INTCONSTANT" || Key == "REALCONSTANT")
        {
            return Value;
        } else if (Key == "ADDOP" || Key == "MULOP" || Key == "RELOP")
        {
            return SymbolLookUp();
        } else
        {
            return Key;
        }
    }

    //finds what symbol corresponds to the int value
    private String SymbolLookUp()
    {
        if (Key == "ADDOP")
        {
            switch (IntValue)
            {
                case 1:
                    return "+";
                case 2:
                    return "-";
                case 3:
                    return "OR";
            }
        } else if (Key == "MULOP")
        {
            switch (IntValue)
            {
                case 1:
                    return "*";
                case 2:
                    return "/";
                case 3:
                    return "DIV";
                case 4:
                    return "MOD";
                case 5:
                    return "AND";
            }
        } else
        {
            switch (IntValue)
            {
                case 1:
                    return "=";
                case 2:
                    return "<>";
                case 3:
                    return "<";
                case 4:
                    return ">";
                case 5:
                    return "<=";
                case 6:
                    return ">=";
            }
        }
        return "Invalid Token Found";
    }

    //finds opcodes that correspond to token values
    public String GetOpCode()
    {
        if (Key == "ADDOP")
        {
            switch (IntValue)
            {
                case 1:
                    return "add";
                case 2:
                    return "sub";
            }
        } else if (Key == "MULOP")
        {
            switch (IntValue)
            {
                case 1:
                    return "mul";
                case 2:
                    return "div";
                case 3:
                    return "div";
                case 4:
                    return "MOD";
            }
        } else
        {
            switch (IntValue)
            {
                case 1:
                    return "beq";
                case 2:
                    return "bne";
                case 3:
                    return "blt";
                case 4:
                    return "bgt";
                case 5:
                    return "ble";
                case 6:
                    return "bge";
            }
        }
        return "Invalid Token Found";
    }

    /*
     * Getters
     */
    public String GetKey()
    {
        return Key;
    }

    public Integer GetLine()
    {
        return Line;
    }

    public String GetValue()
    {
        if (Value != null)
        {
            return Value;
        } else
        {
            return String.valueOf(IntValue);
        }
    }

    public int ToInt()
    {
        String value = this.GetValue();
        return Integer.parseInt(value);
    }


}
