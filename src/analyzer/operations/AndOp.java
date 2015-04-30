package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

public class AndOp extends StackOperator {
    public AndOp(Type[] types) {
        super(types);
    }

    @Override
    protected String safeOp(Argument leftArg, Argument rightArg, String label) throws SemanticException {
        assertValue(leftArg, true);
        assertValue(rightArg, true);
        assertValue(label, false);

        Argument.decreaseSP();      // do this for any stack operations that pop off exactly 1 more argument than is added on.
        return "ANDS";
    }
}
