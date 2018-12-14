import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/*
 * This class contains all the semantic actions required for the VASCAL compiler
 */
public class SemanticAction
{
    private SymbolTable ConstantTable;
    private SymbolTable GlobalTable;
    private SymbolTable LocalTable;

    private Boolean Insert;
    private Boolean Global;
    private Boolean IsArray;
    private int GlobalMemory;
    private int LocalMemory;
    private int TempVars;
    private int GlobalStore;
    private int LocalStore;
    private int NextParam;
    private SymbolTableEntry CurrentFunction;

    //Stack implemented as a linkedlist because it makes the printout look better. For some reason a Stack Object will print bottom up
    private LinkedList<Object> SemanticStack;
    private LinkedList<Integer> ParamCount;
    private LinkedList<List<SymbolTableEntry>> ParamStack;
    private Quadruples Quads;

    private boolean Verbose = false;

    private enum EType
    {
        ARITHMETIC, RELATIONAL
    }

    public SemanticAction()
    {
        ConstantTable = new SymbolTable(100);
        GlobalTable = new SymbolTable(100);
        LocalTable = new SymbolTable(100);

        Insert = true;
        Global = true;
        IsArray = false;
        GlobalMemory = 0;
        LocalMemory = 0;
        TempVars = 0;
        GlobalStore = 0;

        SemanticStack = new LinkedList<>();
        ParamStack = new LinkedList<>();
        Quads = new Quadruples();

        ParamCount = new LinkedList<>();
        ParamCount.push(0);
    }

    //Entry point to the class, calls the actions based on input
    protected void Execute(String action, Token tok)
    {
        if (Verbose)
        {
            System.out.println(tok);
            //globalTable.dumpTable();
        }
        int act = Integer.parseInt(action);
        switch (act)
        {
            case 1:
                Insert = true;
                break;
            case 2:
                Insert = false;
                break;
            case 3:
                ActionThree();
                break;
            case 4:
                ActionFour(tok);
                break;
            case 5:
                ActionFive();
                break;
            case 6:
                IsArray = true;
                break;
            case 7:
                ActionSeven(tok);
                break;
            case 9:
                ActionNine();
                break;
            case 11:
                ActionEleven();
                break;
            case 13:
                ActionThirteen(tok);
                break;
            case 15:
                ActionFifteen(tok);
                break;
            case 16:
                ActionSixteen();
                break;
            case 17:
                ActionSeventeen(tok);
                break;
            case 19:
                ActionNineteen();
                break;
            case 20:
                ActionTwenty();
                break;
            case 21:
                ActionTwentyOne();
                break;
            case 22:
                ActionTwentyTwo(tok);
                break;
            case 24:
                ActionTwentyFour();
                break;
            case 25:
                ActionTwentyFive(tok);
                break;
            case 26:
                ActionTwentySix();
                break;
            case 27:
                ActionTwentySeven();
                break;
            case 28:
                ActionTwentyEight();
                break;
            case 29:
                ActionTwentyNine();
                break;
            case 30:
                ActionThirty(tok);
                break;
            case 31:
                ActionThirtyOne(tok);
                break;
            case 32:
                ActionThirtyTwo();
                break;
            case 33:
                ActionThirtyThree();
                break;
            case 34:
                ActionThirtyFour(tok);
                break;
            case 35:
                ActionThirtyFive();
                break;
            case 36:
                ActionThirtySix();
                break;
            case 37:
                ActionThirtySeven();
                break;
            case 38:
                ActionThirtyEight(tok);
                break;
            case 39:
                ActionThirtyNine();
                break;
            case 40:
                if (tok.GetKey() == "UNARYPLUS" || tok.GetKey() == "UNARYMINUS")
                {
                    SemanticStack.push(tok);
                }
                break;
            case 41:
                ActionFortyOne();
                break;
            case 42:
                ActionFortyTwo(tok);
                break;
            case 43:
                ActionFortyThree();
                break;
            case 44:
                ActionFortyFour(tok);
                break;
            case 45:
                ActionFortyFive();
                break;
            case 46:
                ActionFortySix(tok);
                break;
            case 47:
                ActionFortySeven();
                break;
            case 48:
                ActionFortyEight(tok);
                break;
            case 49:
                ActionFortyNine();
                break;
            case 50:
                ActionFifty();
                break;
            case 51:
                ActionFiftyOne(tok);
                break;
            case 52:
                ActionFiftyTwo();
                break;
            case 53:
                ActionFiftyThree();
                break;
            case 54:
                ActionFiftyFour();
                break;
            case 55:
                ActionFiftyFive();
                break;
            case 56:
                ActionFiftySix();
                break;
            default:
                if (Verbose)
                {
                    System.out.println("Action " + act + " not yet implemented");
                }
        }
        if (Verbose)
        {
            StackDump();
            //globalTable.dumpTable();
        }
    }
    /* ***************************
     * SEMANTIC ACTIONS BEGIN HERE
     * ***************************
     */
    private void ActionThree()
    {
        Token tok = (Token) SemanticStack.pop();
        String type = tok.GetKey();
        if (IsArray)
        {
            Token tok1 = (Token) SemanticStack.pop();
            Token tok2 = (Token) SemanticStack.pop();
            int upperBound = tok1.ToInt();
            int lowerBound = tok2.ToInt();
            int memorySize = (upperBound - lowerBound) + 1;

            Token test = (Token) SemanticStack.peek();
            while (test.GetKey() == "IDENTIFIER")
            {
                Token t = (Token) SemanticStack.pop();
                ArrayEntry id = new ArrayEntry(t.GetValue());
                id.SetType(type);
                id.SetUpperBound(upperBound);
                id.SetLowerBound(lowerBound);

                if (Global)
                {
                    id.SetAddress(GlobalMemory);
                    GlobalTable.Insert(id);
                    GlobalMemory += memorySize;
                } else
                {
                    id.SetAddress(LocalMemory);
                    LocalTable.Insert(id);
                    LocalMemory += memorySize;
                }

                if (SemanticStack.peek() == null)
                {
                    break;
                }
                test = (Token) SemanticStack.peek();
            }
        } else
        {
            Token test = (Token) SemanticStack.peek();
            while (test.GetKey() == "IDENTIFIER")
            {
                Token t = (Token) SemanticStack.pop();
                VariableEntry id = new VariableEntry(t.GetValue());
                id.SetType(type);

                if (Global)
                {
                    id.SetAddress(GlobalMemory);
                    GlobalTable.Insert(id);
                    GlobalMemory++;
                } else
                {
                    id.SetAddress(LocalMemory);
                    LocalTable.Insert(id);
                    LocalMemory++;
                }

                if (SemanticStack.peek() == null || SemanticStack.peek().getClass().getSimpleName() != "Token")
                {
                    break;
                }
                test = (Token) SemanticStack.peek();
            }
        }

        //globalTable.dumpTable();

        IsArray = false;
    }

