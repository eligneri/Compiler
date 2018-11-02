import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;
import java.io.File;

public class Parser {
    //Stack implemented as a linkedlist because it makes the printout look better. For some reason a Stack Object will print bottom up
    private LinkedList<String> stack = new LinkedList<>();
    private ParseTable table = new ParseTable();
    private Grammar g;
    private Lexer lex;
    private boolean verbose = false;
    private SemanticAction actions = new SemanticAction();

    public Parser(File file){
        lex = new Lexer(file);
        g = new Grammar(new File("/Users/Ellis/IdeaProjects/Compiler/grammar.txt"));
        stack.push("ENDOFFILE");
        stack.push("<Goal>");
    }

    //Keeps track of how many steps the parse has taken
    private int step = 1;

    public void parse(){
        Token prevTok = null;
        Token tok = lex.getNextToken();
        String key = tok.getKey();
        while(!stack.isEmpty()){
            try {
                if (verbose) {
                    System.out.println("\n>>>  " + step + "  <<<\nStack ::==> " + dumpStack());
                }
                String stac = stack.peek();
                if (key.equals(stac.toUpperCase())) {
                    if (verbose) {
                        System.out.println("Popped " + stac + " with token " + key + " *MATCH* {consume token}");
                    }
                    stack.pop();
                    prevTok = tok;
                    tok = lex.getNextToken();
                    key = tok.getKey();
                } else if (isNonTerminal(stac)) {
                    if (verbose) {
                        System.out.print("Popped " + stac + " with token " + key + " *PUSH* ");
                    }
                    int i = getRule(key, stac);
                    if(i == 999){
                        throw new ParseError("Invalid Symbol. Unexpected " + tok.getSymbol() + " on line " +
                                tok.getLine());

                    }
                    stack.pop();
                    stackAdd(i);
                } else if(isSemantic(stac)) {
                    if(verbose){
                        System.out.println("Popped " + stac + " with token " + key + " *SEMANTIC ACTION* [" +
                                stac.substring(1) + "]");
                    }
                    actions.execute(stac.substring(1), prevTok);
                    stack.pop();
                } else if (!isNonTerminal(stac)) {
                    throw new ParseError("Invalid Symbol. Expected " + stac + " on line " + tok.getLine() +
                            " instead of " + tok.getSymbol());
                }
                step++;
            } catch (ParseError e){
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }

        System.out.println("\nParse Accepted");
        actions.intermediateCodePrint();
    }

    private boolean isNonTerminal(String s){
        return s.charAt(0) == '<';
    }

    private boolean isSemantic(String s){
        return s.charAt(0) == '#';
    }
    private int getRule(String key, String stac){
        return table.getValue(key, stac);
    }

    private void stackAdd(int i){
        if(i > 0){
            String first = g.getProduction(i);
            String[] productions = first.split("\\s+");
            for(int j = productions.length - 1; j >= 0; j--) {
                stack.push(productions[j]);
            }
            if(verbose){
                System.out.print(Arrays.toString(productions) + "\n");
            }
        } else {
            if(verbose){
                System.out.print(" *EPSILON*\n");
            }
        }
    }

    private String dumpStack(){
        return stack.toString();
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
        actions.setVerbose(verbose);
    }

}


