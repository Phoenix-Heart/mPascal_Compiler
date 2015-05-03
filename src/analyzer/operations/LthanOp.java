package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

public class LthanOp extends StackOperator {
    public LthanOp(Type[] types) {
        super(types);
    }

    @Override
    protected String safeOp(Argument leftArg, Argument rightArg, String label) throws SemanticException {
        return null;
    }
}