    private void ActionFour(Token t)
    {
        String type = t.GetKey();
        if (type.equals("INTEGER") || type.equals("REAL"))
        {
            SemanticStack.push(t);
        }
    }

    private void ActionFive()
    {
        Insert = false;
        SymbolTableEntry id = (SymbolTableEntry) SemanticStack.pop();
        Gen("PROCBEGIN", id.GetName());
        LocalStore = Quads.GetNextQuad();
        Gen("alloc", "");
    }

    private void ActionSeven(Token t)
    {
        if (t.GetKey() == "INTCONSTANT")
        {
            SemanticStack.push(t);
        }
    }

    private void ActionNine()
    {
        Token id1 = (Token) SemanticStack.pop();
        Token id2 = (Token) SemanticStack.pop();
        Token id3 = (Token) SemanticStack.pop();
        //if(verbose) { System.out.println(id1 + " " + id2 + " " + id3);}

        IODeviceEntry entry1 = new IODeviceEntry(id1.GetValue());
        IODeviceEntry entry2 = new IODeviceEntry(id2.GetValue());
        ProcedureEntry entry3 = new ProcedureEntry(id3.GetValue(), 0);
        //if(verbose) {System.out.println(entry1 + " " + entry2 + " " + entry3);}

        ProcedureEntry entry4 = new ProcedureEntry("main", 0);
        ProcedureEntry entry5 = new ProcedureEntry("read", 0);
        ProcedureEntry entry6 = new ProcedureEntry("write", 0);

        entry1.SetReserved(true);
        entry2.SetReserved(true);
        entry3.SetReserved(true);
        entry4.SetReserved(true);
        entry5.SetReserved(true);
        entry6.SetReserved(true);

        GlobalTable.Insert(entry1);
        GlobalTable.Insert(entry2);
        GlobalTable.Insert(entry3);
        GlobalTable.Insert(entry4);
        GlobalTable.Insert(entry5);
        GlobalTable.Insert(entry6);

        Insert = false;

        Gen("call", "main", 0);
        Gen("exit");
    }

    private void ActionEleven()
    {
        Global = true;
        LocalTable = new SymbolTable(100);
        CurrentFunction = null;
        Backpatch(MakeList(LocalStore), String.valueOf(LocalMemory));
        Gen("free", String.valueOf(LocalMemory));
        Gen("PROCEND");
    }

    private void ActionThirteen(Token t)
    {
        if (t.GetKey() == "IDENTIFIER")
        {
            SemanticStack.push(t);
        }
    }

    private void ActionFifteen(Token t)
    {
        VariableEntry result = Create(t.GetValue() + "_RESULT", "integer");
        result.SetResult();
        SymbolTableEntry id = new FunctionEntry(t.GetValue(), result);

        GlobalTable.Insert(id);
        Global = false;
        LocalMemory = 0;
        CurrentFunction = id;
        SemanticStack.push(id);
    }

    private void ActionSixteen()
    {
        Token type = (Token) SemanticStack.pop();
        FunctionEntry id = (FunctionEntry) SemanticStack.peek();
        id.SetType(type.GetKey());
        id.setResultType(type.GetKey());
        CurrentFunction = id;
    }

    private void ActionSeventeen(Token t)
    {
        SymbolTableEntry id = new ProcedureEntry(t.GetValue());
        GlobalTable.Insert(id);
        Global = false;
        LocalMemory = 0;
        CurrentFunction = id;
        SemanticStack.push(id);
    }

    private void ActionNineteen()
    {
        ParamStack = new LinkedList<>();
        ParamCount.push(0);
    }

    private void ActionTwenty()
    {
        SymbolTableEntry id = (SymbolTableEntry) SemanticStack.peek();
        int numParams = ParamCount.pop();
        id.SetNumberOfParameters(numParams);
    }

    private void ActionTwentyOne()
    {
        Token type = (Token) SemanticStack.pop();
        int upperBound = 1;
        int lowerBound = 1;

        if (IsArray)
        {
            Token ub = (Token) SemanticStack.pop();
            Token lb = (Token) SemanticStack.pop();
            upperBound = Integer.parseInt(ub.GetValue());
            lowerBound = Integer.parseInt(lb.GetValue());
        }

        LinkedList<Token> parameters = new LinkedList<>();

        Token t = (Token) SemanticStack.peek();
        while (t.GetKey() == "IDENTIFIER")
        {
            parameters.push((Token) SemanticStack.pop());
            if (SemanticStack.peek().getClass().getSimpleName() != "Token")
            {
                break;
            }
            t = (Token) SemanticStack.peek();
        }

        while (!parameters.isEmpty())
        {
            Token param = parameters.pop();
            SymbolTableEntry var;
            if (IsArray)
            {
                var = new ArrayEntry(param.GetValue(), LocalMemory, type.GetKey(), upperBound, lowerBound);
            } else
            {
                var = new VariableEntry(param.GetValue(), LocalMemory, type.GetKey());
            }
            var.SetParameter();
            LocalTable.Insert(var);
            CurrentFunction.AddParameter(var);
            LocalMemory++;
            ParamCount.push(ParamCount.pop() + 1);
        }

        IsArray = false;

    }

    private void ActionTwentyTwo(Token t)
    {
        EType etype = (EType) SemanticStack.pop();
        try
        {
            if (etype != EType.RELATIONAL)
            {
                throw new SemanticError("Expression Type mismatch: Expected relational instead of arithmetic on line "
                        + t.GetLine());
            }
        } catch (SemanticError e)
        {
            System.err.println(e);
            System.exit(1);
        }

        List<Integer> EFalse = (List<Integer>) SemanticStack.pop();
        List<Integer> ETrue = (List<Integer>) SemanticStack.pop();
        Backpatch(ETrue, String.valueOf(Quads.GetNextQuad()));
        SemanticStack.push(ETrue);
        SemanticStack.push(EFalse);
    }

    private void ActionTwentyFour()
    {
        int beginLoop = Quads.GetNextQuad();
        SemanticStack.push(beginLoop);
    }

    private void ActionTwentyFive(Token t)
    {
        //As far as I can tell, these actions are the same
        ActionTwentyTwo(t);
    }

