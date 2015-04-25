package Analyzer;

import core.Token;
import symbolTable.TableEntry;

import java.util.ArrayList;

/**
 * Created by Christina on 4/14/2015.
 */
public class SemanticRecord {
    public final Token operator;
    public final SemanticRecord leftOperand;
    public final SemanticRecord rightOperand;
    public final String operand;
    public final boolean isOperand;

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

    public SemanticRecord(String operand, Token token)
    {
        this.operator = token;
        this.leftOperand = null;
        this.rightOperand = null;
        this.operand = operand;
        this.isOperand = false;
    }
}
