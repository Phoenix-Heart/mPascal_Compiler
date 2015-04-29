package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

public class GthanOp extends Operator {
    public GthanOp(Type[] types) {
        super(types);
    }

    @Override
    protected void Op(Argument leftArg, Argument rightArg, String label) throws SemanticException {

    }
}