    private void ActionTwentySix()
    {
        List<Integer> EFalse = (List<Integer>) SemanticStack.pop();
        List<Integer> ETrue = (List<Integer>) SemanticStack.pop();
        int beginLoop = (Integer) SemanticStack.pop();
        Gen("goto", String.valueOf(beginLoop));
        Backpatch(EFalse, String.valueOf(Quads.GetNextQuad()));
    }

    private void ActionTwentySeven()
    {
        List<Integer> skipElse = MakeList(Quads.GetNextQuad());
        Gen("goto", "");
        List<Integer> EFalse = (List<Integer>) SemanticStack.pop();
        List<Integer> ETrue = (List<Integer>) SemanticStack.pop();
        Backpatch(EFalse, String.valueOf(Quads.GetNextQuad()));
        SemanticStack.push(skipElse);
        SemanticStack.push(ETrue);
        SemanticStack.push(EFalse);
    }

    private void ActionTwentyEight()
    {
        List<Integer> EFalse = (List<Integer>) SemanticStack.pop();
        List<Integer> ETrue = (List<Integer>) SemanticStack.pop();
        List<Integer> skipElse = (List<Integer>) SemanticStack.pop();
        Backpatch(skipElse, String.valueOf(Quads.GetNextQuad()));
    }

    private void ActionTwentyNine()
    {
        List<Integer> EFalse = (List<Integer>) SemanticStack.pop();
        List<Integer> ETrue = (List<Integer>) SemanticStack.pop();
        Backpatch(EFalse, String.valueOf(Quads.GetNextQuad()));
    }


    private void ActionThirty(Token t)
    {
        try
        {
            if (LookupID(t) != null)
            {
                SemanticStack.push(LookupID(t));
                SemanticStack.push(EType.ARITHMETIC);
            } else
            {
                throw new SemanticError("Undeclared variable");
            }
        } catch (SemanticError e)
        {
            System.err.println(e + " " + t.GetValue() + " on line " + t.GetLine());
            System.exit(1);
        }
    }

    private void ActionThirtyOne(Token t)
    {
        EType etype = (EType) SemanticStack.pop();
        try
        {
            if (etype != EType.ARITHMETIC)
            {
                throw new SemanticError("Expression Type mismatch: Expected Arithmetic instead of relational" +
                        " expression on line " + t.GetLine());
            }
        } catch (SemanticError e)
        {
            System.err.println(e);
            System.exit(1);
        }

        SymbolTableEntry Id2 = (SymbolTableEntry) SemanticStack.pop();
        SymbolTableEntry Offset = (SymbolTableEntry) SemanticStack.pop();
        SymbolTableEntry Id1 = (SymbolTableEntry) SemanticStack.pop();

        int type = TypeCheck(Id1, Id2);

        try
        {
            if (type == 3)
            {
                throw new SemanticError("Type mismatch. Can not assign real to int");
            } else if (type == 2)
            {
                VariableEntry temp = Create(TempName(), "real");
                Gen("ltof", Id2.GetName(), temp.GetName());
                if (Offset == null)
                {
                    Gen("move", temp.GetName(), Id1.GetName());
                } else
                {
                    Gen("stor", temp.GetName(), Offset.GetName(), Id1.GetName());
                }
            } else
            {
                if (Offset == null)
                {
                    Gen("move", Id2.GetName(), Id1.GetName());
                } else
                {
                    Gen("stor", Id2.GetName(), Offset.GetName(), Id1.GetName());
                }
            }
        } catch (SemanticError e)
        {
            System.err.println(e);//+ " on line " + Id2.getLine());
            System.exit(1);
        }
    }

    private void ActionThirtyTwo()
    {
        EType etype = (EType) SemanticStack.pop();
        SymbolTableEntry id = (SymbolTableEntry) SemanticStack.peek();
        try
        {
            if (etype != EType.ARITHMETIC)
            {
                throw new SemanticError("Expression type Mismatch. Require arithmetic expression");
            }
            if (!id.GetArray())
            {
                throw new SemanticError(id + " is not an array");
            }
        } catch (SemanticError e)
        {
            System.err.println(e);
            System.exit(1);
        }
    }

    private void ActionThirtyThree()
    {
        EType etype = (EType) SemanticStack.pop();
        SymbolTableEntry id = (SymbolTableEntry) SemanticStack.pop();
        try
        {
            if (etype != EType.ARITHMETIC)
            {
                throw new SemanticError("Expression Type mismatch");
            }
            if (!id.GetType().toLowerCase().equals("integer"))
            {
                throw new SemanticError("Type mismatch, expected " + id.GetName() + " to be in an integer");
            }
        } catch (SemanticError e)
        {
            System.err.println(e);
            System.exit(1);
        }
        ArrayEntry arr = (ArrayEntry) SemanticStack.peek();
         /*
            If Statement necessary because my Gen function will move any constants in the constant table into temp
            variables. It was implemented in a way that requires the constant to already exist however. This created
            a problem where, if a constant already existed, Gen would move it into a variable and then the move in this
            method would move that variable into another temp variable, which is redundant. But if a constant is not
            already in the table, the move in this method is necessary. This is all in order to make the intermediate
            code look like the examples. i.e. without it, the code looks like:
            mov 1, 49
            mov 49, 47
            mov 2, 50
            sub 50, 47, 48

            instead of:
            move 2, 47
            move 1, 48
            sub 47, 48, 46

            I'm planning on cleaning up this implementation but I'm not sure if it's
            possible.
         */
        if (ConstantTable.Lookup(String.valueOf(arr.lowerBound)) == null)
        {
            VariableEntry temp1 = Create(TempName(), "integer");
            VariableEntry temp2 = Create(TempName(), "integer");
            Gen("move", String.valueOf(arr.lowerBound), temp1.GetName());
            Gen("sub", id.GetName(), temp1.GetName(), temp2.GetName());
            SemanticStack.push(temp2);
        } else
        {
            VariableEntry temp = Create(TempName(), "integer");
            Insert = false;
            Gen("sub", id.GetName(), String.valueOf(arr.lowerBound), temp.GetName());
            Insert = true;
            SemanticStack.push(temp);
        }


    }

    private void ActionThirtyFour(Token t)
    {
        EType etype = (EType) SemanticStack.pop();
        SymbolTableEntry id = (SymbolTableEntry) SemanticStack.peek();
        if (id.GetFunction())
        {
            SemanticStack.push(etype);
            Execute("52", t);
        } else
        {
            SemanticStack.push(null);
        }
    }

