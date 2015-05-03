package analyzer;

import symbolTable.Type;

/**
 * Created by Christina on 4/24/2015.
 */
public class Argument {
    private Type type;
    private String symbol;
    private boolean inStack;
    private int stacklevel;
    protected static int stackpointer = 0;

    // add literal argument
    public Argument(String lit, Type type) {
        inStack = true;
        this.symbol = "#"+lit;
        this.type = type;
    }
    // add intermediate value with just a type signature
    public Argument(Type type) {
        inStack = true;
        this.type = type;
        symbol = null;
    }
    // add identifier argument
    public Argument(String id) {
        inStack = false;
        this.symbol = Analyzer.getSymbol(id);
        this.type = Analyzer.getType(id);
    }

    // use after performing appropriate call to push value onto stack.
        public void confirmStackPush() {
            inStack = true;
        }
        // push symbol onto stack if symbol exists
        public void genPush() throws SemanticException {
            if(symbol==null) {
                throw new SemanticException("There is no symbol to place on the stack.");
            }
            if(inStack) {
                throw new SemanticException(String.format("Symbol %s already on the stack.", symbol));
            }
            Analyzer.putLine("PUSH "+symbol);
            confirmStackPush();
            stacklevel = stackpointer;
            stackpointer++;
        }
    public int getStackDepth() {
        return stacklevel - stackpointer;
    }
    public void castType(Type type) {
        int depth = getStackDepth();
        if(depth!=-1) {
            Analyzer.writePush(depth+"(SP)");
            castOnStack(type);
        }
        depth--;
        Analyzer.writePop(depth+"(SP)");
    }
    private void castOnStack(Type type) {
        try {
            Analyzer.cast(this.type, type);
        } catch (SemanticException e) {
            e.printStackTrace();
        }
        this.type = type;
    }
    public static void increaseSP() {
        stackpointer++;
    }
    public static void decreaseSP() {
        stackpointer--;
    }

        // getters
        public Type getType() {
            return type;
        }
        public String getSymbol() {
            return symbol;
        }
        public boolean isInStack() {
            return inStack;
        }
        // return true if types are the same, return false otherwise.
        public boolean typeEquals(Argument record) {
            return type==record.getType();
        }
        public String toString() {
            return symbol;
        }
}
