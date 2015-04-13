package parser;

import core.Token;
import scanner.Dispatcher;
import symbolTable.Kind;
import symbolTable.SymbolTable;
import symbolTable.TableEntry;

import java.util.LinkedList;

/**
 * Created by Christina on 3/6/2015.
 */
public class Parser {
    private Dispatcher dispatcher;
    private LinkedList stack;
    private Token lookahead;
    private LinkedList<SymbolTable> tables;

    public Parser(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.tables = new LinkedList<SymbolTable>();
    }
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
    private TableEntry getEntry(String lexeme) {
        TableEntry entry = null;
        for(SymbolTable table : tables) {
            if(table.hasEntry(lexeme))
            return table.getEntry(lexeme);
        }
        return null;
    }

    // save parse tree rules to file
    private void parseTree(int n) {

    }
    private void VariableDeclaration() {}
    private void VariableDeclarationPart() throws ParseException {
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
    private void StatementTail() {
        switch (lookahead) {
            case MP_AND:
                break;
            default:
                break;
        }
    }
    private void Statement() throws ParseException {
        // complete
        switch (lookahead) {
            case MP_SCOLON:
                EmptyStatement();
                break;
            case MP_BEGIN:
                CompoundStatement();
                break;
            case MP_ELSE:
                EmptyStatement();
                break;
            case MP_END:
                EmptyStatement();
                break;
            case MP_FOR:
                ForStatement();
                break;
            case MP_IF:
                IfStatement();
                break;
            case MP_READ:
                ReadStatement();
                break;
            case MP_REPEAT:
                RepeatStatement();
                break;
            case MP_UNTIL:
                EmptyStatement();
                break;
            case MP_WHILE:
                WhileStatement();
                break;
            case MP_WRITE:
                WriteStatement();
                break;
            case MP_IDENTIFIER:
                // Identifier kind conflict resolution
                String lex = dispatcher.getLexeme();
                TableEntry entry = getEntry(lex);
                if(entry.kind == Kind.PROCEDURE)
                    ProcedureStatement();   // procedure identifier
                else
                    EmptyStatement();

                break;
            default:
                LL1error();
        }

    }
    private void EmptyStatement() throws ParseException {
        // complete
        switch (lookahead) {
            case MP_SCOLON: break;
            case MP_ELSE: break;
            case MP_END: break;
            case MP_UNTIL: break;
            default:
                LL1error();
        }
    }
    private void ReadStatement() throws ParseException {
        // complete
        matchLookAhead(Token.MP_READ);
        matchLookAhead(Token.MP_LPAREN);
        ReadParameter();
        ReadParameterTail();
        matchLookAhead(Token.MP_RPAREN);
    }
    private void ReadParameter() {
        // complete
        VariableIdentifier();
    }
    private void ReadParameterTail() throws ParseException {
        // complete
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
        // complete
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
        // complete
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
        // complete
        OrdinalExpression();
    }
    private void IfStatement() throws ParseException {
        // complete
        matchLookAhead(Token.MP_IF);
        BooleanExpression();
        matchLookAhead(Token.MP_THEN);
        Statement();
        OptionalElsePart();
    }
    private void OptionalElsePart() throws ParseException {
        // complete
        switch (lookahead) {
            case MP_ELSE:
                /* using greedy/nearest match for conflict resolution */
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
        // complete
        matchLookAhead(Token.MP_REPEAT);
        StatementSequence();
        matchLookAhead(Token.MP_UNTIL);
        BooleanExpression();
    }
    private void WhileStatement() throws ParseException {
        // complete
        matchLookAhead(Token.MP_WHILE);
        BooleanExpression();
        matchLookAhead(Token.MP_DO);
        Statement();
    }
    private void ForStatement() throws ParseException {
        // complete
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
        // complete
        VariableIdentifier();
    }
    private void InitialValue() {
        // complete
        OrdinalExpression();
    }
    private void StepValue() throws ParseException {
        // complete
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
        // complete
        OrdinalExpression();
    }
    private void ProcedureStatement() throws ParseException {
        // complete
        ProcedureIdentifier();
        OptionalActualParameterList();
    }
    private void OptionalActualParameterList() throws ParseException {
        // complete
        switch (lookahead) {
            case MP_LPAREN:
                matchLookAhead(Token.MP_LPAREN);
                ActualParameter();
                ActualParameterTail();
                matchLookAhead(Token.MP_RPAREN);
                break;
            case MP_SCOLON:
            case MP_UNTIL:
            case MP_ELSE:
            case MP_END:
                break;
            default:
                LL1error();
        }
    }
    private void ActualParameterTail() throws ParseException {
        // complete
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
        // complete
        OrdinalExpression();
    }
    private void Expression() throws ParseException {
        // complete
        SimpleExpression();
        OptionalRelationalPart();
    }
    private void OptionalRelationalPart() throws ParseException {
        // complete
        switch (lookahead) {
            case MP_COMMA:
            case MP_SCOLON:
            case MP_LPAREN:
            case MP_DOWNTO:
            case MP_DO:
            case MP_END:
            case MP_ELSE:
            case MP_THEN:
            case MP_TO:
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
        // complete
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
        // complete
        OptionalSign();
        Term();
        TermTail();
    }
    private void TermTail() throws ParseException {
        // complete
        switch (lookahead) {
            case MP_COMMA:
            case MP_SCOLON:
            case MP_RPAREN:
            case MP_EQUAL:
            case MP_LTHAN:
            case MP_GTHAN:
            case MP_LEQUAL:
            case MP_GEQUAL:
            case MP_NEQUAL:
            case MP_DO:
            case MP_DOWNTO:
            case MP_ELSE:
            case MP_END:
            case MP_OR:
            case MP_THEN:
            case MP_TO:
            case MP_UNTIL:
                break;
            case MP_PLUS:
            case MP_MINUS:
            AddingOperator();
            Term();
            TermTail();
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
