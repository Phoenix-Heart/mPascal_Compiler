package symbolTable;
//import javafx.scene.control.Tab;

import java.util.*;
/**
 * Created by hunter on 3/6/15.
 */
public class SymbolTable {
    private HashMap<String, TableEntry> hm;
    private String label;
    private int nestingLevel;
    private static int labelCounter = 0;
    private int nextoffset;         // # offset to give to the next entry
    private String name;

    public SymbolTable(String name, int nestingLevel)
    {
        this.nextoffset = 0;
        this.hm = new HashMap();
        this.name = name;
        this.label = "L" + Integer.toString(labelCounter);
        labelCounter++;
        this.nestingLevel = nestingLevel;
    }

    public void createNewEntry(String lex, Type t, Kind k, Mode mode, List<TableEntry> params) {
        TableEntry entry = new TableEntry(lex, t, k, mode, nextoffset, params);
        hm.put(lex, entry);
        nextoffset++;
    }
    public TableEntry getEntry(String key) {
        return hm.get(key);
    }

    public String getLabel()
    {
        return this.label;
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
        System.out.print("\n\nSymbols");
        for (Object key:this.hm.keySet())
        {
            System.out.println(hm.get(key).toString());
        }
    }

    public boolean hasEntry(String lexeme) {
        return false;
    }
}
