package parser;

import symbolTable.Kind;
import symbolTable.Mode;
import symbolTable.TableEntry;
import symbolTable.Type;

import java.util.List;

// stores temporary information about a table entry. Allows only one update per field.
public class EntryBuilder {
    private String name;
    private Kind kind;
    private Type type;
    private Mode mode;
    private List<TableEntry> params;
    EntryBuilder() {
        name = null;
        kind = null;
        type = null;
        mode = null;
        params = null;
    }
    public String getName() {
        return name;
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
    public void setName(String name) throws ParseException {
        if(this.name==null) {
            this.name = name;
        }
        else {
            error("name", this.name.toString(), name.toString());
        }
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
    private void error(String err, String value, String newvalue) throws ParseException {
        throw new ParseException(String.format("Attempted to overwrite %s entry %s with %s",err, value, newvalue));

    }
}