    private void ActionThirtyFive()
    {
        EType etype = (EType) SemanticStack.pop();
        ProcedureEntry id = (ProcedureEntry) SemanticStack.peek();
        SemanticStack.push(etype);
        ParamCount.push(0);
        ParamStack.push(id.ParameterInfo);
    }

    private void ActionThirtySix()
    {
        EType etype = (EType) SemanticStack.pop();
        ProcedureEntry id = (ProcedureEntry) SemanticStack.pop();
        try
        {
            if (id.NumberOfParameters != 0)
            {
                throw new SemanticError("Wrong number of parameters. Expected 0 in procedure: " + id.GetName());
            }
        } catch (SemanticError e)
        {
            System.err.println(e);
            System.exit(1);
        }
        Insert = true;
        Gen("call", id.GetName(), 0);
        Insert = false;
    }

    private void ActionThirtySeven()
    {
        EType etype = (EType) SemanticStack.pop();
        try
        {
            if (etype != EType.ARITHMETIC)
            {
                throw new SemanticError("Expression Type mismatch: Expected Arithmetic instead of relational");
            }

            SymbolTableEntry id = (SymbolTableEntry) SemanticStack.peek();
            if (!(id.GetVariable() || id.GetFunctionResult() || id.GetArray() || id.getClass().getSimpleName() == "ConstantEntry"))
            {
                throw new SemanticError("Unexpected Parameter " + id.GetName());
            }
            ParamCount.push(ParamCount.pop() + 1);

            LinkedList<SymbolTableEntry> parameters = new LinkedList<>();

            SymbolTableEntry s = (SymbolTableEntry) SemanticStack.peek();
            while (!(s.GetFunction() || s.IsProcedure()))
            {
                parameters.push(s);
                SemanticStack.pop();
                if (SemanticStack.peek().equals(EType.ARITHMETIC) || SemanticStack.peek().equals(EType.RELATIONAL))
                {
                    SemanticStack.pop();
                    break;
                }
                s = (SymbolTableEntry) SemanticStack.peek();
            }

            SymbolTableEntry funcID = (SymbolTableEntry) SemanticStack.peek();
            SemanticStack.push(etype);
            while (!parameters.isEmpty())
            {
                SemanticStack.push(parameters.pop());
            }

            if (!(funcID.GetName() == "read" || funcID.GetName() == "write"))
            {
                if (ParamCount.peek() > funcID.GetNumberOfParameters())
                {
                    throw new SemanticError("Wrong number of parameters for " + funcID.GetName() + ". Expected " +
                            funcID.GetNumberOfParameters());
                }

                SymbolTableEntry param = ParamStack.peek().get(NextParam);
                if (!id.GetType().toLowerCase().equals(param.GetType().toLowerCase()))
                {
                    throw new SemanticError("Bad parameter type, expected " + param.GetName() + " to be a(n) " + id.GetType());
                }
                if (param.GetArray())
                {
                    ArrayEntry Id = (ArrayEntry) id;
                    ArrayEntry Param = (ArrayEntry) param;
                    if ((Id.GetLowerBound() != Param.GetLowerBound()) ||
                            (Id.GetUpperBound() != Param.GetUpperBound()))
                    {
                        throw new SemanticError("Expected " + param.GetName() + " to have a lower bound of " +
                                Id.GetLowerBound() + " and an upper bound of " + Id.GetUpperBound());
                    }
                }
                NextParam++;
            }
        } catch (SemanticError e)
        {
            System.err.println(e);
            System.exit(1);
        }
    }

    private void ActionThirtyEight(Token t)
    {
        EType etype = (EType) SemanticStack.pop();
        try
        {
            if (etype != EType.ARITHMETIC)
            {
                throw new SemanticError("Expression Type mismatch: Expected Arithmetic instead of relational" +
                        "on line " + t.GetLine());
            }
        } catch (SemanticError e)
        {
            System.err.println(e);
            System.exit(1);
        }

        String type = t.GetKey();
        if (type == "ADDOP" || type == "MULOP" || type == "RELOP")
        {
            SemanticStack.push(t);
        }
    }

    private void ActionThirtyNine()
    {
        EType etype = (EType) SemanticStack.pop();
        try
        {
            if (etype != EType.ARITHMETIC)
            {
                throw new SemanticError("Expression Type mismatch: Expected Arithmetic instead of relational");
            }
        } catch (SemanticError e)
        {
            System.err.println(e);
            System.exit(1);
        }

        SymbolTableEntry id2 = (SymbolTableEntry) SemanticStack.pop();
        Token operator = (Token) SemanticStack.pop();
        String opcode = operator.GetOpCode();
        SymbolTableEntry id1 = (SymbolTableEntry) SemanticStack.pop();

        int type = TypeCheck(id1, id2);

        if (type == 2)
        {
            VariableEntry temp = Create(TempName(), "real");
            Gen("ltof", id2.GetName(), temp.GetName());
            Gen(opcode, id1.GetName(), temp.GetName(), "");
        } else if (type == 3)
        {
            VariableEntry temp = Create(TempName(), "real");
            Gen("ltof", id1.GetName(), id2.GetName());
            Gen(opcode, temp.GetName(), id2.GetName(), "");
        } else
        {
            //insert = false;
            Gen(opcode, id1.GetName(), id2.GetName(), "");
            //insert = true;
        }

        Gen("goto", "");
        List<Integer> ETrue = MakeList(Quads.GetNextQuad() - 2);
        List<Integer> EFalse = MakeList(Quads.GetNextQuad() - 1);
        SemanticStack.push(ETrue);
        SemanticStack.push(EFalse);
        SemanticStack.push(EType.RELATIONAL);
    }

    private void ActionFortyOne()
    {
        EType etype = (EType) SemanticStack.pop();
        try
        {
            if (etype != EType.ARITHMETIC)
            {
                throw new SemanticError("Expression Type mismatch: Expected Arithmetic instead of relational");
            }
        } catch (SemanticError e)
        {
            System.err.println(e);
            System.exit(1);
        }

        SymbolTableEntry Id = (SymbolTableEntry) SemanticStack.pop();
        Token sign = (Token) SemanticStack.pop();

        if (sign.GetKey() == "UNARYMINUS")
        {
            VariableEntry temp = Create(TempName(), Id.GetType());
            if (Id.GetType() == "integer")
            {
                Gen("uminus", Id.GetName(), temp.GetName());
            } else
            {
                Gen("fuminus", Id.GetName(), temp.GetName());
            }
            SemanticStack.push(temp);
        } else
        {
            SemanticStack.push(Id);
        }

        SemanticStack.push(EType.ARITHMETIC);
    }

