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
    public void setName(String name) {
        if(name==null)
            this.name = name;
        else
            error("name");
    }
    public void setKind(Kind kind) {
        if(kind==null)
            this.kind = kind;
        else
            error("kind");
    }
    public void setType(Type type) {
        if(type==null)
            this.type = type;
        else
            error("type");
    }
    public void setMode(Mode mode) {
        if(mode==null)
            this.mode = mode;
        else
            error("mode");
    }
    private void error(String err) {
        System.out.println("Attempted to overwrite existing entry value: "+err);
    }
}