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
    List<TableEntry> parameters;

    public TableEntry(String lex, Type t, Kind k, Mode mode, int offset, List<TableEntry> params) {
        this.lexeme = lex.toLowerCase();
        this.type = t;
        this.kind = k;
        this.mode = mode;
        this.offset = offset;
        this.parameters = params;
    }
    public TableEntry(String name, int offset, EntryBuilder entry) {
        this.lexeme = entry.getLexeme().toLowerCase();
        this.type = entry.getType();
        this.kind = entry.getKind();
        this.mode = entry.getMode();
        this.offset = offset;
        this.parameters = entry.getParams();
    }
}