    private void ActionFortyTwo(Token tok)
    {
        EType etype = (EType) SemanticStack.pop();
        try
        {
            if (tok.GetSymbol() == "OR")
            {
                if (etype != EType.RELATIONAL)
                {
                    throw new SemanticError("Expression Type mismatch: Expected relational instead of arithmetic on line " +
                            tok.GetLine());
                }

                List<Integer> EFalse = (List<Integer>) SemanticStack.peek();
                Backpatch(EFalse, String.valueOf(Quads.GetNextQuad()));
            } else
            {
                if (etype != EType.ARITHMETIC)
                {
                    throw new SemanticError("Expression Type mismatch: Expected arithmetic instead of relational on line "
                            + tok.GetLine());
                }
            }
        } catch (SemanticError e)
        {
            System.err.println(e);
            System.exit(1);
        }
        String type = tok.GetKey();
        if (type == "ADDOP" || type == "MULOP" || type == "RELOP")
        {
            SemanticStack.push(tok);
        }
    }

    private void ActionFortyThree()
    {
        EType etype = (EType) SemanticStack.pop();
        if (etype == EType.RELATIONAL)
        {
            List<Integer> E2False = (List<Integer>) SemanticStack.pop();
            List<Integer> E2True = (List<Integer>) SemanticStack.pop();
            Token operator = (Token) SemanticStack.pop();
            List<Integer> E1False = (List<Integer>) SemanticStack.pop();
            List<Integer> E1True = (List<Integer>) SemanticStack.pop();

            List<Integer> ETrue = Merge(E1True, E2True);
            List<Integer> EFalse = E2False;
            SemanticStack.push(ETrue);
            SemanticStack.push(EFalse);
            SemanticStack.push(EType.RELATIONAL);
        } else
        {

            SymbolTableEntry Id2 = (SymbolTableEntry) SemanticStack.pop();
            Token operator = (Token) SemanticStack.pop();
            SymbolTableEntry Id1 = (SymbolTableEntry) SemanticStack.pop();
            String opcode = operator.GetOpCode();

            int type = TypeCheck(Id1, Id2);

            if (type == 0)
            {
                VariableEntry temp = Create(TempName(), "integer");
                Gen(opcode, Id1.GetName(), Id2.GetName(), temp.GetName());
                SemanticStack.push(temp);
            } else if (type == 1)
            {
                VariableEntry temp = Create(TempName(), "real");
                Gen("f" + opcode, Id1.GetName(), Id2.GetName(), temp.GetName());
                SemanticStack.push(temp);
            } else if (type == 2)
            {
                VariableEntry temp1 = Create(TempName(), "real");
                VariableEntry temp2 = Create(TempName(), "real");
                Gen("ltof", Id2.GetName(), temp1.GetName());
                Gen("f" + opcode, Id1.GetName(), temp1.GetName(), temp2.GetName());
                SemanticStack.push(temp2);
            } else if (type == 3)
            {
                VariableEntry temp1 = Create(TempName(), "real");
                VariableEntry temp2 = Create(TempName(), "real");
                Gen("ltof", Id1.GetName(), temp1.GetName());
                Gen("f" + opcode, temp1.GetName(), Id2.GetName(), temp2.GetName());
                SemanticStack.push(temp2);
            }

            SemanticStack.push(EType.ARITHMETIC);
        }
    }

    private void ActionFortyFour(Token tok)
    {
        EType etype = (EType) SemanticStack.pop();
        if (etype == EType.RELATIONAL)
        {
            List<Integer> EFalse = (List<Integer>) SemanticStack.pop();
            List<Integer> ETrue = (List<Integer>) SemanticStack.pop();
            if (tok.GetSymbol() == "AND")
            {
                Backpatch(ETrue, String.valueOf(Quads.GetNextQuad()));
            }
            SemanticStack.push(ETrue);
            SemanticStack.push(EFalse);
        }
        String type = tok.GetKey();
        if (type == "ADDOP" || type == "MULOP" || type == "RELOP")
        {
            SemanticStack.push(tok);
        }
    }

    private void ActionFortyFive()
    {
        EType etype = (EType) SemanticStack.pop();
        if (etype == EType.RELATIONAL)
        {
            List<Integer> E2False = (List<Integer>) SemanticStack.pop();
            List<Integer> E2True = (List<Integer>) SemanticStack.pop();
            Token operator = (Token) SemanticStack.pop();

            if (operator.GetSymbol() == "AND")
            {
                List<Integer> E1False = (List<Integer>) SemanticStack.pop();
                List<Integer> E1True = (List<Integer>) SemanticStack.pop();

                List<Integer> ETrue = E2True;
                List<Integer> EFalse = Merge(E1False, E2False);
                SemanticStack.push(ETrue);
                SemanticStack.push(EFalse);
                SemanticStack.push(EType.RELATIONAL);
            }
        } else
        {

            SymbolTableEntry Id2 = (SymbolTableEntry) SemanticStack.pop();
            Token operator = (Token) SemanticStack.pop();
            SymbolTableEntry Id1 = (SymbolTableEntry) SemanticStack.pop();
            String opcode = operator.GetOpCode();

            int type = TypeCheck(Id1, Id2);

            try
            {
                if (type != 0 && (operator.GetSymbol() == "DIV" || operator.GetSymbol() == "MOD"))
                {
                    throw new SemanticError("Bad Operator: ");
                }
            } catch (SemanticError e)
            {
                System.err.println(e + operator.GetSymbol() + " operation on line " + operator.GetLine() +
                        " requires integers not reals");
                System.exit(1);
            }

            if (type == 0)
            {
                if (operator.GetSymbol() == "MOD")
                {
                    VariableEntry temp1 = Create(TempName(), "integer");
                    VariableEntry temp2 = Create(TempName(), "integer");
                    VariableEntry temp3 = Create(TempName(), "integer");
                    Gen("div", Id1.GetName(), Id2.GetName(), temp1.GetName());
                    Gen("mul", Id2.GetName(), temp1.GetName(), temp2.GetName());
                    Gen("sub", Id1.GetName(), temp2.GetName(), temp3.GetName());
                    SemanticStack.push(temp3);
                } else if (operator.GetSymbol() == "/")
                {
                    VariableEntry temp1 = Create(TempName(), "real");
                    VariableEntry temp2 = Create(TempName(), "real");
                    VariableEntry temp3 = Create(TempName(), "real");
                    Gen("ltof", Id1.GetName(), temp1.GetName());
                    Gen("ltof", Id2.GetName(), temp2.GetName());
                    Gen("fdiv", temp1.GetName(), temp2.GetName(), temp3.GetName());
                    SemanticStack.push(temp3);
                } else
                {
                    VariableEntry temp = Create("temp", "integer");
                    Gen(opcode, Id1.GetName(), Id2.GetName(), temp.GetName());
                    SemanticStack.push(temp);
                }
            } else if (type == 1)
            {
                VariableEntry temp = Create(TempName(), "real");
                Gen("f" + opcode, Id1.GetName(), Id2.GetName(), temp.GetName());
                SemanticStack.push(temp);
            } else if (type == 2)
            {
                VariableEntry temp1 = Create(TempName(), "real");
                VariableEntry temp2 = Create(TempName(), "real");
                Gen("ltof", Id2.GetName(), temp1.GetName());
                Gen("f" + opcode, Id1.GetName(), temp1.GetName(), temp2.GetName());
                SemanticStack.push(temp2);
            } else if (type == 3)
            {
                VariableEntry temp1 = Create(TempName(), "real");
                VariableEntry temp2 = Create(TempName(), "real");
                Gen("ltof", Id1.GetName(), temp1.GetName());
                Gen("f" + opcode, temp1.GetName(), Id2.GetName(), temp2.GetName());
                SemanticStack.push(temp2);
            }

            SemanticStack.push(EType.ARITHMETIC);
        }
    }

