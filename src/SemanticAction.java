import java.util.LinkedList;

public class SemanticAction {
    private SymbolTable constantTable;
    private SymbolTable globalTable;
    private SymbolTable localTable;

    private Boolean insert;
    private Boolean global;
    private Boolean array;
    private int globalMemory;
    private int localMemory;
    private int tempVars;
    private int globalStore;

    //Stack implemented as a linkedlist because it makes the printout look better. For some reason a Stack Object will print bottom up
    private LinkedList<Token> stack;
    private Quadruples quads;

    private boolean verbose = false;

    public SemanticAction(){
        constantTable = new SymbolTable(100);
        globalTable = new SymbolTable(100);
        localTable = new SymbolTable(100);

        insert = true;
        global = true;
        array = false;
        globalMemory = 0;
        localMemory = 0;
        tempVars = 0;
        globalStore = 0;

        stack = new LinkedList<>();
        quads = new Quadruples();
    }


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
            case 30:
                actionThirty(tok);
                break;
            case 31:
                actionThirtyOne();
                break;
            case 40:
                if(tok.getKey() == "UNARYPLUS" || tok.getKey() == "UNARYMINYS"){
                    stack.push(tok);
                }
                break;
            case 41:
                actionFortyOne();
                break;
            case 42:
                String type = tok.getKey();
                if(type == "ADDOP" || type == "MULOP" || type == "RELOP"){
                    stack.push(tok);
                }
                break;
            case 43:
                actionFortyThree();
                break;
            case 44:
                type = tok.getKey();
                if(type == "ADDOP" || type == "MULOP" || type == "RELOP"){
                    stack.push(tok);
                }
                break;
            case 45:
                actionFortyFive();
                break;
            case 46:
                actionFortySix(tok);
                break;
            case 48:
                actionFortyEight();
                break;
            case 55:
                actionFiftyFive();
                break;
            case 56:
                actionFiftySix();
                break;
            default:
                if(verbose) {
                    System.out.println("Action " + act + " not yet implemented");
                }
        }
        if(verbose){
            stackDump();
            //globalTable.dumpTable();
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
            //globalTable.dumpTable();
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

        ProcedureEntry entry4 = new ProcedureEntry("main", 0);
        ProcedureEntry entry5 = new ProcedureEntry("read", 0);
        ProcedureEntry entry6 = new ProcedureEntry("write", 0);

        entry1.setReserved(true);
        entry2.setReserved(true);
        entry3.setReserved(true);
        entry4.setReserved(true);
        entry5.setReserved(true);
        entry6.setReserved(true);

        globalTable.insert(entry1);
        globalTable.insert(entry2);
        globalTable.insert(entry3);
        globalTable.insert(entry4);
        globalTable.insert(entry5);
        globalTable.insert(entry6);

        insert = false;

        gen("call", "main", "0");
        gen("exit");
    }

    private void actionThirteen(Token t){
        if(t.getKey() == "IDENTIFIER"){
            stack.push(t);
        }
    }

    private void actionThirty(Token t){
        try {
            if(lookupID(t) != null){
                stack.push(t);
            } else {
                throw new SemanticError("Undeclared variable");
            }
        } catch (SemanticError e){
            System.err.println(e + " " +  t.getValue() + " on line " + t.getLine());
            System.exit(1);
        }
    }

    private void actionThirtyOne(){
        Token id2 = stack.pop();
        Token offset = null;
        Token id1 = stack.pop();

        SymbolTableEntry _id2 = lookupID(id2);
        SymbolTableEntry _offset = null;
        SymbolTableEntry _id1 = lookupID(id1);

        int type = typeCheck(_id1, _id2);

        try{
            if(type == 3){
                throw new SemanticError("Type mismatch. Can not assign real to int");
            } else if(type == 2){
                VariableEntry temp = create(tempName(), "real");
                gen("ltof", temp.getName(), _id1.name);
                if(offset == null){
                    gen("move", temp.getName(), _id1.name);
                } else {
                    gen("store", temp.getName(), _offset.name, _id1.name);
                }
            } else {
                if(offset == null){
                    gen("move", _id2.name, _id1.name);
                } else {
                    gen("store", _id2.name, _offset.name, _id1.name);
                }
            }
        } catch (SemanticError e){
            System.err.println(e + " on line " + id2.getLine());
            System.exit(1);
        }
    }

    private void actionFortyOne(){
        Token id = stack.pop();
        Token sign = stack.pop();

        SymbolTableEntry _id = lookupID(id);

        if(sign.getKey() == "UNARYMINUS"){
            VariableEntry temp = create(tempName(), _id.getType());
            if(_id.getType() == "integer") {
                gen("uminus", _id.name, temp.getName());
            } else {
                gen("fuminus", _id.name, temp.getName());
            }
            stack.push(new Token(temp.type, temp.name, -1));
        } else {
            stack.push(id);
        }
    }

    private void actionFortyThree(){
        Token id2 = stack.pop();
        Token operator = stack.pop();
        Token id1 = stack.pop();

        SymbolTableEntry _id2 = lookupID(id2);
        SymbolTableEntry _id1 = lookupID(id1);
        String opcode = operator.getOpCode();

        int type = typeCheck(_id1, _id2);

        if(type == 0){
            VariableEntry temp = create(tempName(), "integer");
            gen(opcode, _id1.name, _id2.name, temp.name);
            stack.push(new Token(temp.type, temp.name, -1));
        } else if(type == 1){
            VariableEntry temp = create(tempName(), "real");
            gen("f" + opcode, _id1.name, _id2.name, temp.name);
            stack.push(new Token(temp.type, temp.name, -1));
        } else if(type == 2) {
            VariableEntry temp1 = create(tempName(), "real");
            VariableEntry temp2 = create(tempName(), "real");
            gen("ltof", _id2.name, temp1.name);
            gen("f" + opcode, _id1.name, temp1.name, temp2.name);
            stack.push(new Token(temp2.type, temp2.name, -1));
        } else if(type == 3){
            VariableEntry temp1 = create(tempName(), "real");
            VariableEntry temp2 = create(tempName(), "real");
            gen("ltof", _id1.name, temp1.name);
            gen("f" + opcode, temp1.name, _id2.name, temp2.name);
            stack.push(new Token(temp2.type, temp2.name, -1));
        }
    }

    private void actionFortyFive(){
        Token id2 = stack.pop();
        Token operator = stack.pop();
        Token id1 = stack.pop();

        SymbolTableEntry _id2 = lookupID(id2);
        SymbolTableEntry _id1 = lookupID(id1);
        String opcode = operator.getOpCode();

        int type = typeCheck(_id1, _id2);

        try {
            if (type !=0 && (operator.getSymbol() == "DIV" || operator.getSymbol() == "MOD")){
                throw new SemanticError("Bad Operator: ");
            }
        } catch (SemanticError e){
            System.err.println(e + operator.getSymbol() + " operation on line " + operator.getLine() +
                    " requires integers not reals");
            System.exit(1);
        }

        if(type == 0){
            if(operator.getSymbol() == "MOD"){
                VariableEntry temp1 = create(tempName(), "integer");
                VariableEntry temp2 = create(tempName(), "integer");
                VariableEntry temp3 = create(tempName(), "integer");
                gen("div", _id1.name, _id2.name, temp1.name);
                gen("mul", _id2.name, temp1.name, temp2.name);
                gen("sub", _id1.name, temp2.name, temp3.name);
                stack.push(new Token(temp3.type, temp3.name, -1));
            } else if (operator.getSymbol() == "/") {
                VariableEntry temp1 = create(tempName(), "real");
                VariableEntry temp2 = create(tempName(), "real");
                VariableEntry temp3 = create(tempName(), "real");
                gen("ltof", _id1.name, temp1.name);
                gen("ltof", _id2.name, temp2.name);
                gen("fdiv", temp1.name, temp2.name, temp3.name);
                stack.push(new Token(temp3.type, temp3.name, -1));
            } else {
                VariableEntry temp = create("temp", "integer");
                gen(opcode, _id1.name, _id2.name, temp.name);
                stack.push(new Token(temp.type, temp.name, -1));
            }
        } else if (type == 1) {
            VariableEntry temp = create(tempName(), "real");
            gen("f" + opcode, _id1.name, _id2.name, temp.name);
            stack.push(new Token(temp.type, temp.name, -1));
        } else if (type == 2) {
            VariableEntry temp1 = create(tempName(), "real");
            VariableEntry temp2 = create(tempName(), "real");
            gen("ltof", _id2.name, temp1.name);
            gen("f" + opcode, _id1.name, temp1.name, temp2.name);
            stack.push(new Token(temp2.type, temp2.name, -1));
        } else if (type == 3) {
            VariableEntry temp1 = create(tempName(), "real");
            VariableEntry temp2 = create(tempName(), "real");
            gen("ltof", _id1.name, temp1.name);
            gen("f" + opcode, temp1.name, _id2.name, temp2.name);
            stack.push(new Token(temp2.type, temp2.name, -1));
        }
    }

    private void actionFortySix(Token t){
        if(t.getKey() == "IDENTIFIER"){
            try{
                SymbolTableEntry entry = lookupID(t);
                if(entry == null){
                    throw new SemanticError("Undeclared variable ");
                }
                stack.push(t);
            } catch (SemanticError e) {
                System.err.println(e + t.getValue() + " on line " + t.getLine());
                System.exit(1);
            }
        } else if(t.getKey() == "INTCONSTANT" || t.getKey() == "REALCONSTANT"){
            SymbolTableEntry entry = lookupConstant(t);
            if(entry == null){
                if(t.getKey() == "INTCONSTANT"){
                    entry = new ConstantEntry(t.getValue(), "integer");
                } else if(t.getKey() == "REALCONSTANT"){
                    entry = new ConstantEntry(t.getValue(), "real");
                }
                constantTable.insert(entry);
            }
            stack.push(t);
        }
    }

    private void actionFortyEight(){
        SymbolTableEntry offset = null;
        if(offset != null){
            Token id = stack.pop();
            SymbolTableEntry _id = lookupID(id);
            VariableEntry temp = create(tempName(), _id.getType());
            gen("load", _id.name, offset.name, temp.name);
            stack.push(new Token(temp.type, temp.name, -1));
        }
    }

    private void actionFiftyFive(){
        backpatch(globalStore, String.valueOf(globalMemory));
        gen("free", String.valueOf(globalMemory));
        gen("PROCEND");
    }

    private void actionFiftySix(){
        gen("PROCBEGIN", "main");
        globalStore = quads.getNextQuad();
        gen("alloc", "_");
    }


    private int typeCheck(SymbolTableEntry first, SymbolTableEntry second){
        String type1 = first.getType().toLowerCase();
        String type2 = second.getType().toLowerCase();

        switch(type1){
            case "integer":
                if(type1.equals(type2)){
                    return 0;
                } else {
                    return 3;
                }
            case "real":
                if(type1.equals(type2)){
                    return 1;
                } else {
                    return 2;
                }
            default:
                return 999;
        }

    }

    private VariableEntry create(String name, String type){
        VariableEntry v = new VariableEntry(name);
        v.type = type;
        if(global){
            v.address = globalMemory;
            globalMemory++;
            globalTable.insert(v);
        } else {
            v.address = localMemory;
            localMemory++;
            localTable.insert(v);
        }

        return v;
    }

    private String tempName(){
        tempVars++;
        return "temp" + tempVars;
    }

    private void gen(String... args){
        if(args.length < 1 || args.length > 4){
            System.err.print("Generate Called with too many arguments");
            return;
        }

        String[] code = new String[4];
        code[0] = args[0];

        for(int i = 1; i < args.length; i++) {
            try {
                Integer.parseInt(args[i]);
                SymbolTableEntry e = constantTable.lookup(args[i]);
                if(e != null){
                    VariableEntry temp = create(tempName(), e.getType());
                    String[] constant = new String[4];
                    constant[0] = "move";
                    constant[1] = e.getName();
                    constant[2] = getSteAddress(temp.name);
                    constant[3] = null;
                    quads.addQuad(constant);
                    code[i] = getSteAddress(temp.name);
                } else {
                    code[i] = args[i];
                }
            } catch (NumberFormatException e){
                if(args[i] == "_"){
                    code[i] = args[i];
                } else {
                    code[i] = getSteAddress(args[i]);
                }
            }
        }

        if(args.length < 4){
            for(int i = args.length; i < 4; i++){
                code[i] = null;
            }
        }
        quads.addQuad(code);
    }

    private String getSteAddress(String s){
        if(globalTable.lookup(s) != null){
            return getConstantOrNot("_", globalTable.lookup(s));
        } else if(localTable.lookup(s) != null){
            return getConstantOrNot("%", localTable.lookup(s));
        } else {
            return null;
        }
    }

    private String getConstantOrNot(String pre, SymbolTableEntry e){
        if(e.getClass().getSimpleName() == "ConstantEntry"){
           // VariableEntry temp = create(tempName(), e.getType());
           // gen("move", e.getName(), temp.getName());
           // return pre + temp.getAddress();
            return pre + e.getAddress();
        } else if (e.isArray() || e.isVariable()){
            return pre + e.getAddress();
        } else {
            return e.name;
        }
    }

    private void backpatch(int i, String x){
        quads.setField(i, 1, x);
    }

    private SymbolTableEntry lookupID(Token id){
        SymbolTableEntry entry = globalTable.lookup(id.getValue());
        if(global){
            if(entry != null) {
                return entry;
            } else {
                return lookupConstant(id);
            }
        }
        entry = localTable.lookup(id.getValue());
        if (entry != null){
            return entry;
        } else {
            entry = globalTable.lookup(id.getValue());
        }
        if(entry != null){
            return entry;
        } else {
            return lookupConstant(id);
        }
    }

    private SymbolTableEntry lookupConstant(Token id){
        return constantTable.lookup(id.getValue());
    }




    private void stackDump(){
        System.out.println(stack);
    }

    public void setVerbose(Boolean b){
        verbose = b;
    }

    public void intermediateCodePrint(){
        quads.print();
    }

}

