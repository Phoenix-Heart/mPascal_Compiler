package analyzer;

import symbolTable.Kind;
import symbolTable.Type;

/**
 * Created by Christina on 4/24/2015.
 */
public class Argument {
    public final Type type;
    public final Kind kind;
    public final String symbol;
    public boolean inStack;
        // add terminal value with all information.
        public Argument(String symbol, Type type, Kind kind) {
            inStack = false;
            this.symbol = symbol;
            this.type = type;
            this.kind = kind;
        }
        // add intermediate value with just a type signature
        public Argument(Type type) {
            inStack = false;
            this.symbol = null;
            this.kind = null;
            this.type = type;
        }
        public void confirmStackPush() {
            inStack = true;
        }
}
