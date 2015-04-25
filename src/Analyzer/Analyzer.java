package Analyzer;

import core.Token;
import parser.ParseException;
import symbolTable.SymbolTable;
import symbolTable.TableEntry;
import symbolTable.TableStack;
import symbolTable.Type;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

/**
 * Created by Christina on 4/8/2015.
 */
public class Analyzer {
    private static TableStack tables = TableStack.getStack();
    private static HashMap<Token,HashMap<Type,MachineOp>> opTable;
    private String endline = "\n";
    private Writer writer;


    public Analyzer() {
        addOp(Token.MP_PLUS, Type.INTEGER, MachineOp.Adds);
        addOp(Token.MP_READ, Type.STRING, MachineOp.Read);
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("outfile.txt"), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void addOp(Token token, Type type, MachineOp op) {
        opTable.put(token, new HashMap<Type, MachineOp>(Type.values().length));
        opTable.get(token).put(type, op);

    }

    private void writePush(String s)
    {
        putLine("PUSH " + s);
    }

    // take a record (with at least one valid operation and valid operators). Generate and save the code.
    public static void generate(SemanticRecord record) {
        genRecursive(record);
    }
    // recursively descend and generate code from record.

    private static Type genRecursive(SemanticRecord record) {
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
        String toWrite = getSymbol(record.operand);
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

    public void compare(SemanticRecord s)
    {
        if (s.leftOperand.isOperand && s.rightOperand.isOperand)
        {

        }

    }




}
