package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

/**
 * Created by Christina on 5/1/2015.
 */
public class negSign extends StackOperator {
    public negSign(Type[] types) {
        super(types);
    }

    @Override
    protected String safeOp(Argument leftArg, Argument rightArg, String label) throws SemanticException {
        assertValue(leftArg, true);
        assertValue(rightArg, false);
        assertValue(label,false);
        return "NEGS";
    }
}
