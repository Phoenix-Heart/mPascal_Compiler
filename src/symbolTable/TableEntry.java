package symbolTable;

import java.util.List;

/**
 * Created by Christina on 4/8/2015.
 */
public class TableEntry {
    public final String lexeme;
    public final Type type;
    public final Kind kind;
    public final int mode;
    public final int offset;
    List<List<Object>> parameters;

    public TableEntry(String lex, Type t, Kind k, int mode, int offset, List<List<Object>> params) {
        this.lexeme = lex;
        this.type = t;
        this.kind = k;
        this.mode = mode;
        this.offset = offset;
        this.parameters = params;
    }
}
