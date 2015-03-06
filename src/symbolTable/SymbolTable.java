package symbolTable;
import java.util.*;
/**
 * Created by hunter on 3/6/15.
 */
public class SymbolTable {
    private HashMap hm;
    private String name;
    private String label;
    private int nestingLevel;
    private static int labelCounter = 0;

    public SymbolTable(String name, int nestingLevel)
    {
        this.hm = new HashMap();
        this.name = name;
        this.label = "l" + Integer.toString(labelCounter);
        labelCounter++;
        this.nestingLevel = nestingLevel;
    }

    public HashMap getHashmap()
    {
        return this.hm;
    }

    public String getName()
    {
        return this.name;
    }

    public String getLabel()
    {
        return this.label;
    }

    public int getNestingLevel()
    {
        return this.nestingLevel;
    }

    public void addSymbol(String name, Object o)
    {
        this.hm.put(name, o);
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
}
