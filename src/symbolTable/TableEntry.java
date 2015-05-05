package symbolTable;

import parser.EntryBuilder;

import java.util.List;

/**
 * Created by Christina on 4/8/2015.
 */
public class TableEntry {
    public final String lexeme;
    public final Type type;
    public final Kind kind;
    public final Mode mode;
    public final int offset;
    public final int nest;
    List<TableEntry> parameters;
    public boolean invariant;

    public TableEntry(String lexeme, int offset, EntryBuilder entry) {
        invariant = false;
        this.lexeme = lexeme;
        this.type = entry.getType();
        this.kind = entry.getKind();
        this.mode = entry.getMode();
        this.offset = offset;
        this.nest = entry.nest;
        this.parameters = entry.getParams();
    }
}
