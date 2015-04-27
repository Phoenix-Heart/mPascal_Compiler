package analyzer;

import core.Token;
import parser.ParseException;
import symbolTable.TableEntry;
import symbolTable.TableStack;
import symbolTable.Type;

import java.io.*;
import java.util.HashMap;


/**
 * Created by Christina on 4/8/2015.
 */
public class Analyzer {
    private static TableStack tables = TableStack.getStack();
    private static HashMap<Token,HashMap<Type,MachineOp>> opTable = new HashMap<>();
    private String endline = "\n";
    private String writeFile = "generated_code.il";
    private BufferedWriter writer;
    private int labelCounter = 0;


    public Analyzer(){
        addOp(Token.MP_PLUS, Type.INTEGER, MachineOp.Adds);
        addOp(Token.MP_READ, Type.STRING, MachineOp.Read);
        try {
                writer = new BufferedWriter(new FileWriter(writeFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addOp(Token token, Type type, MachineOp op) {
        opTable.put(token, new HashMap<Type, MachineOp>(Type.values().length));
        opTable.get(token).put(type, op);

    }

    public void closeFile()
    {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getLabel()
    {
        String currentLabel = "l" + Integer.toString(labelCounter);
        labelCounter++;
        return currentLabel;
    }

    private void writePush(String s)
    {
        putLine("PUSH " + s);
    }

    // take a record (with at least one valid operation and valid operators). Generate and save the code.
    public void generate(SemanticRecord record) {
        genRecursive(record);
    }
    // recursively descend and generate code from record.

    private Type genRecursive(SemanticRecord record) {
        Type leftType;
        Type rightType;
        //record.operator;
        if((record.rightOperand.isOperand)&&(record.leftOperand.isOperand)) {

        }
        else {
            if(!record.rightOperand.isOperand) {
                rightType = genRecursive(record.rightOperand);
            }
            else {
                rightType = getType(record.rightOperand.operand);
                // push getSymbol(record.rightOperand.operand) onto stack
            }
            if(!record.leftOperand.isOperand) {
                leftType = genRecursive(record.leftOperand);
            }
            else {
                leftType = getType(record.leftOperand.operand);
                // push getSymbol(record.leftOperand.operand) onto stack
            }
            // continue calculation here
            // ie. do type check and then
            // perform intermediate calculation from stack
        }
        return null;
    }
    private String getSymbol(String operand) {
        TableEntry entry;
        try {
            entry = tables.getEntry(operand);
            return entry.offset+"(D"+entry.nest+")";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Type getType(String operand) {
        try {
            TableEntry entry  = tables.getEntry(operand);
            return entry.type;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Type unsafeGetType(SemanticRecord s)
    {
        if(s.isSimple())
        {
            Type t;
            if (s.operator.isAtomic())
                t = s.operator.literalType();
            else t = getType(s.operand);
            return t;
        }
        return null;
    }

    private boolean typeCompatible(SemanticRecord s1, SemanticRecord s2)
    {
        if (s1.isSimple() && s2.isSimple())
        {
            Type t1 = unsafeGetType(s1);
            Type t2 = unsafeGetType(s2);
            return t1.equals(t2);
        }
        return false;
    }

    // search the operation table for the correct machine operator for the given type
    private MachineOp opLookup(Token token, Type type) {
        if(opTable.containsKey(token) && opTable.get(token).containsKey(type)) {
            return opTable.get(token).get(type);
        }
        else {
            return null;
        }
    }
    // print single line out to a file.
    private void putLine(String line) {
        try
        {
        writer.write(line + endline);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }
    // print input out to file exactly, does not start a new line.
    private void put(String statement) {
        try{
            writer.write(statement);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void preSetup()
    {
        putLine("PUSH D0");
        putLine("MOV SP D0");
    }

    public void varDeclaration(String s)
    {
        writePush(getSymbol(s));
        putLine("ADD SP #1 SP");
    }

    public void genAssign(TableEntry entry)
    {
        String location = getSymbol(entry.lexeme);
        putLine("POP " + location);
    }

    public void genNot()
    {
        putLine("NOTS");
    }

    private void MULS(Argument leftArg, Argument rightArg) {
    // type checking needed
        // push values on stack if not already in stack
        if(!leftArg.inStack)
            writePush(leftArg.symbol);
        if(!rightArg.inStack)
            writePush(rightArg.symbol);
        putLine("MULS");
    }

    public void readParameter(String s){
        {
            String toRead = getSymbol(s);
            putLine("RD" + toRead);
        }
    }

    public void writeParameter(SemanticRecord record) throws SemanticException {
        String toWrite = getRepresentation(record);
        writePush(toWrite);
        switch(record.operator)
        {
            case MP_WRITE:
                putLine("WRTS");
                break;
            case MP_WRITELN:
                putLine("WRTLNS");
                break;
            default:
                throw new SemanticException("bad token in Write parameter:" + record.operator);
        }
    }

    public void optionalSign(Token sign) throws SemanticException
    {
        switch(sign)
        {
            case MP_PLUS:
                break;
            case MP_MINUS:
                putLine("NEGS");
                break;
            default:
                throw new SemanticException("optional sign parsed but not found");
        }
    }

    public void compare(SemanticRecord s) throws SemanticException{
        SemanticRecord l = s.leftOperand;
        SemanticRecord r = s.rightOperand;
        Type lType = l.getType();
        Type rType = r.getType();
        if(l == null || r== null)
            throw new SemanticException("tried to compare without knowing types");
        else if (lType.isFloatish() && !(rType.isFloatish()))
        {
            putLine("CASTSF");
        }
        else
        {
            switch(s.operator)
            {
                case MP_EQUAL:
                    put("CMPEQS");
                    break;
                case MP_GEQUAL:
                    put("CMPGES");
                    break;
                case MP_GTHAN:
                    put("CMPGTS");
                    break;
                case MP_LEQUAL:
                    put("CMPLES");
                    break;
                case MP_LTHAN:
                    put("CMPLTS");
                    break;
                default:
                    throw new SemanticException("tried to do a bad compare with token " + s.operator);
            }
            if (lType.isFloatish() || rType.isFloatish())
            {
                put("F\n");
            }

        }
        }


    public String getRepresentation(SemanticRecord s) throws SemanticException
    {
        if (s.isSimple())
        {
            if (s.operator.isLiteral())
            {
                return("#" + s.operand);
            }
            else return getSymbol(s.operand);
        }
        else
        {
           throw new SemanticException("attempted to use a non-atomic token in an atomic context");
        }
    }





}
