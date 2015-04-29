package analyzer.operations;

import analyzer.Analyzer;
import analyzer.SemRecord;
import analyzer.SemanticException;
import symbolTable.Type;

/**
 * Created by night on 4/26/2015.
 */
public class ReadOp extends Operator {
    public ReadOp(Type[] types) {
        super(types);
    }
    @Override
    protected void Op(SemRecord leftArg, SemRecord rightArg, String label) throws SemanticException {
        // assert that only the appropriate parameters are defined. e.g. leftArg is used and the others are null
        assertValue(leftArg, true);
        assertValue(rightArg, false);
        assertValue(label, false);

        switch (leftArg.getType()) {
            case INTEGER:
                Analyzer.putLine("RD " + leftArg.getSymbol());
                break;
            case FLOAT:
                Analyzer.putLine("RDF " + leftArg.getSymbol());
                break;
            case STRING:
                Analyzer.putLine("RDS " + leftArg.getSymbol());
                break;
            default:
                throw new SemanticException("Unrecognized type: "+leftArg.getType());
        }
    }
}