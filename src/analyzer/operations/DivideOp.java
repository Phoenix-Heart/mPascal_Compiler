package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

public class DivideOp extends Operator {
    public DivideOp(Type[] numTypes) {
        super(numTypes);
    }

    @Override
    protected void Op(Argument leftArg, Argument rightArg, String label) throws SemanticException {

    }
}
