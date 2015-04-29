package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

public class LthanOp extends Operator {
    public LthanOp(Type[] types) {
        super(types);
    }

    @Override
    protected void Op(Argument leftArg, Argument rightArg, String label) throws SemanticException {

    }
}
