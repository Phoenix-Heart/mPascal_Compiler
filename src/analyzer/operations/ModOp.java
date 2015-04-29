package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

public class ModOp extends Operator {
    public ModOp(Type[] types) {
        super(types);
    }

    @Override
    protected void Op(Argument leftArg, Argument rightArg, String label) throws SemanticException {

    }
}