    private void ActionFortySix(Token t)
    {
        if (t.GetKey() == "IDENTIFIER")
        {
            try
            {
                SymbolTableEntry entry = LookupID(t);
                if (entry == null)
                {
                    throw new SemanticError("Undeclared variable ");
                }
                SemanticStack.push(LookupID(t));
            } catch (SemanticError e)
            {
                System.err.println(e + t.GetValue() + " on line " + t.GetLine());
                System.exit(1);
            }
        } else if (t.GetKey() == "INTCONSTANT" || t.GetKey() == "REALCONSTANT")
        {
            SymbolTableEntry entry = LookupConstant(t);
            if (entry == null)
            {
                if (t.GetKey() == "INTCONSTANT")
                {
                    entry = new ConstantEntry(t.GetValue(), "integer");
                } else if (t.GetKey() == "REALCONSTANT")
                {
                    entry = new ConstantEntry(t.GetValue(), "real");
                }
                ConstantTable.Insert(entry);
            }
            SemanticStack.push(entry);
        }
        SemanticStack.push(EType.ARITHMETIC);
    }

    private void ActionFortySeven()
    {
        EType etype = (EType) SemanticStack.pop();
        try
        {
            if (etype != EType.RELATIONAL)
            {
                throw new SemanticError("Expression Type mismatch: Expected Relational instead of Arithmetic");
            }
        } catch (SemanticError e)
        {
            System.err.println(e);
            System.exit(1);
        }

        List<Integer> EFalse = (List<Integer>) SemanticStack.pop();
        List<Integer> ETrue = (List<Integer>) SemanticStack.pop();
        SemanticStack.push(EFalse);
        SemanticStack.push(ETrue);
        SemanticStack.push(EType.RELATIONAL);
    }

    private void ActionFortyEight(Token t)
    {
        SymbolTableEntry offset = (SymbolTableEntry) SemanticStack.pop();
        if (offset != null)
        {
            if (offset.GetFunction())
            {
                Execute("52", t);
            } else
            {
                SymbolTableEntry Id = (SymbolTableEntry) SemanticStack.pop();
                VariableEntry temp = Create(TempName(), Id.GetType());
                Gen("load", Id.GetName(), offset.GetName(), temp.GetName());
                SemanticStack.push(temp);
            }
        }
        SemanticStack.push(EType.ARITHMETIC);
    }

    private void ActionFortyNine()
    {
        EType etype = (EType) SemanticStack.pop();
        SymbolTableEntry id = (SymbolTableEntry) SemanticStack.peek();
        SemanticStack.push(etype);

        try
        {
            if (etype != EType.ARITHMETIC)
            {
                throw new SemanticError("Expected " + id.GetName() + " to be arithmetic not relational");
            }

            if (!id.GetFunction())
            {
                throw new SemanticError("Expected " + id.GetName() + " to be a function");
            }
        } catch (SemanticError e)
        {
            System.err.println(e);
            System.exit(1);
        }

        ParamCount.push(0);
        FunctionEntry Id = (FunctionEntry) id;
        ParamStack.push(Id.GetParameterInfo());
    }

    private void ActionFifty()
    {
        LinkedList<SymbolTableEntry> parameters = new LinkedList<>();
        SymbolTableEntry s = (SymbolTableEntry) SemanticStack.peek();
        while (s.GetArray() || s.GetVariable() || s.getClass().getSimpleName() == "ConstantEntry")
        {
            parameters.push(s);
            SemanticStack.pop();
            if (SemanticStack.peek().equals(EType.ARITHMETIC) || SemanticStack.peek().equals(EType.RELATIONAL))
            {
                break;
            }
            s = (SymbolTableEntry) SemanticStack.peek();
        }

        while (!parameters.isEmpty())
        {
            Gen("param " + GetSTEAddressParam(GetParamPrefix(parameters.peek()), parameters.pop()));
            LocalMemory++;
        }

        EType etype = (EType) SemanticStack.pop();
        FunctionEntry id = (FunctionEntry) SemanticStack.pop();
        int numParams = ParamCount.pop();
        try
        {
            if (numParams > id.GetNumberOfParameters())
            {
                throw new SemanticError("Expected " + id.GetName() + " to have " + id.GetNumberOfParameters() + " parameters");
            }
        } catch (SemanticError e)
        {
            System.err.println(e);
            System.exit(1);
        }

        Gen("call", id.GetName(), numParams);
        ParamStack.pop();
        NextParam = 0;

        VariableEntry temp = Create(TempName(), id.GetResult().GetType());
        Gen("move", id.GetResult().GetName(), temp.GetName());
        SemanticStack.push(temp);
        SemanticStack.push(EType.ARITHMETIC);
    }

