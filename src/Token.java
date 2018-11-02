public class Token{
    private String key;
    private String value;
    private Integer intValue;
    private Integer line;

    public Token(String x, String y, int z) {
        key = x;
        value = y;
        line = z;
    }

    public Token(String x, int y, int z) {
        key = x;
        intValue = y;
        line = z;
    }

    @Override public String toString(){
        //if(value == null || intValue == null){
        //    return "[\"" + key + "\", " + "null]";
        //}

        if(value != null) {
            return "[\"" + key + "\", \"" + value + "\"]";
        } else {
            return "[\"" + key + "\", " + intValue + "]";
        }
    }

    public boolean equals(Token t){
        return (this.key == t.key && this.value == t.value) || (this.key == t.key && this.intValue == t.intValue);
    }

    public String getSymbol(){
        if(key == "IDENTIFIER" || key == "INTCONSTANT" || key == "REALCONSTANT"){
            return value;
        }else if(key == "ADDOP" || key == "MULOP" || key == "RELOP"){
            return symbolLookUp();
        }else{
            return key;
        }
    }

    private String symbolLookUp(){
        if(key == "ADDOP"){
            switch(intValue){
                case 1:
                    return "+";
                case 2:
                    return "-";
                case 3:
                    return "OR";
            }
        } else if(key == "MULOP"){
            switch(intValue){
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
        } else {
            switch(intValue){
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

    public String getOpCode() {
        if (key == "ADDOP") {
            switch (intValue) {
                case 1:
                    return "add";
                case 2:
                    return "sub";
            }
        } else if (key == "MULOP") {
            switch (intValue) {
                case 1:
                    return "mul";
                case 2:
                    return "div";
                case 3:
                    return "DIV";
                case 4:
                    return "MOD";
            }
        }
        return "Invalid Token Found";
    }

    public String getKey() {
        return key;
    }

    public Integer getLine() {
        return line;
    }

    public String getValue() {
        if(value != null){
            return value;
        } else {
            return String.valueOf(intValue);
        }
    }

    public int toInt(){
        String value = this.getValue();
        return Integer.parseInt(value);
    }


}
