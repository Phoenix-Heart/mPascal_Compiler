package analyzer;

import analyzer.operations.*;
import core.Token;
import parser.ParseException;
import parser.Parser;
import symbolTable.TableEntry;
import symbolTable.TableStack;
import symbolTable.Type;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Christina on 4/8/2015.
 * Modified by Hunter
 */
public class Analyzer {
    private static TableStack tables = TableStack.getStack();
    private static HashMap<Token,Operator> opTable = new HashMap<>();
    private static negSign negsign;
    private static String endline = "\n";
    private String writeFile;
    private static BufferedWriter writer;
    private static int labelCounter = 0;

    public Analyzer(String filename){
        writeFile = filename + ".psc";

        // helper lists
        Type[] allTypes = {Type.BOOLEAN,Type.STRING,Type.FLOAT,Type.INTEGER, Type.FIXED};
        Type[] numTypes = {Type.INTEGER,Type.FLOAT,Type.FIXED};
        Type[] boolType = {Type.BOOLEAN};
        Type[] floatType = {Type.FLOAT, Type.FIXED};
        Type[] intType = {Type.INTEGER};

        negsign = new negSign(numTypes);

        // initialize operator lookup table
        opTable.put(Token.MP_READ, new ReadOp(new Type[]{Type.INTEGER, Type.FLOAT, Type.STRING, Type.FIXED}));
        opTable.put(Token.MP_WRITE, new WriteOp(allTypes));
        opTable.put(Token.MP_WRITELN, new WriteLnOp(allTypes));
        opTable.put(Token.MP_ASSIGN, new AssignOp(allTypes));
        opTable.put(Token.MP_AND, new AndOp(boolType));
        opTable.put(Token.MP_OR, new OrOp(boolType));
        opTable.put(Token.MP_FLOAT_DIVIDE, new FloatDivOp(floatType));
        opTable.put(Token.MP_GEQUAL,new GequalOp(numTypes));
        opTable.put(Token.MP_GTHAN, new GthanOp(numTypes));
        opTable.put(Token.MP_EQUAL, new EqualOp(numTypes));
        opTable.put(Token.MP_LEQUAL, new LequalOp(numTypes));
        opTable.put(Token.MP_LTHAN, new LthanOp(numTypes));
        opTable.put(Token.MP_MINUS, new MinusOp(numTypes));
        opTable.put(Token.MP_MOD, new ModOp(numTypes));
        opTable.put(Token.MP_NEQUAL, new NequalOp(numTypes));
        opTable.put(Token.MP_PLUS, new PlusOp(numTypes));
        opTable.put(Token.MP_DIV, new DivOp(intType));
        opTable.put(Token.MP_NOT, new NotOp(boolType));
        opTable.put(Token.MP_TIMES, new TimesOp(numTypes));

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
        putLine("HLT");
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Operator getNeg() {
        return negsign;
    }

    private static String getLabel()
    {
        String currentLabel = "L" + Integer.toString(labelCounter);
        labelCounter++;
        return currentLabel;
    }

    public static void writePush(String s)
    {
        putLine("PUSH " + s);
    }

    public static String genIfStart()
    {
        String l1 = getLabel();
        putLine("BRFS " + l1);
        Argument.decreaseSP();
        return l1;
    }

    public static void genIfEnd(String l1)
    {
        putLine(l1 + ":");
    }

    public static String genRepeatUntilStart()
    {
        String l1 = getLabel();
        putLine(l1 + ":");
        return l1;
    }

    public static void genRepeatUntilEnd(String l1)
    {
        putLine("BRTS " + l1);
    }

    public static ArrayList<String> genWhileStart()
    {
        ArrayList<String> labelList = new ArrayList<String>();
        labelList.add(getLabel());
        labelList.add(getLabel());
        putLine(labelList.get(0) + ":");
        return labelList;
    }

    public static void genWhileMiddle(ArrayList<String> labels)
    {
        putLine("BRFS " + labels.get(1));
    }

    public static void genWhileEnd(ArrayList<String> labels)
    {
        putLine("BR " + labels.get(0));
        putLine(labels.get(1) + ": ");
    }

    public static String genForStart(String cVar, Argument init)
    {
        String symbol = getSymbol(cVar);
        putLine("POP " + symbol);
        String l1 = getLabel();
        putLine(l1 + ": ");
        putLine("PUSH " + symbol);
        return l1;
    }

    public static String genForMiddle(String cVar, Token step, Argument end, String l1)
    {
        //end val should be on stack already
        if(step == Token.MP_DOWNTO)
        {
            putLine("CMPLES");
            Argument.decreaseSP();
        }
        else if (step == Token.MP_TO)
        {
            putLine("CMPGES");
            Argument.decreaseSP();
        }
        else
        {
            System.out.println("Token " + step.toString() + "encountered. Expected TO or DOWNTO");
        }
        String l2 = getLabel();
        putLine("BRTS " + l2);
        return l2;
    }

    public static void genForEnd(String cVar, Token step, String l1, String l2)
    {
        String symbol = getSymbol(cVar);
        switch(step)
        {
            case MP_DOWNTO:
                putLine("SUB " + symbol + " #1 " + symbol);
                break;
            case MP_TO:
                putLine("ADD " + symbol + " #1 " + symbol);
                break;
        }
        putLine("BR " + l1);
        putLine(l2 + ": ");
    }

    public static void genAssign(String id)
    {
        putLine("POP " + getSymbol(id));
    }



    /* take a record (with at least one valid operation and valid operators). Generate and save the code.
    public void generate(SemanticRecord record) {
        genRecursive(record);
    }
    // recursively descend and generate code from record.

    private Type genRecursive(SemanticRecord record) {
        Type leftType;
        Type rightType;
        //record.operator;
        if((record.getRightOperand().isOperand())&&(record.getLeftOperand().isOperand())) {
            Operator op = opLookup(record.getOperator());
            SemRecord leftArg = new SemRecord(getSymbol(record.getLeftOperand().getOperand()), getType(record.getLeftOperand().getOperand()));
            SemRecord rightArg = new SemRecord(getSymbol(record.getRightOperand().getOperand()), getType(record.getRightOperand().getOperand()));
            op.performOp(leftArg, rightArg, null);
        }
        else {
            if(!record.getRightOperand().isOperand()) {
                rightType = genRecursive(record.getRightOperand());
            }
            else {
                rightType = getType(record.getRightOperand().getOperand());
                // push getSymbol(record.rightOperand.operand) onto stack
            }
            if(!record.getLeftOperand().isOperand()) {
                leftType = genRecursive(record.getLeftOperand());
            }
            else {
                leftType = getType(record.getLeftOperand().getOperand());
                // push getSymbol(record.leftOperand.operand) onto stack
            }
            // continue calculation here
            // ie. do type check and then
            // perform intermediate calculation from stack
        }
        return null;
    }
    */

    // get identifier symbol from table
    public static String getSymbol(String lexeme) {
        TableEntry entry;
        try {
            entry = tables.getEntry(lexeme);
            int nest = tables.getNest(lexeme);
            return entry.offset+"(D"+nest+")";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Type getType(String operand) {
        try {
            TableEntry entry  = tables.getEntry(operand);
            return entry.type;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    // search the operation table for the correct machine operator for the given type
    public static Operator opLookup(Token token) {
        if(opTable.containsKey(token)) {
            return opTable.get(token);
        }
        else {
            return null;
        }
    }
    // print single line out to a file.
    public static void putLine(String line) {
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
    public static void put(String statement) {
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
    public static void tempDeclaration(String symbol) {
        writePush(symbol);
        putLine("ADD SP #1 SP");
    }

    public static void cast(Type from, Type to) throws SemanticException {
        if((to==Type.FLOAT || to==Type.FIXED) && from==Type.INTEGER)
        putLine("CASTSF");
        else if(to==Type.INTEGER && (from==Type.FLOAT || from==Type.FIXED)) {
            putLine("CASTSI");
        }
        else throw new SemanticException(String.format("Error casting, invalid types, from %s, to %s", from, to));
    }

    public static void writePop(String s) {
        putLine("POP "+s);
    }


    /*
    // these methods should be added to operations package

    public void genAssign(String lex)
    {
        String location = getSymbol(lex);
        putLine("POP " + location);
    }

    public void genNot()
    {
        putLine("NOTS");
    }

    private void MULS(SemRecord leftArg, SemRecord rightArg) {
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
        switch(record.getOperator())
        {
            case MP_WRITE:
                putLine("WRTS");
                break;
            case MP_WRITELN:
                putLine("WRTLNS");
                break;
            default:
                throw new SemanticException("bad token in Write parameter:" + record.getOperator());
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
        SemanticRecord l = s.getLeftOperand();
        SemanticRecord r = s.getRightOperand();
        Type lType = getType(l.operand);
        Type rType = getType(r.operand);
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
            switch(s.getOperator())
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
                    throw new SemanticException("tried to do a bad compare with token " + s.getOperator());
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
            if (s.getOperator().isLiteral())
            {
                return("#" + s.getOperand());
            }
            else return getSymbol(s.getOperand());
        }
        else
        {
           throw new SemanticException("attempted to use a non-atomic token in an atomic context");
        }
    }
    */
}