    private void ActionFiftyOne(Token t)
    {
        LinkedList<SymbolTableEntry> parameters = new LinkedList<>();
        SymbolTableEntry s = (SymbolTableEntry) SemanticStack.peek();
        while (s.GetArray() || s.GetVariable() || s.getClass().getSimpleName() == "ConstantEntry")
        {
            parameters.push(s);
            SemanticStack.pop();
            if (SemanticStack.peek().equals(EType.ARITHMETIC) || SemanticStack.peek().equals(EType.RELATIONAL))
            {
                break;
            }
            s = (SymbolTableEntry) SemanticStack.peek();

        }

        EType etype = (EType) SemanticStack.pop();
        ProcedureEntry id = (ProcedureEntry) SemanticStack.pop();

        if (id.GetName() == "read" || id.GetName() == "write")
        {
            SemanticStack.push(id);
            SemanticStack.push(etype);
            while (!parameters.isEmpty())
            {
                SemanticStack.push(parameters.pop());
            }
            if (id.GetName() == "read")
            {
                fiftyOneRead(t);
            } else
            {
                fityOneWrite(t);
            }
        } else
        {
            int numParams = ParamCount.pop();
            try
            {
                if (numParams != id.GetNumberOfParameters())
                {
                    throw new SemanticError("Expected " + id.GetName() + " to have " + id.GetNumberOfParameters() + " parameters");
                }
            } catch (SemanticError e)
            {
                System.err.println(e);
                System.exit(1);
            }

            while (!parameters.isEmpty())
            {
                Gen("param " + GetSTEAddressParam(GetParamPrefix(parameters.peek()), parameters.pop()));
                LocalMemory++;
            }
            Insert = false;
            Gen("call", id.GetName(), numParams);
            Insert = true;
            ParamStack.pop();
            NextParam = 0;
        }
    }

    private void fiftyOneRead(Token t)
    {
        LinkedList<SymbolTableEntry> parameters = new LinkedList<>();
        SymbolTableEntry s = (SymbolTableEntry) SemanticStack.peek();
        while (s.GetVariable())
        {
            parameters.push(s);
            SemanticStack.pop();
            if (SemanticStack.peek().equals(EType.ARITHMETIC) || SemanticStack.peek().equals(EType.RELATIONAL))
            {
                break;
            }
            s = (SymbolTableEntry) SemanticStack.peek();
        }

        while (!parameters.isEmpty())
        {
            SymbolTableEntry id = parameters.pop();
            if (id.GetType().toLowerCase().equals("REAL"))
            {
                Gen("finp", id.GetName());
            } else
            {
                Gen("inp", id.GetName());
            }
        }

        EType etype = (EType) SemanticStack.pop();
        SymbolTableEntry id = (SymbolTableEntry) SemanticStack.pop();
        ParamCount.pop();
    }

    private void fityOneWrite(Token t)
    {
        LinkedList<SymbolTableEntry> parameters = new LinkedList<>();
        SymbolTableEntry s = (SymbolTableEntry) SemanticStack.peek();
        while (s.GetVariable() || s.getClass().getSimpleName() == "ConstantEntry")
        {
            parameters.push(s);
            SemanticStack.pop();
            if (SemanticStack.peek().equals(EType.ARITHMETIC) || SemanticStack.peek().equals(EType.RELATIONAL))
            {
                break;
            }
            s = (SymbolTableEntry) SemanticStack.peek();
        }

        while (!parameters.isEmpty())
        {
            SymbolTableEntry id = parameters.pop();
            if (id.getClass().getSimpleName() == "ConstantEntry")
            {
                if (id.GetType().toLowerCase().equals("REAL"))
                {
                    Gen("foutp " + id.GetName());
                } else
                {
                    Gen("outp " + id.GetName());
                }
            } else
            {
                Gen("print \"" + id.GetName() + " = \"");
                if (id.GetType().equals("REAL"))
                {
                    Gen("foutp ", id.GetName());
                } else
                {
                    Gen("outp ", id.GetName());
                }
            }
            Gen("newl");
        }
        EType etype = (EType) SemanticStack.pop();
        SymbolTableEntry id = (SymbolTableEntry) SemanticStack.pop();
        ParamCount.pop();
    }

    private void ActionFiftyTwo()
    {
        EType etype = (EType) SemanticStack.pop();
        SymbolTableEntry id = (SymbolTableEntry) SemanticStack.pop();
        try
        {
            if (!id.GetFunction())
            {
                throw new SemanticError(id.GetName() + " is not a function");
            }
            if (id.GetNumberOfParameters() > 0)
            {
                throw new SemanticError("Expected " + id.GetName() + " to have 0 parameters");
            }
        } catch (SemanticError e)
        {
            System.err.println(e);
            System.exit(1);
        }
        Gen("call", id.GetName(), 0);
        VariableEntry temp = Create(TempName(), id.GetType());
        Gen("move", id.GetResult().GetName(), temp.GetName());
        SemanticStack.push(temp);
        SemanticStack.push(null);
    }

    private void ActionFiftyThree()
    {
        EType etype = (EType) SemanticStack.pop();
        SymbolTableEntry id = (SymbolTableEntry) SemanticStack.pop();
        if (id.GetFunction())
        {
            try
            {
                if (id != CurrentFunction)
                {
                    throw new SemanticError("Illegal procedure " + id);
                }
            } catch (SemanticError e)
            {
                System.err.println(e);
            }
            SemanticStack.push(id.GetResult());
            SemanticStack.push(EType.ARITHMETIC);
        } else
        {
            SemanticStack.push(id);
            SemanticStack.push(etype);
        }
    }

    private void ActionFiftyFour()
    {
        EType etype = (EType) SemanticStack.pop();
        SymbolTableEntry id = (SymbolTableEntry) SemanticStack.peek();
        SemanticStack.push(etype);
        try
        {
            if (!id.IsProcedure())
            {
                throw new SemanticError("Expected" + id.GetName() + " to be a procedure");
            }
        } catch (SemanticError e)
        {
            System.err.println(e);
            System.exit(1);
        }
    }

    private void ActionFiftyFive()
    {
        Backpatch(MakeList(GlobalStore), String.valueOf(GlobalMemory));
        Gen("free", String.valueOf(GlobalMemory));
        Gen("PROCEND");
    }

    private void ActionFiftySix()
    {
        Gen("PROCBEGIN", "main");
        GlobalStore = Quads.GetNextQuad();
        Gen("alloc", "");
    }

    /* ************************
     * END OF SEMANTIC ACTIONS
     * ************************
     */

