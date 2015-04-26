package symbolTable;

import core.Token;
import parser.EntryBuilder;
import parser.ParseException;
import scanner.Dispatcher;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Christina on 4/21/2015.
 */
public class TableStack {
    private LinkedList<SymbolTable> stack;
    private int nest;
    private static TableStack singleton;


    private TableStack() {
        singleton = this;
        this.stack = new LinkedList<>();
        nest = 0;
    }
    public static TableStack getStack() {
        if(singleton==null) {
            return new TableStack();
        }
        else return singleton;
    }
    // get entry to use during identifier conflict resolution
    public TableEntry getEntry(String lexeme) throws ParseException {
        Iterator<SymbolTable> iter = stack.descendingIterator();
        SymbolTable table;
        while(iter.hasNext()) {
            table = iter.next();
            if(table.hasEntry(lexeme))
                return table.getEntry(lexeme);
        }
        throw new ParseException("Symbol table entry not found. Lexeme: " + lexeme + ".  ");
    }
    // create a new symbol table and add it to the stack
    public void createTable(EntryBuilder builder) throws ParseException {
        if(builder.getLexeme()!=null) {
            stack.push(new SymbolTable(builder.getLexeme(), nest));
            nest++;
        }
        else {
            throw new ParseException("Attempted to insert symbol table without identifier.");
        }
        for (TableEntry entry : builder.getParams()) {
            SymbolTable table = stack.peek();
            table.addEntry(entry);
        }
    }
    // remove top symbol table from the stack
    public SymbolTable pop() {
        nest--;
        return stack.pop();
    }

    public SymbolTable peek()
    {
        return stack.peek();
    }

    // add a row to the symbol table using information retrieved during parse.
    public boolean addEntry(EntryBuilder builder) throws ParseException {
        // need to fill out logic, catch all errors & ensure program,procedure,function preserve entry for new table creation.

        SymbolTable table = stack.peek();
        if(builder.getLexeme()==null) {
            throw new ParseException(String.format("Missing lexeme in %s declaration.", builder.getKind()));
        }
        else if(builder.getKind()==null) {
            throw new ParseException(String.format("Missing Kind in declaration. Lexeme %s", builder.getLexeme()));
        }
        switch ((builder.getKind())) {
            case FUNCTION:
                // temp allowing function without a mode
                //if(tableEntry.getMode()==null)
                //throw new ParseException(String.format("Missing mode in function declaration. Lexeme %s", tableEntry.getLexeme()));
                //else
                table.createNewEntry(builder.getLexeme(), builder);
                // entry reset after new table creation
                return false;
            case PROCEDURE:
                table.createNewEntry(builder.getLexeme(),builder);
                // entry reset after new table creation
                return false;
            case PARAMETER:
                if(builder.getType()==null)
                    throw new ParseException(String.format("Missing type in parameter declaration. Lexeme %s", builder.getLexeme()));
                table.createNewEntry(builder.getLexeme(),builder);
                // reset entry
                return true;
            case VARIABLE:
                if(builder.getType()==null)
                    throw new ParseException(String.format("Missing type in variable declaration. Lexeme %s", builder.getLexeme()));
                else {
                    for(String lexeme : builder.getLexemes()) {
                        table.createNewEntry(lexeme, builder);
                    }
                }
                // reset entry
                return true;
            case PROGRAM:
                throw new ParseException("Attempted to enter PROGRAM identifier into table. Program identifiers used in table creation, not entries.");
        }
        return false;
    }
}
