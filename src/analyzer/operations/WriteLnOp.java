package analyzer.operations;

import analyzer.Analyzer;
import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

/**
 * Created by Christina on 4/26/2015.
 */
public class WriteLnOp extends Operator {
    public WriteLnOp(Type[] types) {
        super(types);
    }
    @Override
    protected String Op(Argument leftArg, Argument rightArg, String label) throws SemanticException {
        assertValue(leftArg, true);
        assertValue(rightArg, false);
        assertValue(label, false);

        if(leftArg.isInStack()) {
            Argument.decreaseSP();
            return "WRTLNS ";
        }
        else {
            return "WRTLN " + leftArg.getSymbol();
        }
    }
}