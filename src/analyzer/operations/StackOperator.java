package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

/**
 * Created by night on 4/29/2015.
 */
public abstract class StackOperator extends Operator {
    public StackOperator(Type[] types) {
        super(types);
    }

    @Override
    protected String Op(Argument leftArg, Argument rightArg, String label) throws SemanticException {
        // add
        if(leftArg!=null) {
            if (!leftArg.isInStack())
                leftArg.genPush();

        }
        if(rightArg!=null) {
            if (!rightArg.isInStack())
                rightArg.genPush();
        }


        return safeOp(leftArg, rightArg,label);
    }
    protected abstract String safeOp(Argument leftArg, Argument rightArg, String label) throws SemanticException;
}
