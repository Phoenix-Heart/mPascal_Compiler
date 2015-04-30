package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

public class FloatDivOp extends Operator {
    public FloatDivOp(Type[] numTypes) {
        super(numTypes);
    }

    @Override
    protected String Op(Argument leftArg, Argument rightArg, String label) throws SemanticException {
        Argument.decreaseSP();
        return "DIVSF";
    }
}
