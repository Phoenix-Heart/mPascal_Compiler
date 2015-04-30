package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

public class NotOp extends Operator {
    public NotOp(Type[] types) {
        super(types);
    }

    @Override
    protected String Op(Argument leftArg, Argument rightArg, String label) throws SemanticException {
        Argument.decreaseSP();
        return "NOTS";
    }
}
