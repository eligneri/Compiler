import java.util.LinkedList;
import java.util.Stack;

public class SemanticAction {
    private SymbolTable constantTable = new SymbolTable(100);
    private SymbolTable globalTable = new SymbolTable(100);
    private SymbolTable localTable;

    private Boolean insert = true;
    private Boolean global = true;
    private Boolean array = false;
    private int globalMemory = 0;
    private int localMemory = 0;

    //Stack implemented as a linkedlist because it makes the printout look better. For some reason a Stack Object will print bottom up
    private LinkedList<Token> stack = new LinkedList<>();

    private boolean verbose = false;


    protected void execute(String action, Token tok){
        if (verbose) {
            System.out.println(tok);
        }
        int act = Integer.parseInt(action);
        switch(act){
            case 1:
                insert = true;
                break;
            case 2:
                insert = false;
                break;
            case 3:
                actionThree();
                break;
            case 4:
                actionFour(tok);
                break;
            case 6:
                array = true;
                break;
            case 7:
                actionSeven(tok);
                break;
            case 9:
                actionNine();
                break;
            case 13:
                actionThirteen(tok);
                break;
            default:
                System.out.println("Action " + act + " not yet implemented");
        }
        if(verbose){
            stackDump();
        }
    }

    private void actionThree(){
        String type = stack.pop().getKey();
        if(array){
            int upperBound = stack.pop().toInt();
            int lowerBound = stack.pop().toInt();
            int memorySize = (upperBound - lowerBound) + 1;

            while(stack.peek().getKey() == "IDENTIFIER"){
                Token t = stack.pop();
                ArrayEntry id = new ArrayEntry(t.getValue());
                id.setType(type);
                id.setUpperBound(upperBound);
                id.setLowerBound(lowerBound);

                if(global){
                    id.setAddress(globalMemory);
                    globalTable.insert(id);
                    globalMemory += memorySize;
                } else {
                    id.setAddress(localMemory);
                    localTable.insert(id);
                    localMemory += memorySize;
                }

                if(stack.peek() == null){
                    break;
                }
            }
        } else {
            while(stack.peek().getKey() == "IDENTIFIER"){
                Token t = stack.pop();
                VariableEntry id = new VariableEntry(t.getValue());
                id.setType(type);

                if(global){
                    id.setAddress(globalMemory);
                    globalTable.insert(id);
                    globalMemory++;
                } else {
                    id.setAddress(localMemory);
                    localTable.insert(id);
                    localMemory++;
                }

                if(stack.peek() == null){
                    break;
                }
            }
        }

        if(verbose){
            globalTable.dumpTable();
        }

        array = false;
    }

    private void actionFour(Token t){
        String type = t.getKey();
        if(type.equals("INTEGER") || type.equals("REAL")){
            stack.push(t);
        }
    }

    private void actionSeven(Token t){
        if(t.getKey() == "INTCONSTANT"){
            stack.push(t);
        }
    }

    private void actionNine(){
        Token id1 = stack.pop();
        Token id2 = stack.pop();
        Token id3 = stack.pop();
        if(verbose) { System.out.println(id1 + " " + id2 + " " + id3);}

        IODeviceEntry entry1 = new IODeviceEntry(id1.getValue());
        IODeviceEntry entry2 = new IODeviceEntry(id2.getValue());
        ProcedureEntry entry3 = new ProcedureEntry(id3.getValue(), 0);
        if(verbose) {System.out.println(entry1 + " " + entry2 + " " + entry3);}

        entry1.setReserved(true);
        entry2.setReserved(true);
        entry3.setReserved(true);

        globalTable.insert(entry1);
        globalTable.insert(entry2);
        globalTable.insert(entry3);

        insert = false;
    }

    private void actionThirteen(Token t){
        if(t.getKey() == "IDENTIFIER"){
            stack.push(t);
        }
    }

    private void stackDump(){
        System.out.println(stack);
    }

    public void setVerbose(Boolean b){
        verbose = b;
    }
}
