package analyzer;

import core.Token;
import symbolTable.Type;

/**
 * Created by Christina on 4/14/2015.
 */
public class SemanticRecord {
    public final Token operator;
    public final SemanticRecord leftOperand;
    public final SemanticRecord rightOperand;
    public String operand;
    public Type type;
    public boolean isOperand;
    public boolean isEvaluated;

    public SemanticRecord(SemanticRecord leftOperand, Token operator, SemanticRecord rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
        this.isOperand = false;
        operand = null;
    }
    // operand is a lexeme
    public SemanticRecord(String operand) {
        this.operand = operand;
        this.isOperand = true;
        leftOperand = null;
        rightOperand = null;
        operator = null;
    }
    public SemanticRecord() {
        isOperand = true;
        operand = null;
        leftOperand = null;
        rightOperand = null;
        operator = null;
    }
    //use for things that may be literals. in this case operand is the
    //value of the literal if token is a literal, otherwise it is the
    //token's lexeme
    public SemanticRecord(String operand, Token token)
    {
        this.operator = token;
        this.leftOperand = null;
        this.rightOperand = null;
        this.operand = operand;
        this.isOperand = false;
    }
    public Boolean isSimple()
    {
        if (this.operator == null)
            return false;
        else return this.operator.isAtomic();
    }
}
