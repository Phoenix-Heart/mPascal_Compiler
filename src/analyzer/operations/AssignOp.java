package analyzer.operations;

import analyzer.Analyzer;
import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

/**
 * Created by Christina on 4/26/2015.
 */
public class AssignOp extends Operator {
    public AssignOp(Type[] types) {
        super(types);
    }
    @Override
    protected String Op(Argument leftArg, Argument rightArg, String label) throws SemanticException {
        assertValue(leftArg,true);
        assertValue(rightArg,true);
        assertValue(label,false);
        if(leftArg.getType()!=rightArg.getType()) {

        }


        Argument.decreaseSP();
        return String.format("POP %s", leftArg);
    }
}