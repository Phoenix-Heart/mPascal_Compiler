package analyzer;

import core.Token;
import sun.net.www.content.text.plain;
import symbolTable.Type;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Christina on 4/14/2015.
 */
public class SemanticRecord {

    private Token operator;
    private Type type;
    private SemanticRecord leftOperand;
    private SemanticRecord rightOperand;
    private String operand;
    private boolean isOperand;

    public SemanticRecord(SemanticRecord leftOperand, Token operator, SemanticRecord rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
        this.isOperand = false;
        operand = null;
        type = null;
    }
    // operand is a lexeme
    public SemanticRecord(String operand) {
        this.operand = operand;
        this.isOperand = true;
        leftOperand = null;
        rightOperand = null;
        operator = null;
        type = null;
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
        this.type = null;
    }

    public SemanticRecord()
    {
        this.operator = null;
        this.leftOperand = null;
        this.rightOperand = null;
        this.operand = null;
        this.isOperand = false;
        this.type = null;
    }

    public Type getType()
    {
        return this.type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public Token getOperator() {
        return operator;
    }

    public void setOperator(Token operator) {
        this.operator = operator;
    }

    public SemanticRecord getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(SemanticRecord leftOperand) {
        this.leftOperand = leftOperand;
    }

    public SemanticRecord getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(SemanticRecord rightOperand) {
        this.rightOperand = rightOperand;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public boolean isOperand() {
        return isOperand;
    }

    public void setOperand(boolean isOperand) {
        this.isOperand = isOperand;
    }

    public Boolean isSimple()
    {
        if (this.operator == null)
            return false;
        else return this.operator.isAtomic();
    }

    public void printRecord()
    {
        printRecordHelper(this);
    }

    public void printRecordHelper(SemanticRecord op)
    {
        System.out.println("RECORD");
        while(op.operator != null)
        {
            System.out.println(op.operator);
            System.out.println(op.operand);
            System.out.println("leftSide");
            printRecordHelper(op.getLeftOperand());
            printRecordHelper(op.getRightOperand());

        }
    }
}
