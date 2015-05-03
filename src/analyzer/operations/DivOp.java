package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

public class DivOp extends StackOperator {
    public DivOp(Type[] types) {
        super(types);
    }

    @Override
    protected String safeOp(Argument leftArg, Argument rightArg, String label) throws SemanticException {
            assertValue(leftArg, true);
            assertValue(rightArg, true);
            assertValue(label, false);
            Argument.decreaseSP();
            return "DIVS";
    }
}
