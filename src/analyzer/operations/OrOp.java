package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

public class OrOp extends Operator {
    public OrOp(Type[] types) {
        super(types);
    }

    @Override
    protected String Op(Argument leftArg, Argument rightArg, String label) throws SemanticException {
        Argument.decreaseSP();
        return "ORS";
    }
}
