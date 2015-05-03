package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

public class NotOp extends StackOperator {
    public NotOp(Type[] types) {
        super(types);
    }

    @Override
    protected String safeOp(Argument leftArg, Argument rightArg, String label) throws SemanticException {
        assertValue(leftArg, true);
        assertValue(rightArg, false);
        assertValue(label, false);
        Argument.decreaseSP();
        return "NOTS";
    }
}
