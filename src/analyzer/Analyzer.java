package analyzer;

import analyzer.operations.*;
import core.Token;
import parser.ParseException;
import symbolTable.Kind;
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
    private static HashMap<Token,Operator> opTable = new HashMap<>();
    private static String endline = "\n";
    private String writeFile = "generated_code.il";
    private static BufferedWriter writer;
    private int labelCounter = 0;


    public Analyzer(){
        // initialize operator lookup table
        opTable.put(Token.MP_READ, new ReadOp());
        opTable.put(Token.MP_WRITE, new WriteOp());
        opTable.put(Token.MP_WRITELN, new WriteLnOp());
        opTable.put(Token.MP_ASSIGN, new AssignOp());

        try {
                writer = new BufferedWriter(new FileWriter(writeFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void closeFile()
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

    private static void writePush(String s)
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
           Operator op = opLookup(record.operator);
            Argument leftArg = new Argument(getSymbol(record.leftOperand.operand), getType(record.leftOperand.operand),getKind(record.leftOperand.operand));
            Argument rightArg = new Argument(getSymbol(record.rightOperand.operand), getType(record.rightOperand.operand),getKind(record.rightOperand.operand));
            try {
                op.performOp(leftArg,rightArg);
            } catch (SemanticException e) {
                e.printStackTrace();
            }
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
    private static String getSymbol(String operand) {
        TableEntry entry;
        try {
            entry = tables.getEntry(operand);
            return entry.offset+"(D"+entry.nest+")";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static Type getType(String operand) {
        try {
            TableEntry entry  = tables.getEntry(operand);
            return entry.type;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static  Kind getKind(String operand) {
        try {
            TableEntry entry = tables.getEntry(operand);
            return entry.kind;
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
    private Operator opLookup(Token token) {
        if(opTable.containsKey(token)) {
            return opTable.get(token);
        }
        else {
            return null;
        }
    }
    // print single line out to a file.
    static void putLine(String line) {
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
    static void put(String statement) {
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

    public static void varDeclaration(String s)
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
        Type lType = getType(l.operand);
        Type rType = getType(r.operand);
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
    // compare input types to expected types
    private Type typeCheck(Type[] input, Type[] expected) {
        int precedence = expected.length+1;
        boolean exists = false;
        for(Type type : input) {
            exists = false;
            for(int i=0;i<expected.length;i++) {
                Type e = expected[i];
                if(type==e) {
                    exists = true;
                    //
                    if(i<precedence)
                        precedence = i;
                }
                if(!exists) {
                    try {
                        throw new SemanticException(String.format("This is not the type you are looking for. Type %s, expected: ", type,expected));
                    } catch (SemanticException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        if(precedence < expected.length) {
            return expected[precedence];
        }
        else
            return null;                // this code should be unreachable
    }
}
