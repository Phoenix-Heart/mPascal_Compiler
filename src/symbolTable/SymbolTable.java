package symbolTable;

import analyzer.Analyzer;
import parser.EntryBuilder;

import java.util.*;
/**
 * Created by hunter on 3/6/15.
 */
public class SymbolTable {
    private HashMap<String, TableEntry> hm;
    private String label;
    private int nestingLevel;
    private int nextoffset;         // # offset to give to the next entry
    private String name;

    public SymbolTable(String name, int nestingLevel)
    {
        this.nextoffset = 0;
        this.hm = new HashMap();
        this.name = name.toUpperCase();

        this.nestingLevel = nestingLevel;
    }
    public TableEntry getEntry(String key) {
        return hm.get(key.toUpperCase());
    }

    public int getNestingLevel()
    {
        return this.nestingLevel;
    }

    public void printTable()
    {
        System.out.print("Table Name:  ");
        System.out.print(this.name);
        System.out.print("\nTable Label: ");
        System.out.print(this.label);
        System.out.print("\nNesting Level: ");
        System.out.print(this.nestingLevel);
        System.out.print("\n\nSymbols\n");
        for (Object key :this.hm.keySet())
        {
            TableEntry e = hm.get(key);
            System.out.println(e.lexeme);
            System.out.println(e.kind);
            System.out.println(e.offset);
            System.out.println(e.type);
        }
    }

    public boolean hasEntry(String lexeme) {
        return hm.containsKey(lexeme.toUpperCase());
    }

    public void createNewEntry(String lexeme, EntryBuilder tableEntry) {
        tableEntry.setNest(nestingLevel);
        TableEntry entry = new TableEntry(lexeme.toUpperCase(), nextoffset, tableEntry);
        addEntry(entry);
    }

    public void addEntry(TableEntry entry) {
        hm.put(entry.lexeme.toUpperCase(),entry);

        // procedures and programs do not store values.
        if(entry.kind!=Kind.PROCEDURE && entry.kind!=Kind.PROGRAM)
            Analyzer.varDeclaration(entry.lexeme);

        nextoffset++;
    }
    public void tempDeclaration() {
        Analyzer.tempDeclaration(nextoffset+"(D"+nestingLevel+")");
    }
}
