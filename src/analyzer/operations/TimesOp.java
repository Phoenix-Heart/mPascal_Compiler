package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

public class TimesOp extends StackOperator {
    public TimesOp(Type[] types) {
        super(types);
    }

    @Override
    protected String safeOp(Argument leftArg, Argument rightArg, String label) throws SemanticException {
        return null;
    }
}
