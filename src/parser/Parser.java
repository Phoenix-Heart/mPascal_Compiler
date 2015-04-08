package parser;

import core.Token;
import scanner.Dispatcher;

import java.util.LinkedList;

/**
 * Created by Christina on 3/6/2015.
 */
public class Parser {
    Dispatcher dispatcher;
    public Parser(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
    private LinkedList stack;
    private Token lookahead;

    public void Parser() {
        lookahead = dispatcher.getToken();
    }
    private void matchLookAhead(Token token) throws ParseException {

        if(lookahead!=token) {
            throw new ParseException(String.format("Parse error on line %s, col %s. Found %s, expected %s.", dispatcher.getLine(), dispatcher.getColumn(), dispatcher.getLexeme(), token.name()));
        }
        lookahead = dispatcher.getToken();
    }
    private void LL1error() throws ParseException {
        throw new ParseException(String.format("Parse error on line %s, col %s. Unexpected token %s", dispatcher.getLine(), dispatcher.getColumn(), dispatcher.getLexeme(), lookahead.name()));
    }
    private void VariableDeclaration() {}
    private void VariableDeclarationPart() throws ParseException {
        VariableDeclarationTail();
        matchLookAhead(Token.MP_SCOLON);
        VariableDeclaration();
        matchLookAhead(Token.MP_VAR);
    }

    private void VariableDeclarationTail() throws ParseException {
    }


    private void Type() {    }
    private void ProcedureDeclaration() {    }
    private void FunctionDeclaration() {    }
    private void ProcedureHeading() {    }

    private void FunctionHeading() {         }
    private void OptionalFormalParameterList() {    }
    private void FormalParameterSectionTail() {    }
    private void FormalParameterSection() {    }
    private void ValueParameterSection() {}
    private void VariableParameterSection() {    }
    private void StatementPart() {    }
    private void CompoundStatement() {    }
    private void StatementSequence() {    }
    private void StatementTail() {    }
    private void Statement() throws ParseException {
        // use LL1 table to fill this in
        switch (lookahead) {
            case MP_SCOLON:
                break;
            case MP_BEGIN:
                break;
            case MP_ELSE:
                break;
            case MP_END:
                break;
            case MP_FOR:
                break;
            case MP_IF:
                break;
            case MP_READ:
                break;
            case MP_REPEAT:
                break;
            case MP_UNTIL:
                break;
            case MP_WHILE:
                break;
            case MP_WRITE:
                break;
            case MP_IDENTIFIER:
                break;
            default:
                LL1error();
        }

    }
    private void EmptyStatement() throws ParseException {
        // use LL1 table to fill this in
        switch (lookahead) {
            case MP_SCOLON:
                // end of branch
                break;
            case MP_ELSE:
                break;
            case MP_END:
                break;
            case MP_UNTIL:
                break;
            default:
                LL1error();
        }
    }
    private void ReadStatement() throws ParseException {
        matchLookAhead(Token.MP_READ);
        matchLookAhead(Token.MP_LPAREN);
        ReadParameter();
        ReadParameterTail();
        matchLookAhead(Token.MP_RPAREN);
    }
    private void ReadParameter() {
        VariableIdentifier();
    }
    private void ReadParameterTail() throws ParseException {
        // use LL1 table to fill this in
        switch (lookahead) {
            case MP_COMMA:
                matchLookAhead(Token.MP_COMMA);
                ReadParameter();
                ReadParameterTail();
                break;
            case MP_RPAREN:
                break;
            default:
                LL1error();
        }}
    private void WriteStatement() throws ParseException {
        switch (lookahead) {
            case MP_WRITE:
                matchLookAhead(Token.MP_WRITE);
                matchLookAhead(Token.MP_LPAREN);
                WriteParameter();
                WriteParameterTail();
                matchLookAhead(Token.MP_RPAREN);
                break;
            case MP_WRITELN:
                matchLookAhead(Token.MP_WRITELN);
                matchLookAhead(Token.MP_LPAREN);
                WriteParameter();
                WriteParameterTail();
                matchLookAhead(Token.MP_RPAREN);
                break;
            default:
                LL1error();
        }

    }
    private void WriteParameterTail() throws ParseException {
    switch (lookahead) {
        case MP_COMMA:
            matchLookAhead(Token.MP_COMMA);
            WriteParameter();
            WriteParameterTail();
        case MP_RPAREN:
            break;
        default:
            LL1error();
    }
    }
    private void WriteParameter() throws ParseException {
        switch (lookahead) {
            case MP_LPAREN:
                break;
            case MP_PLUS:
                break;
            case MP_MINUS:
                break;
            case MP_IDENTIFIER:
                break;
            case MP_INTEGER_LIT:
                break;
            case MP_NOT:
                break;
            default:
                LL1error();
        }
    }
    private void IfStatement() throws ParseException {
        matchLookAhead(Token.MP_IF);
        BooleanExpression();
        matchLookAhead(Token.MP_THEN);
        Statement();
        OptionalElsePart();
    }
    private void OptionalElsePart() throws ParseException {
        switch (lookahead) {
            case MP_ELSE:
                matchLookAhead(Token.MP_ELSE);
                Statement();
                break;
            case MP_SCOLON:
            case MP_END:
            case MP_UNTIL:
                break;
            default:
                LL1error();
        }
    }
    private void RepeatStatement() throws ParseException {
        matchLookAhead(Token.MP_REPEAT);
        StatementSequence();
        matchLookAhead(Token.MP_UNTIL);
        BooleanExpression();
    }
    private void WhileStatement() throws ParseException {
        matchLookAhead(Token.MP_WHILE);
        BooleanExpression();
        matchLookAhead(Token.MP_DO);
        Statement();
    }
    private void ForStatement() throws ParseException {
        matchLookAhead(Token.MP_FOR);
        ControlVariable();
        matchLookAhead(Token.MP_ASSIGN);
        InitialValue();
        StepValue();
        FinalValue();
        matchLookAhead(Token.MP_DO);
        Statement();
    }
    private void ControlVariable() {
        VariableIdentifier();
    }
    private void InitialValue() {
        OrdinalExpression();
    }
    private void StepValue() throws ParseException {
        switch (lookahead) {
            case MP_TO:
                matchLookAhead(Token.MP_TO);
                break;
            case MP_DOWNTO:
                matchLookAhead(Token.MP_DOWNTO);
                break;
            default:
                LL1error();
        }
    }
    private void FinalValue() {
        OrdinalExpression();
    }
    private void ProcedureStatement() throws ParseException {
        ProcedureIdentifier();
        OptionalActualParameterList();
    }
    private void OptionalActualParameterList() throws ParseException {
        matchLookAhead(Token.MP_LPAREN);
        ActualParameter();
        ActualParameterTail();
        matchLookAhead(Token.MP_RPAREN);
    }
    private void ActualParameterTail() throws ParseException {
        switch (lookahead) {
            case MP_COMMA:
                matchLookAhead(Token.MP_COMMA);
                ActualParameter();
                ActualParameterTail();
                break;
            case MP_RPAREN:
                break;
            default:
                LL1error();
        }
    }
    private void ActualParameter() throws ParseException {
        OrdinalExpression();
    }
    private void Expression() throws ParseException {
        SimpleExpression();
        OptionalRelationalPart();
    }
    private void OptionalRelationalPart() throws ParseException {
        switch (lookahead) {
            case MP_COMMA:
            case MP_SCOLON:
            case MP_RPAREN:
            case MP_DOWNTO:
            case MP_DO:
            case MP_END:
            case MP_THEN:
            case MP_UNTIL:
                break;
            case MP_EQUAL:
            case MP_LTHAN:
            case MP_GTHAN:
            case MP_LEQUAL:
            case MP_GEQUAL:
            case MP_NEQUAL:
                RelationalOperator();
                SimpleExpression();
                break;
            default:
                LL1error();
        }
    }
    private void RelationalOperator() throws ParseException {
        switch (lookahead) {
            case MP_EQUAL:
                matchLookAhead(Token.MP_EQUAL);
                break;
            case MP_LTHAN:
                matchLookAhead(Token.MP_LTHAN);
                break;
            case MP_GTHAN:
                matchLookAhead(Token.MP_GTHAN);
                break;
            case MP_LEQUAL:
                matchLookAhead(Token.MP_LEQUAL);
                break;
            case MP_GEQUAL:
                matchLookAhead(Token.MP_GEQUAL);
                break;
            case MP_NEQUAL:
                matchLookAhead(Token.MP_NEQUAL);
                break;
            default:
                LL1error();
        }
    }
    private void SimpleExpression() throws ParseException {
        OptionalSign();
        Term();
        TermTail();
    }
    private void TermTail() throws ParseException {
        switch (lookahead) {
            case MP_COMMA:
                break;
            case MP_SCOLON:
                break;
            case MP_RPAREN:
                break;
            case MP_EQUAL:
                break;
            case MP_LTHAN:
                break;
            case MP_GTHAN:
                break;
            case MP_LEQUAL:
                break;
            case MP_GEQUAL:
                break;
            case MP_NEQUAL:
                break;
            case MP_PLUS:
                break;
            case MP_MINUS:
                break;
            case MP_DO:
                break;
            case MP_DOWNTO:
                break;
            case MP_ELSE:
                break;
            case MP_END:
                break;
            case MP_OR:
                break;
            case MP_THEN:
                break;
            case MP_TO:
                break;
            case MP_UNTIL:
                break;
            default:
                LL1error();
        }
    }
    private void OptionalSign() {}
    private void AddingOperator() {}
    private void Term() {}
    private void FactorTail() {}
    private void MultiplyingOperator() {}
    private void Factor() {}
    private void ProgramIdentifier() {}
    private void VariableIdentifier() {}
    private void ProcedureIdentifier() {}
    private void FunctionIdentifier() {}
    private void BooleanExpression() {}
    private void OrdinalExpression() {}
    private void IdentifierList() {}
    private void IdentifierTail() {}


}
