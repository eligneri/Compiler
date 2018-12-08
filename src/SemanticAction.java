import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    private int localStore;
    private int nextParam;
    private SymbolTableEntry currentFunction;

    //Stack implemented as a linkedlist because it makes the printout look better. For some reason a Stack Object will print bottom up
    private LinkedList<Object> stack;
    private LinkedList<Integer> paramCount;
    private LinkedList<List<SymbolTableEntry>> paramStack;
    private Quadruples quads;

    private boolean verbose = false;

    private enum EType{
        ARITHMETIC, RELATIONAL
    }

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
        paramStack = new LinkedList<>();
        quads = new Quadruples();

        paramCount = new LinkedList<>();
        paramCount.push(0);
    }


    protected void execute(String action, Token tok){
        if (verbose) {
            System.out.println(tok);
            //globalTable.dumpTable();
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
            case 5:
                actionFive();
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
            case 11:
                actionEleven();
                break;
            case 13:
                actionThirteen(tok);
                break;
            case 15:
                actionFifteen(tok);
                break;
            case 16:
                actionSixteen();
                break;
            case 17:
                actionSeventeen(tok);
                break;
            case 19:
                actionNineteen();
                break;
            case 20:
                actionTwenty();
                break;
            case 21:
                actionTwentyOne();
                break;
            case 22:
                actionTwentyTwo(tok);
                break;
            case 24:
                actionTwentyFour();
                break;
            case 25:
                actionTwentyFive(tok);
                break;
            case 26:
                actionTwentySix();
                break;
            case 27:
                actionTwentySeven();
                break;
            case 28:
                actionTwentyEight();
                break;
            case 29:
                actionTwentyNine();
                break;
            case 30:
                actionThirty(tok);
                break;
            case 31:
                actionThirtyOne(tok);
                break;
            case 32:
                actionThirtyTwo();
                break;
            case 33:
                actionThirtyThree();
                break;
            case 34:
                actionThirtyFour(tok);
                break;
            case 35:
                actionThirtyFive();
                break;
            case 36:
                actionThirtySix();
                break;
            case 37:
                actionThirtySeven();
                break;
            case 38:
                actionThirtyEight(tok);
                break;
            case 39:
                actionThirtyNine();
                break;
            case 40:
                if(tok.getKey() == "UNARYPLUS" || tok.getKey() == "UNARYMINUS"){
                    stack.push(tok);
                }
                break;
            case 41:
                actionFortyOne();
                break;
            case 42:
                actionFortyTwo(tok);
                break;
            case 43:
                actionFortyThree();
                break;
            case 44:
                actionFortyFour(tok);
                break;
            case 45:
                actionFortyFive();
                break;
            case 46:
                actionFortySix(tok);
                break;
            case 47:
                actionFortySeven();
                break;
            case 48:
                actionFortyEight(tok);
                break;
            case 49:
                actionFortyNine();
                break;
            case 50:
                actionFifty();
                break;
            case 51:
                actionFiftyOne(tok);
                break;
            case 52:
                actionFiftyTwo();
                break;
            case 53:
                actionFiftyThree();
                break;
            case 54:
                actionFiftyFour();
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
        Token tok = (Token)stack.pop();
        String type = tok.getKey();
        if(array){
            Token tok1 = (Token)stack.pop();
            Token tok2 = (Token)stack.pop();
            int upperBound = tok1.toInt();
            int lowerBound = tok2.toInt();
            int memorySize = (upperBound - lowerBound) + 1;

            Token test = (Token)stack.peek();
            while(test.getKey() == "IDENTIFIER"){
                Token t = (Token)stack.pop();
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
                test = (Token)stack.peek();
            }
        } else {
            Token test = (Token)stack.peek();
            while(test.getKey() == "IDENTIFIER"){
                Token t = (Token)stack.pop();
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

                if(stack.peek() == null || stack.peek().getClass().getSimpleName() != "Token"){
                    break;
                }
                test = (Token)stack.peek();
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

    private void actionFive(){
        insert = false;
        SymbolTableEntry id = (SymbolTableEntry)stack.pop();
        gen("PROCBEGIN", id.name);
        localStore = quads.getNextQuad();
        gen("alloc", "_");
    }

    private void actionSeven(Token t){
        if(t.getKey() == "INTCONSTANT"){
            stack.push(t);
        }
    }

    private void actionNine(){
        Token id1 = (Token)stack.pop();
        Token id2 = (Token)stack.pop();
        Token id3 = (Token)stack.pop();
        //if(verbose) { System.out.println(id1 + " " + id2 + " " + id3);}

        IODeviceEntry entry1 = new IODeviceEntry(id1.getValue());
        IODeviceEntry entry2 = new IODeviceEntry(id2.getValue());
        ProcedureEntry entry3 = new ProcedureEntry(id3.getValue(), 0);
        //if(verbose) {System.out.println(entry1 + " " + entry2 + " " + entry3);}

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

        gen("call", "main", 0);
        gen("exit");
    }

    private void actionEleven(){
        global = true;
        localTable = new SymbolTable(100);
        currentFunction = null;
        backpatch(makeList(localStore), String.valueOf(localMemory));
        gen("free", String.valueOf(localMemory));
        gen("PROCEND");
    }

    private void actionThirteen(Token t){
        if(t.getKey() == "IDENTIFIER"){
            stack.push(t);
        }
    }

    private void actionFifteen(Token t){
        VariableEntry result = create(t.getValue() + "_RESULT", "integer");
        result.setResult();
        SymbolTableEntry id = new FunctionEntry(t.getValue(), result);

        globalTable.insert(id);
        global = false;
        localMemory = 0;
        currentFunction = id;
        stack.push(id);
    }

    private void actionSixteen(){
        Token type = (Token)stack.pop();
        FunctionEntry id = (FunctionEntry)stack.peek();
        id.setType(type.getKey());
        id.setResultType(type.getKey());
        currentFunction = id;
    }

    private void actionSeventeen(Token t){
        SymbolTableEntry id = new ProcedureEntry(t.getValue());
        globalTable.insert(id);
        global = false;
        localMemory = 0;
        currentFunction = id;
        stack.push(id);
    }

    private void actionNineteen(){
        paramStack = new LinkedList<>();
        paramCount.push(0);
    }

    private void actionTwenty(){
        SymbolTableEntry id = (SymbolTableEntry)stack.peek();
        int numParams = paramCount.pop();
        id.setNumberOfParameters(numParams);
    }

    private void actionTwentyOne(){
        Token type = (Token)stack.pop();
        int upperBound = -1;
        int lowerBound = -1;

        if(array){
            Token ub = (Token)stack.pop();
            Token lb = (Token)stack.pop();
            upperBound = Integer.parseInt(ub.getValue());
            lowerBound = Integer.parseInt(lb.getValue());
        }

        LinkedList<Token> parameters = new LinkedList<>();

        Token t = (Token)stack.peek();
        while(t.getKey() == "IDENTIFIER"){
            parameters.push((Token)stack.pop());
            if(stack.peek().getClass().getSimpleName() != "Token"){
                break;
            }
            t = (Token)stack.peek();
        }

        while(!parameters.isEmpty()) {
            Token param = parameters.pop();
            SymbolTableEntry var;
            if (array) {
                var = new ArrayEntry(param.getValue(), localMemory, type.getKey(), upperBound, lowerBound);
            } else {
                var = new VariableEntry(param.getValue(), localMemory, type.getKey());
            }
            var.setParameter();
            localTable.insert(var);
            currentFunction.addParameter(var);
            localMemory++;
            paramCount.push(paramCount.pop() + 1);
        }

        array = false;

    }

    private void actionTwentyTwo(Token t){
        EType etype = (EType)stack.pop();
        try {
            if (etype != EType.RELATIONAL) {
                throw new SemanticError("Expression Type mismatch: Expected relational instead of arithmetic on line "
                + t.getLine());
            }
        }catch(SemanticError e){
            System.err.println(e);
            System.exit(1);
        }

        List<Integer> EFalse = (List<Integer>)stack.pop();
        List<Integer> ETrue = (List<Integer>)stack.pop();
        backpatch(ETrue, String.valueOf(quads.getNextQuad()));
        stack.push(ETrue);
        stack.push(EFalse);
    }

    private void actionTwentyFour(){
        int beginLoop = quads.getNextQuad();
        stack.push(beginLoop);
    }

    private void actionTwentyFive(Token t){
        //As far as I can tell, these actions are the same
        actionTwentyTwo(t);
    }

    private void actionTwentySix(){
        List<Integer> EFalse = (List<Integer>)stack.pop();
        List<Integer> ETrue = (List<Integer>)stack.pop();
        int beginLoop = (Integer) stack.pop();
        gen("goto", String.valueOf(beginLoop));
        backpatch(EFalse, String.valueOf(quads.getNextQuad()));
    }

    private void actionTwentySeven(){
        List<Integer> skipElse = makeList(quads.getNextQuad());
        gen("goto", "_");
        List<Integer> EFalse = (List<Integer>)stack.pop();
        List<Integer> ETrue = (List<Integer>)stack.pop();
        backpatch(EFalse, String.valueOf(quads.getNextQuad()));
        stack.push(skipElse);
        stack.push(ETrue);
        stack.push(EFalse);
    }

    private void actionTwentyEight(){
        List<Integer> EFalse = (List<Integer>)stack.pop();
        List<Integer> ETrue = (List<Integer>)stack.pop();
        List<Integer> skipElse = (List<Integer>)stack.pop();
        backpatch(skipElse, String.valueOf(quads.getNextQuad()));
    }

    private void actionTwentyNine(){
        List<Integer> EFalse = (List<Integer>)stack.pop();
        List<Integer> ETrue = (List<Integer>)stack.pop();
        backpatch(EFalse, String.valueOf(quads.getNextQuad()));
    }


    private void actionThirty(Token t){
        try {
            if(lookupID(t) != null){
                stack.push(lookupID(t));
                stack.push(EType.ARITHMETIC);
            } else {
                throw new SemanticError("Undeclared variable");
            }
        } catch (SemanticError e){
            System.err.println(e + " " +  t.getValue() + " on line " + t.getLine());
            System.exit(1);
        }
    }

    private void actionThirtyOne(Token t){
        EType etype = (EType)stack.pop();
        try {
            if (etype != EType.ARITHMETIC) {
                throw new SemanticError("Expression Type mismatch: Expected Arithmetic instead of relational" +
                        " expression on line " + t.getLine());
            }
        }catch(SemanticError e){
            System.err.println(e);
            System.exit(1);
        }

        SymbolTableEntry _id2 = (SymbolTableEntry)stack.pop();
        SymbolTableEntry _offset = (SymbolTableEntry)stack.pop();
        SymbolTableEntry _id1 = (SymbolTableEntry)stack.pop();

        int type = typeCheck(_id1, _id2);

        try{
            if(type == 3){
                throw new SemanticError("Type mismatch. Can not assign real to int");
            } else if(type == 2){
                VariableEntry temp = create(tempName(), "real");
                gen("ltof", _id2.name, temp.getName());
                if(_offset == null){
                    gen("move", temp.getName(), _id1.name);
                } else {
                    gen("stor", temp.getName(), _offset.name, _id1.name);
                }
            } else {
                if(_offset == null){
                    gen("move", _id2.name, _id1.name);
                } else {
                    gen("stor", _id2.name, _offset.name, _id1.name);
                }
            }
        } catch (SemanticError e){
            System.err.println(e);//+ " on line " + _id2.getLine());
            System.exit(1);
        }
    }

    private void actionThirtyTwo(){
        EType etype = (EType)stack.pop();
        SymbolTableEntry id = (SymbolTableEntry)stack.peek();
        try {
            if (etype != EType.ARITHMETIC) {
                throw new SemanticError("Expression type Mismatch. Require arithmetic expression");
            }
            if(!id.isArray()) {
                throw new SemanticError(id + " is not an array");
            }
        } catch(SemanticError e){
            System.err.println(e);
            System.exit(1);
        }
    }

    private void actionThirtyThree(){
        EType etype = (EType)stack.pop();
        SymbolTableEntry id = (SymbolTableEntry)stack.pop();
        try{
            if(etype != EType.ARITHMETIC) {
                throw new SemanticError("Expression Type mismatch");
            }
            if(!id.getType().toLowerCase().equals("integer")) {
                throw new SemanticError("Type mismatch, expected " + id.name + " to be in an integer");
            }
        }catch(SemanticError e) {
            System.err.println(e);
            System.exit(1);
        }
        ArrayEntry arr = (ArrayEntry)stack.peek();
         /*
            If Statement necessary because my gen function will move any constants in the constant table into temp
            variables. It was implemented in a way that requires the constant to already exist however. This created
            a problem where, if a constant already existed, gen would move it into a variable and then the move in this
            method would move that variable into another temp variable, which is redundant. But if a constant is not
            already in the table, the move in this method is necessary. This is all in order to make the intermediate
            code look like the examples. i.e. without it, the code looks like:
            mov 1, _49
            mov _49, _47
            mov 2, _50
            sub _50, _47, _48

            instead of:
            move 2, _47
            move 1, _48
            sub _47, _48, _46

            I'm planning on cleaning up this implementation but I'm not sure if it's
            possible.
         */
        if(constantTable.lookup(String.valueOf(arr.lowerBound)) == null) {
            VariableEntry temp1 = create(tempName(), "integer");
            VariableEntry temp2 = create(tempName(), "integer");
            gen("move", String.valueOf(arr.lowerBound), temp1.name);
            gen("sub", id.name, temp1.name, temp2.name);
            stack.push(temp2);
        } else {
            VariableEntry temp = create(tempName(), "integer");
            insert = false;
            gen("sub", id.name, String.valueOf(arr.lowerBound), temp.name);
            insert = true;
            stack.push(temp);
        }


    }

    private void actionThirtyFour(Token t){
        EType etype = (EType)stack.pop();
        SymbolTableEntry id = (SymbolTableEntry)stack.peek();
        if(id.isFunction()){
            stack.push(etype);
            execute("52", t);
        } else {
            stack.push(null);
        }
    }

    private void actionThirtyFive(){
        EType etype = (EType)stack.pop();
        ProcedureEntry id = (ProcedureEntry)stack.peek();
        stack.push(etype);
        paramCount.push(0);
        paramStack.push(id.parameterInfo);
    }

    private void actionThirtySix(){
        EType etype = (EType)stack.pop();
        ProcedureEntry id = (ProcedureEntry)stack.pop();
        try {
            if (id.numberofParameters != 0) {
                throw new SemanticError("Wrong number of parameters. Expected 0 in procedure: " + id.name);
            }
        } catch(SemanticError e){
            System.err.println(e);
            System.exit(1);
        }
        insert = true;
        gen("call", id.name, 0);
        insert = false;
    }

    private void actionThirtySeven(){
        EType etype = (EType)stack.pop();
        try{
            if(etype != EType.ARITHMETIC){
                throw new SemanticError("Expression Type mismatch: Expected Arithmetic instead of relational");
            }

            SymbolTableEntry id = (SymbolTableEntry)stack.peek();
            if(!(id.isVariable() || id.isFunctionResult() || id.isArray() || id.getClass().getSimpleName() == "ConstantEntry")){
                throw new SemanticError("Unexpected Parameter " + id.name);
            }
            paramCount.push(paramCount.pop() + 1);

            LinkedList<SymbolTableEntry> parameters = new LinkedList<>();

            SymbolTableEntry s = (SymbolTableEntry)stack.peek();
            while(!(s.isFunction() || s.isProcedure())){
                parameters.push(s);
                stack.pop();
                if(stack.peek().equals(EType.ARITHMETIC) || stack.peek().equals(EType.RELATIONAL)) {
                    stack.pop();
                    break;
                }
                s = (SymbolTableEntry)stack.peek();
            }

            SymbolTableEntry funcID = (SymbolTableEntry)stack.peek();
            stack.push(etype);
            while(!parameters.isEmpty()){
                stack.push(parameters.pop());
            }

            if(!(funcID.name == "read" || funcID.name == "write")){
                if(paramCount.peek() > funcID.getNumberOfParameters()){
                    throw new SemanticError("Wrong number of parameters for " + funcID.name + ". Expected " +
                            funcID.getNumberOfParameters());
                }

                SymbolTableEntry param = paramStack.peek().get(nextParam);
                if(!id.getType().toLowerCase().equals(param.getType().toLowerCase())){
                    throw new SemanticError("Bad parameter type, expected " + param.name + " to be a(n) " + id.getType());
                }
                if(param.isArray()){
                    ArrayEntry _id = (ArrayEntry)id;
                    ArrayEntry _param = (ArrayEntry)param;
                    if((_id.getLowerBound() != _param.getLowerBound()) ||
                            (_id.getUpperBound() != _param.getUpperBound())){
                        throw new SemanticError("Expected " + param.name + " to have a lower bound of " +
                                _id.getLowerBound() + " and an upper bound of " + _id.getUpperBound());
                    }
                }
                nextParam++;
            }
        } catch (SemanticError e){
            System.err.println(e);
            System.exit(1);
        }
    }

    private void actionThirtyEight(Token t){
        EType etype = (EType)stack.pop();
        try {
            if (etype != EType.ARITHMETIC) {
                throw new SemanticError("Expression Type mismatch: Expected Arithmetic instead of relational" +
                        "on line " + t.getLine());
            }
        }catch(SemanticError e){
            System.err.println(e);
            System.exit(1);
        }

        String type = t.getKey();
        if(type == "ADDOP" || type == "MULOP" || type == "RELOP"){
            stack.push(t);
        }
    }

    private void actionThirtyNine(){
        EType etype = (EType)stack.pop();
        try {
            if (etype != EType.ARITHMETIC) {
                throw new SemanticError("Expression Type mismatch: Expected Arithmetic instead of relational");
            }
        }catch(SemanticError e){
            System.err.println(e);
            System.exit(1);
        }

        SymbolTableEntry id2 = (SymbolTableEntry)stack.pop();
        Token operator = (Token)stack.pop();
        String opcode = operator.getOpCode();
        SymbolTableEntry id1 = (SymbolTableEntry)stack.pop();

        int type = typeCheck(id1, id2);

        if(type == 2){
            VariableEntry temp = create(tempName(), "real");
            gen("ltof", id2.name, temp.name);
            gen(opcode, id1.name, temp.name, "_");
        } else if(type == 3){
            VariableEntry temp = create(tempName(), "real");
            gen("ltof", id1.name, id2.name);
            gen(opcode, temp.name, id2.name, "_");
        } else {
            //insert = false;
            gen(opcode, id1.name, id2.name, "_");
            //insert = true;
        }

        gen("goto", "_");
        List<Integer> ETrue = makeList(quads.getNextQuad() - 2);
        List<Integer> EFalse = makeList(quads.getNextQuad() - 1);
        stack.push(ETrue);
        stack.push(EFalse);
        stack.push(EType.RELATIONAL);
    }

    private void actionFortyOne(){
        EType etype = (EType)stack.pop();
        try {
            if (etype != EType.ARITHMETIC) {
                throw new SemanticError("Expression Type mismatch: Expected Arithmetic instead of relational");
            }
        }catch(SemanticError e){
            System.err.println(e);
            System.exit(1);
        }

        SymbolTableEntry _id = (SymbolTableEntry) stack.pop();
        Token sign = (Token)stack.pop();

        if(sign.getKey() == "UNARYMINUS"){
            VariableEntry temp = create(tempName(), _id.getType());
            if(_id.getType() == "integer") {
                gen("uminus", _id.name, temp.getName());
            } else {
                gen("fuminus", _id.name, temp.getName());
            }
            stack.push(temp);
        } else {
            stack.push(_id);
        }

        stack.push(EType.ARITHMETIC);
    }

    private void actionFortyTwo(Token tok){
        EType etype = (EType)stack.pop();
        try {
            if (tok.getSymbol() == "OR") {
                if (etype != EType.RELATIONAL) {
                    throw new SemanticError("Expression Type mismatch: Expected relational instead of arithmetic on line " +
                            tok.getLine());
                }

                List<Integer> EFalse = (List<Integer>)stack.peek();
                backpatch(EFalse, String.valueOf(quads.getNextQuad()));
            } else {
                if(etype != EType.ARITHMETIC) {
                    throw new SemanticError("Expression Type mismatch: Expected arithmetic instead of relational on line "
                            + tok.getLine());
                }
            }
        } catch(SemanticError e){
            System.err.println(e);
            System.exit(1);
        }
        String type = tok.getKey();
        if(type == "ADDOP" || type == "MULOP" || type == "RELOP"){
            stack.push(tok);
        }
    }

    private void actionFortyThree(){
        EType etype = (EType)stack.pop();
        if(etype == EType.RELATIONAL){
            List<Integer> E2False = (List<Integer>)stack.pop();
            List<Integer> E2True = (List<Integer>)stack.pop();
            Token operator = (Token)stack.pop();
            List<Integer> E1False = (List<Integer>)stack.pop();
            List<Integer> E1True = (List<Integer>)stack.pop();

            List<Integer> ETrue = merge(E1True, E2True);
            List<Integer> EFalse = E2False;
            stack.push(ETrue);
            stack.push(EFalse);
            stack.push(EType.RELATIONAL);
        } else {

            SymbolTableEntry _id2 = (SymbolTableEntry) stack.pop();
            Token operator = (Token) stack.pop();
            SymbolTableEntry _id1 = (SymbolTableEntry) stack.pop();
            String opcode = operator.getOpCode();

            int type = typeCheck(_id1, _id2);

            if (type == 0) {
                VariableEntry temp = create(tempName(), "integer");
                gen(opcode, _id1.name, _id2.name, temp.name);
                stack.push(temp);
            } else if (type == 1) {
                VariableEntry temp = create(tempName(), "real");
                gen("f" + opcode, _id1.name, _id2.name, temp.name);
                stack.push(temp);
            } else if (type == 2) {
                VariableEntry temp1 = create(tempName(), "real");
                VariableEntry temp2 = create(tempName(), "real");
                gen("ltof", _id2.name, temp1.name);
                gen("f" + opcode, _id1.name, temp1.name, temp2.name);
                stack.push(temp2);
            } else if (type == 3) {
                VariableEntry temp1 = create(tempName(), "real");
                VariableEntry temp2 = create(tempName(), "real");
                gen("ltof", _id1.name, temp1.name);
                gen("f" + opcode, temp1.name, _id2.name, temp2.name);
                stack.push(temp2);
            }

            stack.push(EType.ARITHMETIC);
        }
    }

    private void actionFortyFour(Token tok){
        EType etype = (EType)stack.pop();
        if(etype == EType.RELATIONAL){
            List<Integer> EFalse = (List<Integer>)stack.pop();
            List<Integer> ETrue = (List<Integer>)stack.pop();
            if(tok.getSymbol() == "AND"){
                backpatch(ETrue, String.valueOf(quads.getNextQuad()));
            }
            stack.push(ETrue);
            stack.push(EFalse);
        }
        String type = tok.getKey();
        if(type == "ADDOP" || type == "MULOP" || type == "RELOP"){
            stack.push(tok);
        }
    }

    private void actionFortyFive(){
        EType etype = (EType)stack.pop();
        if(etype == EType.RELATIONAL){
            List<Integer> E2False = (List<Integer>)stack.pop();
            List<Integer> E2True = (List<Integer>)stack.pop();
            Token operator = (Token)stack.pop();

            if(operator.getSymbol() == "AND") {
                List<Integer> E1False = (List<Integer>) stack.pop();
                List<Integer> E1True = (List<Integer>) stack.pop();

                List<Integer> ETrue = E2True;
                List<Integer> EFalse = merge(E1False, E2False);
                stack.push(ETrue);
                stack.push(EFalse);
                stack.push(EType.RELATIONAL);
            }
        } else {

            SymbolTableEntry _id2 = (SymbolTableEntry) stack.pop();
            Token operator = (Token) stack.pop();
            SymbolTableEntry _id1 = (SymbolTableEntry) stack.pop();
            String opcode = operator.getOpCode();

            int type = typeCheck(_id1, _id2);

            try {
                if (type != 0 && (operator.getSymbol() == "DIV" || operator.getSymbol() == "MOD")) {
                    throw new SemanticError("Bad Operator: ");
                }
            } catch (SemanticError e) {
                System.err.println(e + operator.getSymbol() + " operation on line " + operator.getLine() +
                        " requires integers not reals");
                System.exit(1);
            }

            if (type == 0) {
                if (operator.getSymbol() == "MOD") {
                    VariableEntry temp1 = create(tempName(), "integer");
                    VariableEntry temp2 = create(tempName(), "integer");
                    VariableEntry temp3 = create(tempName(), "integer");
                    gen("div", _id1.name, _id2.name, temp1.name);
                    gen("mul", _id2.name, temp1.name, temp2.name);
                    gen("sub", _id1.name, temp2.name, temp3.name);
                    stack.push(temp3);
                } else if (operator.getSymbol() == "/") {
                    VariableEntry temp1 = create(tempName(), "real");
                    VariableEntry temp2 = create(tempName(), "real");
                    VariableEntry temp3 = create(tempName(), "real");
                    gen("ltof", _id1.name, temp1.name);
                    gen("ltof", _id2.name, temp2.name);
                    gen("fdiv", temp1.name, temp2.name, temp3.name);
                    stack.push(temp3);
                } else {
                    VariableEntry temp = create("temp", "integer");
                    gen(opcode, _id1.name, _id2.name, temp.name);
                    stack.push(temp);
                }
            } else if (type == 1) {
                VariableEntry temp = create(tempName(), "real");
                gen("f" + opcode, _id1.name, _id2.name, temp.name);
                stack.push(temp);
            } else if (type == 2) {
                VariableEntry temp1 = create(tempName(), "real");
                VariableEntry temp2 = create(tempName(), "real");
                gen("ltof", _id2.name, temp1.name);
                gen("f" + opcode, _id1.name, temp1.name, temp2.name);
                stack.push(temp2);
            } else if (type == 3) {
                VariableEntry temp1 = create(tempName(), "real");
                VariableEntry temp2 = create(tempName(), "real");
                gen("ltof", _id1.name, temp1.name);
                gen("f" + opcode, temp1.name, _id2.name, temp2.name);
                stack.push(temp2);
            }

            stack.push(EType.ARITHMETIC);
        }
    }

    private void actionFortySix(Token t){
        if(t.getKey() == "IDENTIFIER"){
            try{
                SymbolTableEntry entry = lookupID(t);
                if(entry == null){
                    throw new SemanticError("Undeclared variable ");
                }
                stack.push(lookupID(t));
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
            stack.push(entry);
        }
        stack.push(EType.ARITHMETIC);
    }

    private void actionFortySeven(){
        EType etype = (EType)stack.pop();
        try {
            if (etype != EType.RELATIONAL) {
                throw new SemanticError("Expression Type mismatch: Expected Relational instead of Arithmetic");
            }
        }catch(SemanticError e){
            System.err.println(e);
            System.exit(1);
        }

        List<Integer> EFalse = (List<Integer>)stack.pop();
        List<Integer> ETrue = (List<Integer>)stack.pop();
        stack.push(EFalse);
        stack.push(ETrue);
        stack.push(EType.RELATIONAL);
    }

    private void actionFortyEight(Token t){
        SymbolTableEntry offset = (SymbolTableEntry)stack.pop();
        if(offset != null){
            if(offset.isFunction()) {
                execute("52", t);
            } else {
                SymbolTableEntry _id = (SymbolTableEntry)stack.pop();
                VariableEntry temp = create(tempName(), _id.getType());
                gen("load", _id.name, offset.name, temp.name);
                stack.push(temp);
            }
        }
        stack.push(EType.ARITHMETIC);
    }

    private void actionFortyNine(){
        EType etype = (EType)stack.pop();
        SymbolTableEntry id = (SymbolTableEntry)stack.peek();
        stack.push(etype);

        try{
            if(etype != EType.ARITHMETIC){
                throw new SemanticError("Expected " + id.name + " to be arithmetic not relational");
            }

            if(!id.isFunction()){
                throw new SemanticError("Expected " + id.name + " to be a function");
            }
        } catch(SemanticError e){
            System.err.println(e);
            System.exit(1);
        }

        paramCount.push(0);
        FunctionEntry _id = (FunctionEntry)id;
        paramStack.push(_id.getParameterInfo());
    }

    private void actionFifty(){
        LinkedList<SymbolTableEntry> parameters = new LinkedList<>();
        SymbolTableEntry s = (SymbolTableEntry)stack.peek();
        while(s.isArray() || s.isVariable() || s.getClass().getSimpleName() == "ConstantEntry"){
            parameters.push(s);
            stack.pop();
            if(stack.peek().equals(EType.ARITHMETIC) || stack.peek().equals(EType.RELATIONAL)) {
                break;
            }
            s = (SymbolTableEntry)stack.peek();
        }

        while(!parameters.isEmpty()){
            gen("param " + getConstantOrNotParam(getParamPrefix(parameters.peek()), parameters.pop()));
            localMemory++;
        }

        EType etype = (EType)stack.pop();
        FunctionEntry id = (FunctionEntry)stack.pop();
        int numParams = paramCount.pop();
        try{
            if(numParams > id.getNumberOfParameters()){
                throw new SemanticError("Expected " + id.name + " to have " + id.getNumberOfParameters() + " parameters");
            }
        } catch (SemanticError e){
            System.err.println(e);
            System.exit(1);
        }

        gen("call", id.name, numParams);
        paramStack.pop();
        nextParam = 0;

        VariableEntry temp = create(tempName(), id.getResult().getType());
        gen("move", id.getResult().name, temp.name);
        stack.push(temp);
        stack.push(EType.ARITHMETIC);
    }

    private void actionFiftyOne(Token t){
        LinkedList<SymbolTableEntry> parameters = new LinkedList<>();
        SymbolTableEntry s = (SymbolTableEntry)stack.peek();
        while(s.isArray() || s.isVariable() || s.getClass().getSimpleName() == "ConstantEntry"){
            parameters.push(s);
            stack.pop();
            if(stack.peek().equals(EType.ARITHMETIC) || stack.peek().equals(EType.RELATIONAL)) {
                break;
            }
            s = (SymbolTableEntry) stack.peek();

        }

        EType etype = (EType)stack.pop();
        ProcedureEntry id = (ProcedureEntry)stack.pop();

        if(id.name == "read" || id.name =="write"){
            stack.push(id);
            stack.push(etype);
            while(!parameters.isEmpty()){
                stack.push(parameters.pop());
            }
            if(id.name == "read"){
                fiftyOneRead(t);
            } else {
                fityOneWrite(t);
            }
        } else {
            int numParams = paramCount.pop();
            try {
                if (numParams != id.getNumberOfParameters()) {
                    throw new SemanticError("Expected " + id.name + " to have " + id.getNumberOfParameters() + " parameters");
                }
            } catch(SemanticError e){
                System.err.println(e);
                System.exit(1);
            }

            while(!parameters.isEmpty()){
                gen("param " + getConstantOrNotParam(getParamPrefix(parameters.peek()), parameters.pop()));
                localMemory++;
            }
            insert = false;
            gen("call", id.name, numParams);
            insert = true;
            paramStack.pop();
            nextParam = 0;
        }
    }
    private void fiftyOneRead(Token t){
        LinkedList<SymbolTableEntry> parameters = new LinkedList<>();
        SymbolTableEntry s = (SymbolTableEntry)stack.peek();
        while(s.isVariable()){
            parameters.push(s);
            stack.pop();
            if(stack.peek().equals(EType.ARITHMETIC) || stack.peek().equals(EType.RELATIONAL)) {
                break;
            }
            s = (SymbolTableEntry)stack.peek();
        }

        while (!parameters.isEmpty()){
            SymbolTableEntry id = parameters.pop();
            if(id.getType().toLowerCase().equals("REAL")){
                gen("finp", id.name);
            } else {
                gen("inp", id.name);
            }
        }

        EType etype = (EType)stack.pop();
        SymbolTableEntry id = (SymbolTableEntry)stack.pop();
        paramCount.pop();
    }

    private void fityOneWrite(Token t){
        LinkedList<SymbolTableEntry> parameters = new LinkedList<>();
        SymbolTableEntry s = (SymbolTableEntry)stack.peek();
        while(s.isVariable() || s.getClass().getSimpleName() == "ConstantEntry"){
            parameters.push(s);
            stack.pop();
            if(stack.peek().equals(EType.ARITHMETIC) || stack.peek().equals(EType.RELATIONAL)) {
                break;
            }
            s = (SymbolTableEntry)stack.peek();
        }

        while(!parameters.isEmpty()){
            SymbolTableEntry id = parameters.pop();
            if(id.getClass().getSimpleName() == "ConstantEntry"){
                if(id.getType().toLowerCase().equals("REAL")){
                    gen("foutp "+ id.name);
                } else {
                    gen("outp "+ id.name);
                }
            } else {
                gen("print \"" + id.name + " = \"");
                if(id.getType().equals("REAL")){
                    gen("foutp ", id.name);
                } else {
                    gen("outp ", id.name);
                }
            }
            gen("newl");
        }
        EType etype = (EType)stack.pop();
        SymbolTableEntry id = (SymbolTableEntry)stack.pop();
        paramCount.pop();
    }

    private void actionFiftyTwo(){
        EType etype = (EType)stack.pop();
        SymbolTableEntry id = (SymbolTableEntry)stack.pop();
        try{
            if(!id.isFunction()){
                throw new SemanticError(id.name + " is not a function");
            }
            if(id.getNumberOfParameters() > 0){
                throw new SemanticError("Expected " + id.name + " to have 0 parameters");
            }
        } catch(SemanticError e){
            System.err.println(e);
            System.exit(1);
        }
        gen("call", id.name,0);
        VariableEntry temp = create(tempName(), id.getType());
        gen("move", id.getResult().name, temp.name);
        stack.push(temp);
        stack.push(null);
    }

    private void actionFiftyThree(){
        EType etype = (EType)stack.pop();
        SymbolTableEntry id = (SymbolTableEntry)stack.pop();
        if(id.isFunction()){
            try {
                if (id != currentFunction) {
                    throw new SemanticError("Illegal procedure " + id);
                }
            } catch(SemanticError e){
                System.err.println(e);
            }
            stack.push(id.getResult());
            stack.push(EType.ARITHMETIC);
        } else {
            stack.push(id);
            stack.push(etype);
        }
    }

    private void actionFiftyFour(){
        EType etype = (EType)stack.pop();
        SymbolTableEntry id = (SymbolTableEntry)stack.peek();
        stack.push(etype);
        try{
            if(!id.isProcedure()){
                throw new SemanticError("Expected" + id.name + " to be a procedure");
            }
        } catch(SemanticError e){
            System.err.println(e);
            System.exit(1);
        }
    }

    private void actionFiftyFive(){
        backpatch(makeList(globalStore), String.valueOf(globalMemory));
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
                Float.parseFloat(args[i]);
                SymbolTableEntry e = constantTable.lookup(args[i]);
                if(e != null){
                   //if(!insert) {
                        VariableEntry temp = create(tempName(), e.getType());
                        String[] constant = new String[4];
                        constant[0] = "move";
                        constant[1] = e.getName();
                        constant[2] = getSteAddress(temp.name);
                        constant[3] = null;
                        quads.addQuad(constant);
                        code[i] = getSteAddress(temp.name);
                   // } else {
                    //    code[i] = args[i];
                   // }
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

    private void gen(String action, String method, int args){
        String[] code = new String[4];
        code[0] = action;
        code[1] = method;
        code[2] = String.valueOf(args);
        code[3] = null;

        quads.addQuad(code);
    }

    private String getSteAddress(String s){
        if(global){
            return getConstantOrNot("_", globalTable.lookup(s));
        } else if(localTable.lookup(s) != null){
            if(localTable.lookup(s).isParameter()){
                return getConstantOrNot("^%", localTable.lookup(s));
            } else {
                return getConstantOrNot("%", localTable.lookup(s));
            }
        } else {
            return getConstantOrNot("_", globalTable.lookup(s));
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

    private String getConstantOrNotParam(String pre, SymbolTableEntry e){
        if(e.getClass().getSimpleName() == "ConstantEntry"){
            VariableEntry temp = create(tempName(), e.getType());
            String[] constant = new String[4];
            constant[0] = "move";
            constant[1] = e.getName();
            constant[2] = getSteAddress(temp.name);
            constant[3] = null;
            quads.addQuad(constant);
            return pre + temp.getAddress();
        } else if (e.isArray() || e.isVariable()){
            return pre + e.getAddress();
        } else {
            return e.name;
        }
    }

    String getParamPrefix(SymbolTableEntry id){
        if(global) {
            return "@_";
        } else {
            if(id.isParameter()){
                return "%";
            } else {
                return "@%";
            }
        }
    }
    private void backpatch(List<Integer> l, String x){
        for(Integer i : l){
            String[] q = quads.getQuad(i);
            if(q[0] == "goto" || q[0] == "alloc"){
                quads.setField(i, 1, x);
            } else {
                quads.setField(i, 3, x);
            }
        }
    }

    private List<Integer> makeList(int i){
        List<Integer> l = new ArrayList<>();
        l.add(i);
        return l;
    }

    private List<Integer> merge(List<Integer> list1, List<Integer> list2){
        List<Integer> l = new ArrayList<>();
        l.addAll(list1);
        l.addAll(list2);

        return l;
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

