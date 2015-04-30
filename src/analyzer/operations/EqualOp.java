package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

public class EqualOp extends StackOperator {
    public EqualOp(Type[] types) {
        super(types);
    }

    @Override
    protected String safeOp(Argument leftArg, Argument rightArg, String label) throws SemanticException {
        Argument.decreaseSP();
        if(leftArg.getType()==Type.FIXED || leftArg.getType()==Type.FLOAT) {
            return "CMPEQSF";
        }
        else {
            return "CMPEQS";
        }
    }
}