    //compares the types of two values, int/int, int/real, real/int, real/real
    private int TypeCheck(SymbolTableEntry first, SymbolTableEntry second)
    {
        String type1 = first.GetType().toLowerCase();
        String type2 = second.GetType().toLowerCase();

        switch (type1)
        {
            case "integer":
                if (type1.equals(type2))
                {
                    return 0;
                } else
                {
                    return 3;
                }
            case "real":
                if (type1.equals(type2))
                {
                    return 1;
                } else
                {
                    return 2;
                }
            default:
                return 999;
        }

    }

    //creates a new variable entry, usually used for temporary variables
    private VariableEntry Create(String name, String type)
    {
        VariableEntry v = new VariableEntry(name);
        v.SetType(type);
        if (Global)
        {
            v.SetAddress(GlobalMemory);
            GlobalMemory++;
            GlobalTable.Insert(v);
        } else
        {
            v.SetAddress(LocalMemory);
            LocalMemory++;
            LocalTable.Insert(v);
        }

        return v;
    }

    //generates uniques temporary variable names
    private String TempName()
    {
        TempVars++;
        return "temp" + TempVars;
    }

    //generates TVI code
    private void Gen(String... args)
    {
        if (args.length < 1 || args.length > 4)
        {
            System.err.print("Generate Called with too many arguments");
            return;
        }

        String[] code = new String[4];
        code[0] = args[0];

        for (int i = 1; i < args.length; i++)
        {
            try
            {
                Float.parseFloat(args[i]);
                SymbolTableEntry e = ConstantTable.Lookup(args[i]);
                if (e != null)
                {
                    VariableEntry temp = Create(TempName(), e.GetType());
                    String[] constant = new String[4];
                    constant[0] = "move";
                    constant[1] = e.GetName();
                    constant[2] = GetSTEPrefix(temp.GetName());
                    constant[3] = null;
                    Quads.AddQuad(constant);
                    code[i] = GetSTEPrefix(temp.GetName());
                } else
                {
                    code[i] = args[i];
                }
            } catch (NumberFormatException e)
            {
                if (args[i] == "")
                {
                    code[i] = args[i];
                } else
                {
                    code[i] = GetSTEPrefix(args[i]);
                }
            }
        }

        if (args.length < 4)
        {
            for (int i = args.length; i < 4; i++)
            {
                code[i] = null;
            }
        }
        Quads.AddQuad(code);
    }

    //overload of Gen for the case where the 3rd value is an int. Usually used for calling functions
    private void Gen(String action, String method, int args)
    {
        String[] code = new String[4];
        code[0] = action;
        code[1] = method;
        code[2] = String.valueOf(args);
        code[3] = null;

        Quads.AddQuad(code);
    }

    //helper function for gen, this one determines what the prefix of an address is
    private String GetSTEPrefix(String s)
    {
        if (Global)
        {
            return GetSTEAddress("", GlobalTable.Lookup(s));
        } else if (LocalTable.Lookup(s) != null)
        {
            if (LocalTable.Lookup(s).GetParameter())
            {
                return GetSTEAddress("^%", LocalTable.Lookup(s));
            } else
            {
                return GetSTEAddress("%", LocalTable.Lookup(s));
            }
        } else
        {
            return GetSTEAddress("", GlobalTable.Lookup(s));
        }
    }

    //helper function for gen, this finds the address of a value
    private String GetSTEAddress(String pre, SymbolTableEntry e)
    {
        if (e.getClass().getSimpleName() == "ConstantEntry")
        {
            // VariableEntry temp = create(TempName(), e.getType());
            // Gen("move", e.getName(), temp.getName());
            // return pre + temp.getAddress();
            return pre + e.GetAddress();
        } else if (e.GetArray() || e.GetVariable())
        {
            return pre + e.GetAddress();
        } else
        {
            return e.GetName();
        }
    }

    //This is similar to the above function, but exclusively used for parameters
    private String GetSTEAddressParam(String pre, SymbolTableEntry e)
    {
        if (e.getClass().getSimpleName() == "ConstantEntry")
        {
            VariableEntry temp = Create(TempName(), e.GetType());
            String[] constant = new String[4];
            constant[0] = "move";
            constant[1] = e.GetName();
            constant[2] = GetSTEPrefix(temp.GetName());
            constant[3] = null;
            Quads.AddQuad(constant);
            return pre + temp.GetAddress();
        } else if (e.GetArray() || e.GetVariable())
        {
            return pre + e.GetAddress();
        } else
        {
            return e.GetName();
        }
    }

    //This function returns the proper paramater prefix for the "param" TVI code
    String GetParamPrefix(SymbolTableEntry id)
    {
        if (Global)
        {
            return "@";
        } else
        {
            if (id.GetParameter())
            {
                return "%";
            } else
            {
                return "@%";
            }
        }
    }

    //Backpatches values into TVI entries
    private void Backpatch(List<Integer> l, String x)
    {
        for (Integer i : l)
        {
            String[] q = Quads.GetQuad(i);
            if (q[0] == "goto" || q[0] == "alloc")
            {
                Quads.SetField(i, 1, x);
            } else
            {
                Quads.SetField(i, 3, x);
            }
        }
    }

    //Makes an integer a list, used for backpatch
    private List<Integer> MakeList(int i)
    {
        List<Integer> l = new ArrayList<>();
        l.add(i);
        return l;
    }

    //Merges two lists into one, used for backpatch
    private List<Integer> Merge(List<Integer> list1, List<Integer> list2)
    {
        List<Integer> l = new ArrayList<>();
        l.addAll(list1);
        l.addAll(list2);

        return l;
    }

    //given a token, this returns the symbol table entry pertaining to the token
    private SymbolTableEntry LookupID(Token id)
    {
        SymbolTableEntry entry = GlobalTable.Lookup(id.GetValue());
        if (Global)
        {
            if (entry != null)
            {
                return entry;
            } else
            {
                return LookupConstant(id);
            }
        }
        entry = LocalTable.Lookup(id.GetValue());
        if (entry != null)
        {
            return entry;
        } else
        {
            entry = GlobalTable.Lookup(id.GetValue());
        }
        if (entry != null)
        {
            return entry;
        } else
        {
            return LookupConstant(id);
        }
    }

    //given a token, return the contant entry pertaining to that token
    private SymbolTableEntry LookupConstant(Token id)
    {
        return ConstantTable.Lookup(id.GetValue());
    }

    //prints value of semantic stack
    private void StackDump()
    {
        System.out.println(SemanticStack);
    }

    //toggle debugging information
    public void SetVerbose(Boolean b)
    {
        Verbose = b;
    }

    //prints the TVI code that has been generated
    public void IntermediateCodePrint()
    {
        Quads.Print();
    }

}

