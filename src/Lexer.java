import java.io.*;
import java.util.*;

public class Lexer {
    //The Lexer class takes a file
    private File file;

    //And then reads it into a string
    public Lexer(File x) {
        file = x;
        try {
            this.start();
        } catch (IOException e) {
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
    private static final ArrayList<String> keywords = new ArrayList<>(Arrays.asList("program", "begin","end"
            ,"var","function","procedure","result","integer","real","array","of","not","if","then","else","while","do"));
    private static final ArrayList<String> keyOps = new ArrayList<>(Arrays.asList("div", "and","mod","or"));
    private static final int IDENTIFIER_MAX_LENGTH = 32;

    //These functions determine whether or not a character belongs to one of the categories of accepted characters
    private static boolean isAlphaNumeric(char x){
        return isAlpha(x)|| isNumeric(x);
    }

    private static boolean isAlpha(char x){
        return VALID_CHARS_ALPHA.indexOf(x) != -1;
    }

    private static boolean isNumeric(char x){
        return VALID_CHARS_NUMERIC.indexOf(x) != -1;
    }

    private static boolean isBlank(char x){
        return VALID_CHARS_BLANKS.indexOf(x) != -1;
    }

    private static boolean isPunc(char x){
        return VALID_CHARS_PUNCTUATION.indexOf(x) != -1;
    }

    private static boolean isOp(char x){
        return VALID_CHARS_OPS.indexOf(x) != -1;
    }


    //input is the String that holds the file characters
    private String input = "";

    //Store the previous token in order to check it later
    private Token previousToken = new Token(null, null, 0);

    //Reads the input file into a string
    private void start() throws IOException {
        FileReader reader = new FileReader(file);
        int x = reader.read();
        while(x != -1){
            input += (char)x;
            x = reader.read();
        }
        input = input.toLowerCase();
        reader.close();
    }

    //index keeps track of what point in the string we are
    private int index = 0;

    //Keeps track of what line we are on in the original file
    private int line = 1;

    //Returns the next char in the string
    private char getNextChar(){
        index++;
        if(index > input.length()){
            return ' ';
        }
        if(input.charAt(index - 1) == '\n'){
            line++;
        }
        return input.charAt(index - 1);
    }

    //used to get back to our initial position after a lookahead
    private void goBack(){
        index--;
        if(index >= input.length()){
            return;
        }
        if(input.charAt(index) == '\n'){
            line--;
        }
    }

    //Publicly accessible method for retrieving the next token
    public Token getNextToken(){
        Token token;

        //check to see if we have reached the end of the file
        if(index >= input.length()){
            return new Token("ENDOFFILE", null, line);
        }

        char c = getNextChar();

        //scrolls over blanks
        while (isBlank(c)){
            if(index >= input.length()){
                return new Token("ENDOFFILE", null, line);
            }
            c = getNextChar();
        }

        //Determine the category of the char c or throw an error if it is not a valid character
        try {
            if (isAlpha(c)) {
                token = alphaNFA(c);
            } else if (isNumeric(c)) {
                token = numNFA(c);
            } else if (isPunc(c)) {
                token = puncNFA(c);
            } else if (isOp(c)) {
                token = opNFA(c);
            } else {
                throw new LexicalError("Invalid Character");
            }
            previousToken = token;
            return token;
        } catch (LexicalError e) {
            System.err.println(e.getMessage() + " on line: " + line);
            System.exit(1);
        }

        //this should never be reached but java wants a return statement
        return null;
    }

    //determine the token key and value when presented with an alpha numeric character
    private Token alphaNFA(char c){
        String output = "";
        while(isAlphaNumeric(c)){
            output += c;
            c = getNextChar();
        }
        goBack();
        //finds if identifier is a keyword or operator
        if(keywords.contains(output)){
            //goBack();
            return new Token(output.toUpperCase(), null, line);
        } else if(keyOps.contains(output)){
            //goBack();
            return opSelector(output);
        }

        //check that the identifier is shorter than the maximum length
        else {
            try {
                if (output.length() <= IDENTIFIER_MAX_LENGTH) {
                   // goBack();
                    return new Token("IDENTIFIER", output, line);
                } else {
                    //goBack();
                    throw new LexicalError("Identifier exceeds maximum length of 32 chars: ");
                }
            } catch (LexicalError e){
                System.err.println((e.getMessage() + " on line " + line));
                System.exit(1);
            }

        }

        //again, this should never be reached
        return null;
    }

    //method for determining the token type and value when the first char is a number
    private Token numNFA(char c){
        String value = "";
        //number can be int
        while(isNumeric(c)){
            value += c;
            c = getNextChar();
        }

        //of the form X.Y
        if (c == '.'){
            if(isDoubleDot()){
                goBack();
                goBack();
                return new Token("INTCONSTANT", value, line);
            }
            //make sure the constant follows lexical rules
            try {
                c = getNextChar();
                if(isNumeric(c)){
                    value += '.';
                } else {
                    value += '.';
                    throw new LexicalError("Invalid Constant " + value);
                }
            } catch (LexicalError e){
                System.err.println(e.getMessage() + " on line: "+ line);
                System.exit(1);
            }
            while(isNumeric(c)){
                value += c;
                c = getNextChar();
            }
            //of the form X.Ye(+/-)z
            if (c == 'e') {
                return(exponent(c, value));
            }
            goBack();
            return new Token("REALCONSTANT", value, line);
        }
        //or Xe(+/-)Y
        if (c == 'e') {
            return exponent(c, value);
        }

        goBack();
        return new Token("INTCONSTANT", value, line);
    }

    //method for determining token type and value when char is punctuation
    private Token puncNFA(char c){
        //scrolls past comments and returns the next token in the file
        if (c == '{') {
            while (c != '}') {
                c = getNextChar();
            }
                return getNextToken();
            }
        try {
            switch (c) {
                case '(':
                    return new Token("LPAREN", null, line);
                case ')':
                    return new Token("RPAREN", null, line);
                case '[':
                    return new Token("LBRACKET", null, line);
                case ']':
                    return new Token("RBRACKET", null, line);
                case ';':
                    return new Token("SEMICOLON", null, line);
                case ':':
                    if (getNextChar() == '=') {
                        return new Token("ASSIGNOP", null, line);
                    } else {
                        goBack();
                        return new Token("COLON", null, line);
                    }
                case ',':
                    return new Token("COMMA", null, line);
                case '.':
                    if (isDoubleDot()) {
                        return new Token("DOUBLEDOT", null, line);
                    } else {
                        return new Token("ENDMARKER", null, line);
                    }
                default:
                    throw new LexicalError("Right Brace not allowed");
            }
        } catch(LexicalError e){
            System.err.println(e.getMessage() + " on line: " + line);
            System.exit(1);
        }
        //should not be reached
        return null;
    }

    //method for determining token type and value when char is an operation
    private Token opNFA(char c){
        switch(c){
            case '=':
                return new Token("RELOP", 1, line);
            case '<':
                char x = getNextChar();
                if(x =='>'){
                    return new Token("RELOP", 2, line);
                } else if(x == '='){

                    return new Token("RELOP", 5, line);
                } else {
                    goBack();
                    return new Token("RELOP", 3, line);
                }
            case '>':
                x = getNextChar();
                if(x == '='){
                    return new Token("RELOP", 6, line);
                } else {
                    goBack();
                    return new Token("RELOP", 4, line);
                }
            case '*':
                return new Token("MULOP", 1, line);
            case '/':
                return new Token("MULOP", 2, line);
            case '+':
            case '-':
                return unaryOrAdd(c);
             //should not be reached
            default:
                return new Token(null,null, 0);
        }
    }

    //method for finding value after encountering an e in a constant
    private Token exponent(char c, String value){
            value += c;
            c = getNextChar();
            if(c == '-' || c == '+'){
                value += c;
                c = getNextChar();
            }
            try{
                if(!isNumeric(c) && c != '-' && c != '+'){
                    throw new LexicalError("Invalid Constant " + value);
                }
            } catch(LexicalError e){
                System.err.println(e.getMessage() + " on line: " + line);
                System.exit(1);
            }
            while(isNumeric(c)){
                value += c;
                c = getNextChar();
            }
            goBack();
            return new Token ("REALCONSTANT", value, line);
    }

    //determines if the + and - symbols are ADDOP's or UNARYOP's
    private Token unaryOrAdd(char c){
        if (previousToken.getKey() == "RIGHTPAREN" || previousToken.getKey() == "RIGHTBRACKET" ||
                previousToken.getKey() == "IDENTIFIER" || previousToken.getKey() == "INTCONSTANT" ||
                previousToken.getKey() == "REALCONSTANT") {
            if(c == '+'){
                return new Token("ADDOP",1, line);
            } else {
                return new Token ("ADDOP", 2, line);
            }

        } else {
            if(c == '+'){
                return new Token("UNARYPLUS", null, line);
            } else {
                return new Token("UNARYMINUS", null, line);
            }
        }
    }

    //returns the token with integer value for keyword operations
    private Token opSelector(String input){
        switch(input){
            case "div":
                return new Token("MULOP", 3, line);
            case "mod":
                return new Token("MULOP", 4, line);
            case "and":
                return new Token("MULOP", 5, line);
            case "or":
                return new Token("ADDOP", 3, line);
            default:
                return new Token(null, null, 0);
        }
    }

    //determines if a . indicates a double dot or not
    private boolean isDoubleDot(){
        if(index < input.length()) {
            char c = getNextChar();
            if (c == '.') {
                return true;
            } else {
                goBack();
                return false;
            }
        } else {
            return false;
        }
    }

    /*
    *  TODO: better error handling
    *  TODO: Add a method for attaching unary operators to constants
    *  TODO: anything other than white space or eof after a number is not allowed
    */

}
