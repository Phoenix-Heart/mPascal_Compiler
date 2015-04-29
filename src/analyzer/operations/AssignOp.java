package analyzer.operations;

import analyzer.Analyzer;
import analyzer.SemRecord;
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
    protected void Op(SemRecord leftArg, SemRecord rightArg, String label) throws SemanticException {
        assertValue(leftArg,true);
        assertValue(rightArg,false);
        assertValue(label,false);

        Analyzer.putLine(String.format("POP %s", leftArg.getSymbol()));
    }
}