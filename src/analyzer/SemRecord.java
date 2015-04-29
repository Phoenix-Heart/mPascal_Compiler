package analyzer;

import symbolTable.Kind;
import symbolTable.Type;

/**
 * Created by Christina on 4/24/2015.
 */
public class SemRecord {
    private Type type;
    private String symbol;
    private boolean inStack;
        // add intermediate value with just a type signature
        public SemRecord(Type type) {
            inStack = true;
            this.symbol = null;
            this.type = type;
        }

    public SemRecord(String id) {
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
        public boolean typeEquals(SemRecord record) {
            return type==record.getType();
        }
        public String toString() {
            return symbol+" : "+type.name();
        }
}
