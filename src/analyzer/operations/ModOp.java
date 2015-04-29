package analyzer.operations;

import analyzer.SemRecord;
import analyzer.SemanticException;
import symbolTable.Type;

public class ModOp extends Operator {
    public ModOp(Type[] types) {
        super(types);
    }

    @Override
    protected void Op(SemRecord leftArg, SemRecord rightArg, String label) throws SemanticException {

    }
}
