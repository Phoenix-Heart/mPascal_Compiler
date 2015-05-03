package analyzer.operations;

import analyzer.Analyzer;
import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

/**
 * Created by Christina on 4/26/2015.
 */
public class WriteOp extends Operator {
    public WriteOp(Type[] types) {
        super(types);
    }

    @Override
    public String Op(Argument leftArg, Argument rightArg, String label) throws SemanticException {
        assertValue(leftArg, true);
        assertValue(rightArg, false);
        assertValue(label, false);

        if(leftArg.isInStack()) {
            return "WRTS ";
        }
        else {
            return "WRT " + leftArg.getSymbol();
        }
    }
}