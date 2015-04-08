package symbolTable;

import java.util.List;

/**
 * Created by Christina on 4/8/2015.
 */
public class TableEntry {
    private String lexeme;
    private Type type;
    private Kind kind;
    private int mode;
    private int size;
    List<List<Object>> parameters;

    public TableEntry(String lex, Type t, Kind k, int mode, int size, List<List<Object>> params) {
        lexeme = lex;
        type = t;
        kind = k;
        this.mode = mode;
        this.size = size;
        parameters = params;
    }
}
enum Type {
    INTEGER, FLOAT, STRING, BOOLEAN;
}
enum Kind {
    VARIABLE, PROCEDURE, PARAMETER, FUNCTION;
}


