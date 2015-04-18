package parser;

import symbolTable.Kind;
import symbolTable.Mode;
import symbolTable.TableEntry;
import symbolTable.Type;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// stores temporary information about a table entry. Allows only one update per field.
public class EntryBuilder {
    private LinkedList<String> lexemes;
    private Kind kind;
    private Type type;
    private Mode mode;
    private LinkedList<TableEntry> params;
    EntryBuilder() {
        lexemes = new LinkedList();
        kind = null;
        type = null;
        mode = null;
        params = null;
    }
    public boolean hasLexeme() {
        return !lexemes.isEmpty();
    }
    public String getLexeme() {
        return lexemes.peek();
    }
    public LinkedList<String> getLexemes() {
        return lexemes;
    }
    public Kind getKind() {
        return kind;
    }
    public Type getType() {
        return type;
    }
    public Mode getMode() {
        return mode;
    }
    public List<TableEntry> getParams() {
        return params;
    }
    public void setLexeme(String lexeme) throws ParseException {
        lexemes.push(lexeme);
    }
    public void setKind(Kind kind) throws ParseException {
        if(this.kind==null)
            this.kind = kind;
        else
            error("kind", this.kind.toString(), kind.toString());
    }
    public void setType(Type type) throws ParseException {
        if(this.type==null)
            this.type = type;
        else
            error("type", this.type.toString(), type.toString());
    }
    public void setMode(Mode mode) throws ParseException {
        if(this.mode==null)
            this.mode = mode;
        else
            error("mode", this.mode.toString(), mode.toString());
    }
    public void addParam(TableEntry entry) {
        params.push(entry);
    }
    private void error(String err, String value, String newvalue) throws ParseException {
        throw new ParseException(String.format("Attempted to overwrite %s entry %s with %s",err, value, newvalue));

    }
}