package parser;

import core.Token;
import scanner.Dispatcher;
import symbolTable.*;
import analyzer.*;

import java.io.*;
import java.util.HashMap;

/**
 * Created by Christina on 3/6/2015.
 */
public class Parser {

    private Dispatcher dispatcher;
    private Token lookahead;
    private TableStack stack;
    private EntryBuilder tableEntry;
    private String parse;
    private int nest;
    private HashMap rules = RuleLookup.getRules();
    private Analyzer analyzer = new Analyzer();

    public Parser(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.stack = TableStack.getStack();
        this.parse = "";
        nest = 0;
        tableEntry = new EntryBuilder();
    }
    public void startParse() throws ParseException {
        lookahead = dispatcher.nextToken();
        SystemGoal();
        //System.out.println(parse);
        saveParse();
    }
    // copy trace to file
    public void saveParse() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("parsefile.txt"));
            writer.write("## Trace File ##\n");
            writer.write(parse);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // assert token is a match.
    private void matchLookAhead(Token token) throws ParseException {

        if(lookahead!=token) {
            throw new ParseException(String.format("Parse error on line %s, col %s. Found %s, expected %s.", dispatcher.getLine(), dispatcher.getColumn(),lookahead, token));
        }
        lookahead = dispatcher.nextToken();
    }
    // generic error message when an unexpected token is discovered.
    private void LL1error() throws ParseException {
        throw new ParseException(String.format("Parse error on line %s, col %s. Unexpected token %s, lexeme %s", dispatcher.getLine(), dispatcher.getColumn(), dispatcher.getToken(), dispatcher.getLexeme()));
    }



    // save parse tree rules to file
    private void parseTree(int rule) {
        String r = String.valueOf(rules.get(rule));
        parse += rule;
        parse += " ";
        parse += r;
        parse += "\n";
        System.out.print(rule + " ");
        System.out.println(r);

    }

    /*************************************************
        Parse Stubs Below
     ************************************************/

    // section written by Andrew

    private void SystemGoal() throws ParseException {
        analyzer.preSetup();
        parseTree(1);
        Program();
        matchLookAhead(Token.MP_EOF);
    }
    private void Program() throws ParseException {
        parseTree(2);
        ProgramHeading();
        matchLookAhead(Token.MP_SCOLON);
        Block();
        matchLookAhead(Token.MP_PERIOD);
    }
    private void ProgramHeading() throws ParseException {
        parseTree(3);
        tableEntry.setKind(Kind.PROGRAM);
        matchLookAhead(Token.MP_PROGRAM);
        ProgramIdentifier();
        stack.createTable(tableEntry);
        tableEntry = new EntryBuilder();
    }
    private void Block() throws ParseException
    {
        parseTree(4);
        VariableDeclarationPart();
        ProcedureAndFunctionDeclarationPart();
        StatementPart();
    }

    private void VariableDeclarationPart() throws ParseException {
        switch (lookahead) {
            case MP_VAR:
                parseTree(5);
                tableEntry.setKind(Kind.VARIABLE);
                matchLookAhead(Token.MP_VAR);
                VariableDeclaration();
                matchLookAhead(Token.MP_SCOLON);
                VariableDeclarationTail();
                tableEntry = new EntryBuilder();
                break;
            case MP_PROCEDURE:
            case MP_FUNCTION:
            case MP_BEGIN:
                parseTree(6);
                break;
            default:
                LL1error();
        }
    }

    private void VariableDeclarationTail() throws ParseException
    {
        switch(lookahead){
            case MP_IDENTIFIER:
                parseTree(7);
                VariableDeclaration();
                matchLookAhead(Token.MP_SCOLON);
                VariableDeclarationTail();
                break;
            case MP_PROCEDURE:
            case MP_FUNCTION:
            case MP_BEGIN:
                parseTree(8);
                break;
            default:
                 LL1error();
        }
    }

    private void VariableDeclaration() throws ParseException    //need to use a list here for variables and set type for them.
    {
        parseTree(9);
        IdentifierList();
        matchLookAhead(Token.MP_COLON);
        Type();
        boolean flag = stack.addEntry(tableEntry);
        if(flag)
        {
            tableEntry = new EntryBuilder();
        }
        tableEntry.setKind(Kind.VARIABLE);
    }

    private void Type() throws ParseException {
        switch (lookahead) {
            case MP_INTEGER:
                parseTree(10);
                tableEntry.setType(Type.INTEGER);
                matchLookAhead(Token.MP_INTEGER);
                break;
            case MP_FLOAT:
                parseTree(11);
                tableEntry.setType(Type.INTEGER);
                matchLookAhead(Token.MP_FLOAT);
                break;
            case MP_STRING:
                parseTree(12);
                tableEntry.setType(Type.STRING);
                matchLookAhead(Token.MP_STRING);
                break;
            case MP_BOOLEAN:
                parseTree(13);
                tableEntry.setType(Type.BOOLEAN);
                matchLookAhead(Token.MP_BOOLEAN);
                break;
            default:
                LL1error();
        }
    }

    private void ProcedureAndFunctionDeclarationPart() throws ParseException {
        switch (lookahead) {
            case MP_PROCEDURE:
                parseTree(14);
                ProcedureDeclaration();
                ProcedureAndFunctionDeclarationPart();
                break;
            case MP_FUNCTION:
                parseTree(15);
                FunctionDeclaration();
                ProcedureAndFunctionDeclarationPart();
                break;
            case MP_BEGIN:
                parseTree(16);
                break;
            default:
                LL1error();
        }
    }

    private void ProcedureDeclaration() throws ParseException
    {
        parseTree(17);
        ProcedureHeading();
        matchLookAhead(Token.MP_SCOLON);
        Block();
        matchLookAhead(Token.MP_SCOLON);
    }

    private void FunctionDeclaration() throws ParseException
    {
        parseTree(18);
        FunctionHeading();
        matchLookAhead(Token.MP_SCOLON);
        Block();
        matchLookAhead(Token.MP_SCOLON);
    }

    private void ProcedureHeading() throws ParseException
    {
        parseTree(19);
        tableEntry.setKind(Kind.PROCEDURE);
        matchLookAhead(Token.MP_PROCEDURE);
        ProcedureIdentifier();
        OptionalFormalParameterList();
        boolean flag = stack.addEntry(tableEntry);
        if(flag)
        {
            tableEntry = new EntryBuilder();
        }
        stack.createTable(tableEntry);
        tableEntry = new EntryBuilder();
    }

    private void FunctionHeading() throws ParseException
    {
        parseTree(20);
        tableEntry.setKind(Kind.FUNCTION);
        matchLookAhead(Token.MP_FUNCTION);
        FunctionIdentifier();
        OptionalFormalParameterList();
        matchLookAhead(Token.MP_COLON);
        Type();
        boolean flag = stack.addEntry(tableEntry);
        if(flag)
        {
            tableEntry = new EntryBuilder();
        }
        stack.createTable(tableEntry);
        tableEntry = new EntryBuilder();
    }

    private void OptionalFormalParameterList() throws ParseException
    {
        switch(lookahead){
            case MP_LPAREN:
                parseTree(21);
                matchLookAhead(Token.MP_LPAREN);
                FormalParameterSection();
                FormalParameterSectionTail();
                matchLookAhead(Token.MP_RPAREN);
                break;
            case MP_COLON:
            case MP_SCOLON:
                parseTree(22);
                break;
            default:
                LL1error();
        }
    }

    private void FormalParameterSectionTail() throws ParseException
    {
        switch(lookahead){
            case MP_SCOLON:
                parseTree(23);
                matchLookAhead(Token.MP_SCOLON);
                FormalParameterSection();
                FormalParameterSectionTail();
                break;
            case MP_RPAREN:
                parseTree(24);
                break;
            default:
                LL1error();
        }
    }

    private void FormalParameterSection() throws ParseException
    {
        switch(lookahead){
            case MP_IDENTIFIER:
                parseTree(25);
                ValueParameterSection();
                break;
            case MP_VAR:
                parseTree(26);
                VariableParameterSection();
                break;
            default:
                LL1error();
        }
    }

    private void ValueParameterSection() throws ParseException
    {
        parseTree(27);
        EntryBuilder temp = tableEntry;
        tableEntry = new EntryBuilder();
        tableEntry.setKind(Kind.PARAMETER);
        IdentifierList();
        matchLookAhead(Token.MP_COLON);
        Type();
        // hacky stuff follows
        int offset = 0;
        for (String lexeme : tableEntry.getLexemes()) {
            temp.addParam(new TableEntry(lexeme, offset, tableEntry));
            offset++;
        }
        tableEntry = temp;
    }

    private void VariableParameterSection() throws ParseException
    {
        switch(lookahead) {
            case MP_VAR:
                parseTree(28);
                EntryBuilder temp = tableEntry;
                tableEntry = new EntryBuilder();
                tableEntry.setKind(Kind.VARIABLE);
                matchLookAhead(Token.MP_VAR);
                IdentifierList();
                matchLookAhead(Token.MP_COLON);
                Type();
                // hacky stuff follows
                int offset = 0;
                for (String lexeme : tableEntry.getLexemes()) {
                    temp.addParam(new TableEntry(lexeme, offset, tableEntry));
                    offset++;
                }
                tableEntry = temp;
                break;
            default:
                LL1error();
        }
    }

    private void StatementPart() throws ParseException
    {
        parseTree(29);
        CompoundStatement();
    }

    private void CompoundStatement() throws ParseException
    {
        parseTree(30);
        matchLookAhead(Token.MP_BEGIN);
        StatementSequence();
        matchLookAhead(Token.MP_END);
    }

    private void StatementSequence() throws ParseException
    {
        parseTree(31);
        Statement();
        StatementTail();
    }

    private void StatementTail() throws ParseException {
        switch (lookahead) {
            case MP_END:
            case MP_UNTIL:
                parseTree(33);
                break;
            case MP_SCOLON:
                parseTree(32);
                matchLookAhead(Token.MP_SCOLON);
                Statement();
                StatementTail();
                break;
            default:
                LL1error();
        }
    }

    // section written by Christina

    private void Statement() throws ParseException {

        switch (lookahead) {
            case MP_SCOLON:
            case MP_ELSE:
            case MP_END:
            case MP_UNTIL:
                parseTree(34);
                EmptyStatement();
                break;
            case MP_BEGIN:
                parseTree(35);
                CompoundStatement();
                break;
            case MP_READ:
                parseTree(36);
                ReadStatement();
                break;
            case MP_WRITE:
                parseTree(37);
                WriteStatement();
                break;
            case MP_IDENTIFIER:
                // Identifier kind conflict resolution
                TableEntry entry;
                try
                {
                   entry = stack.getEntry(dispatcher.getLexeme());
                } catch (ParseException p)
                {
                    throw new ParseException(p.getMessage() + String.format("Line %s, Col %s, Token %s", dispatcher.getLine(), dispatcher.getColumn(), dispatcher.getToken().toString()));
                }
                try
                {
                    Kind myKind = entry.kind;
                } catch (NullPointerException np)
                {
                    throw new ParseException(String.format("Line %s, Col %s, Token %s", dispatcher.getLine(), dispatcher.getColumn(), dispatcher.getToken().toString()));
                }

                if(entry.kind == Kind.PROCEDURE) {
                    parseTree(43);
                    ProcedureStatement();   // procedure identifier
                }
                else if(entry.kind == Kind.VARIABLE || entry.kind == Kind.FUNCTION) {
                    parseTree(38);
                    AssignmentStatement();
                }
                break;
            case MP_IF:
                parseTree(39);
                IfStatement();
                break;
            case MP_WHILE:
                parseTree(40);
                WhileStatement();
                break;
            case MP_REPEAT:
                parseTree(41);
                RepeatStatement();
                break;
            case MP_FOR:
                parseTree(42);
                ForStatement();
                break;
            default:
                LL1error();
        }

    }

    private void EmptyStatement() throws ParseException {

        switch (lookahead) {
            case MP_SCOLON:
            case MP_ELSE:
            case MP_END:
            case MP_UNTIL:
                parseTree(44);
                break;
            default:
                LL1error();
        }
    }
    private void ReadStatement() throws ParseException {

        parseTree(45);
        matchLookAhead(Token.MP_READ);
        matchLookAhead(Token.MP_LPAREN);
        ReadParameter();
        ReadParameterTail();
        matchLookAhead(Token.MP_RPAREN);
    }
    private void ReadParameterTail() throws ParseException {

        switch (lookahead) {
            case MP_COMMA:
                parseTree(46);
                matchLookAhead(Token.MP_COMMA);
                ReadParameter();
                ReadParameterTail();
                break;
            case MP_RPAREN:
                parseTree(47);
                break;
            default:
                LL1error();
        }
    }
    private void ReadParameter() throws ParseException {

        parseTree(48);
        VariableIdentifier();
    }
    private void WriteStatement() throws ParseException {

        switch (lookahead) {
            case MP_WRITE:
                parseTree(49);
                matchLookAhead(Token.MP_WRITE);
                matchLookAhead(Token.MP_LPAREN);
                WriteParameter();
                WriteParameterTail();
                matchLookAhead(Token.MP_RPAREN);
                break;
            case MP_WRITELN:
                parseTree(50);
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
                parseTree(51);
                matchLookAhead(Token.MP_COMMA);
                WriteParameter();
                WriteParameterTail();
                break;
            case MP_RPAREN:
                parseTree(52);
                break;
        default:
            LL1error();
        }
    }
    private void WriteParameter() throws ParseException {
        parseTree(53);
        OrdinalExpression();
    }
    private void AssignmentStatement() throws ParseException {

        TableEntry entry;
        try
        {
            entry = stack.getEntry(dispatcher.getLexeme());
        } catch (ParseException p)
        {
            throw new ParseException(p.getMessage() + String.format("Line %s, Col %s, Token %s", dispatcher.getLine(), dispatcher.getColumn(), dispatcher.getToken().toString()));
        }
        if(entry.kind == Kind.FUNCTION) {
            parseTree(55);
            FunctionIdentifier();

        }
        else if (entry.kind == Kind.VARIABLE) {
            parseTree(54);
            VariableIdentifier();
        }
        else {
            System.out.println(" (AssignmentStatement) This code should be unreachable.");
        }
        
        matchLookAhead(Token.MP_ASSIGN);
        Expression();
    }
    private void IfStatement() throws ParseException {

        parseTree(56);
        matchLookAhead(Token.MP_IF);
        BooleanExpression();
        matchLookAhead(Token.MP_THEN);
        Statement();
        OptionalElsePart();
    }
    private void OptionalElsePart() throws ParseException {

        switch (lookahead) {
            case MP_ELSE:
                /* using greedy/nearest match for conflict resolution */
                parseTree(57);
                matchLookAhead(Token.MP_ELSE);
                Statement();
                break;
            case MP_SCOLON:
            case MP_END:
            case MP_UNTIL:
                parseTree(58);
                break;
            default:
                LL1error();
        }
    }
    private void RepeatStatement() throws ParseException {

        parseTree(59);
        matchLookAhead(Token.MP_REPEAT);
        StatementSequence();
        matchLookAhead(Token.MP_UNTIL);
        BooleanExpression();
    }
    private void WhileStatement() throws ParseException {

        parseTree(60);
        matchLookAhead(Token.MP_WHILE);
        BooleanExpression();
        matchLookAhead(Token.MP_DO);
        Statement();
    }
    private void ForStatement() throws ParseException {

        parseTree(61);
        matchLookAhead(Token.MP_FOR);
        ControlVariable();
        matchLookAhead(Token.MP_ASSIGN);
        InitialValue();
        StepValue();
        FinalValue();
        matchLookAhead(Token.MP_DO);
        Statement();
    }
    private void ControlVariable() throws ParseException {

        parseTree(62);
        VariableIdentifier();
    }
    private void InitialValue() throws ParseException {

        parseTree(63);
        OrdinalExpression();
    }
    private void StepValue() throws ParseException {

        switch (lookahead) {
            case MP_TO:
                parseTree(64);
                matchLookAhead(Token.MP_TO);
                break;
            case MP_DOWNTO:
                parseTree(65);
                matchLookAhead(Token.MP_DOWNTO);
                break;
            default:
                LL1error();
        }
    }
    private void FinalValue() throws ParseException {
        parseTree(66);
        OrdinalExpression();
    }
    private void ProcedureStatement() throws ParseException {
        parseTree(67);
        ProcedureIdentifier();
        OptionalActualParameterList();
    }
    private void OptionalActualParameterList() throws ParseException {
        switch (lookahead) {
            case MP_LPAREN:
                parseTree(68);
                matchLookAhead(Token.MP_LPAREN);
                ActualParameter();
                ActualParameterTail();
                matchLookAhead(Token.MP_RPAREN);
                break;
            case MP_SCOLON:
            case MP_UNTIL:
            case MP_ELSE:
            case MP_END:
                parseTree(69);
                break;
            default:
                LL1error();
        }
    }
    private void ActualParameterTail() throws ParseException {

        switch (lookahead) {
            case MP_COMMA:
                parseTree(70);
                matchLookAhead(Token.MP_COMMA);
                ActualParameter();
                ActualParameterTail();
                break;
            case MP_RPAREN:
                parseTree(71);
                break;
            default:
                LL1error();
        }
    }
    private void ActualParameter() throws ParseException {
        parseTree(72);
        OrdinalExpression();
    }
    private void Expression() throws ParseException {
                parseTree(73);
                SimpleExpression();
                OptionalRelationalPart();
}
    private void OptionalRelationalPart() throws ParseException {
        switch (lookahead) {
            case MP_COMMA:
            case MP_SCOLON:
            case MP_RPAREN:     // added for fix
            case MP_LPAREN:
            case MP_DOWNTO:
            case MP_DO:
            case MP_END:
            case MP_ELSE:
            case MP_THEN:
            case MP_TO:
            case MP_UNTIL:
                parseTree(75);
                break;
            case MP_EQUAL:
            case MP_LTHAN:
            case MP_GTHAN:
            case MP_LEQUAL:
            case MP_GEQUAL:
            case MP_NEQUAL:
                parseTree(74);
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
                parseTree(76);
                matchLookAhead(Token.MP_EQUAL);
                break;
            case MP_LTHAN:
                parseTree(77);
                matchLookAhead(Token.MP_LTHAN);
                break;
            case MP_GTHAN:
                parseTree(78);
                matchLookAhead(Token.MP_GTHAN);
                break;
            case MP_LEQUAL:
                parseTree(79);
                matchLookAhead(Token.MP_LEQUAL);
                break;
            case MP_GEQUAL:
                parseTree(80);
                matchLookAhead(Token.MP_GEQUAL);
                break;
            case MP_NEQUAL:
                parseTree(81);
                matchLookAhead(Token.MP_NEQUAL);
                break;
            default:
                LL1error();
        }
    }
    private void SimpleExpression() throws ParseException {
                parseTree(82);
                OptionalSign();
                Term();
                TermTail();
    }
    private void TermTail() throws ParseException {
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
                parseTree(84);
                break;
            case MP_PLUS:
            case MP_MINUS:
                parseTree(83);
                AddingOperator();
                Term();
                TermTail();
                break;
            default:
                LL1error();
        }
    }

    // section written by Hunter

    private void OptionalSign() throws ParseException {
        switch(lookahead){
            case MP_FALSE:
            case MP_NOT:
            case MP_TRUE:
            case MP_IDENTIFIER:
            case MP_INTEGER_LIT:
            case MP_FLOAT_LIT:
            case MP_STRING_LIT:
            case MP_LPAREN:
                parseTree(87);
                break;
            case MP_PLUS:
                parseTree(85);
                matchLookAhead(Token.MP_PLUS);
                break;
            case MP_MINUS:
                parseTree(86);
                matchLookAhead(Token.MP_MINUS);
                break;
            default:
                LL1error();
        }
        
    }
    private void AddingOperator() throws ParseException{
        switch(lookahead){
            case MP_PLUS:
                parseTree(88);
                matchLookAhead(Token.MP_PLUS);
                break;
            case MP_MINUS:
                parseTree(89);
                matchLookAhead(Token.MP_MINUS);
                break;
            case MP_OR:
                parseTree(90);
                matchLookAhead(Token.MP_OR);
            default:
                LL1error();
        }
    }

    private void Term() throws ParseException {
                parseTree(91);
                Factor();
                FactorTail();
    }
    private void FactorTail() throws ParseException
    {
        switch(lookahead){
            case MP_FLOAT_DIVIDE:
            case MP_TIMES:
            case MP_MOD:
            case MP_AND:
                parseTree(92);
                MultiplyingOperator();
                Factor();
                FactorTail();
                break;
            case MP_DO:
            case MP_DOWNTO:
            case MP_ELSE:
            case MP_END:
            case MP_OR:
            case MP_THEN:
            case MP_TO:
            case MP_UNTIL:
            case MP_COMMA:
            case MP_SCOLON:
            case MP_RPAREN:
            case MP_EQUAL:
            case MP_GTHAN:
            case MP_LTHAN:
            case MP_LEQUAL:
            case MP_GEQUAL:
            case MP_NEQUAL:
            case MP_PLUS:
            case MP_MINUS:
                parseTree(93);
                break;
            default:
                LL1error();
        }
    }
    private void MultiplyingOperator() throws ParseException
    {
        switch(lookahead){
            case MP_TIMES:
                parseTree(94);
                matchLookAhead(Token.MP_TIMES);
                break;
            case MP_FLOAT_DIVIDE:
                parseTree(95);
                matchLookAhead(Token.MP_FLOAT_DIVIDE);
                break;
            case MP_DIV:
                parseTree(96);
                matchLookAhead(Token.MP_DIV);
                break;
            case MP_MOD:
                parseTree(97);
                matchLookAhead(Token.MP_MOD);
                break;
            case MP_AND:
                parseTree(98);
                matchLookAhead(Token.MP_AND);
                break;

            default:
                LL1error();
        }
    }

    private void Factor() throws ParseException
    {
        switch(lookahead){
            case MP_NOT:
                parseTree(104);
                matchLookAhead(Token.MP_NOT);
                Factor();
                break;
            case MP_LPAREN:
                parseTree(105);
                matchLookAhead(Token.MP_LPAREN);
                Expression();
                matchLookAhead(Token.MP_RPAREN);
                break;
            case MP_INTEGER_LIT:
                parseTree(99);
                matchLookAhead(Token.MP_INTEGER_LIT);
                break;
            case MP_FIXED_LIT:
                //parseTree(0);
                matchLookAhead(Token.MP_FIXED_LIT);
                break;
            case MP_FLOAT_LIT:
                parseTree(100);
                matchLookAhead(Token.MP_FLOAT_LIT);
                break;
            case MP_STRING_LIT:
                parseTree(101);
                matchLookAhead(Token.MP_STRING_LIT);
                break;
            case MP_TRUE:
                parseTree(102);
                matchLookAhead(Token.MP_TRUE);
                break;
            case MP_FALSE:
                parseTree(103);
                matchLookAhead(Token.MP_FALSE);
                break;
            case MP_IDENTIFIER:
                TableEntry entry;
                try
                {
                    entry = stack.getEntry(dispatcher.getLexeme());
                } catch (ParseException p)
                {
                    throw new ParseException(p.getMessage() + String.format("Line %s, Col %s, Token %s", dispatcher.getLine(), dispatcher.getColumn(), dispatcher.getToken().toString()));
                }
                if(entry.kind == Kind.VARIABLE) {
                    parseTree(116);
                    VariableIdentifier();
                }
                else if (entry.kind == Kind.FUNCTION) {
                    parseTree(106);
                    FunctionIdentifier();
                    OptionalActualParameterList();
                }
                break;
            default:
                LL1error();
        }
    }
    private void ProgramIdentifier() throws ParseException {
        parseTree(107);
        if(lookahead==Token.MP_IDENTIFIER) {                // the table entry needs to be updated before running our match.
            tableEntry.setLexeme(dispatcher.getLexeme());
        }
        matchLookAhead(Token.MP_IDENTIFIER);
    }
    private void VariableIdentifier() throws ParseException {
        parseTree(108);
        if(lookahead==Token.MP_IDENTIFIER) {                // the table entry needs to be updated before running our match.
            tableEntry.setLexeme(dispatcher.getLexeme());
        }
        matchLookAhead(Token.MP_IDENTIFIER);
    }
    private void ProcedureIdentifier() throws ParseException {
        parseTree(109);
        if(lookahead==Token.MP_IDENTIFIER) {
            tableEntry.setLexeme(dispatcher.getLexeme());
        }
        matchLookAhead(Token.MP_IDENTIFIER);
    }
    private void FunctionIdentifier() throws ParseException {
        parseTree(110);
        if(lookahead==Token.MP_IDENTIFIER) {
            tableEntry.setLexeme(dispatcher.getLexeme());
        }
        matchLookAhead(Token.MP_IDENTIFIER);}
    private void BooleanExpression() throws ParseException {
                parseTree(111);
                Expression();
    }
    private void OrdinalExpression() throws ParseException {
                parseTree(112);
                Expression();
    }
    private void IdentifierList() throws ParseException {
                parseTree(113);
                if(lookahead==Token.MP_IDENTIFIER) {
                    tableEntry.setLexeme(dispatcher.getLexeme());
                }
                matchLookAhead(Token.MP_IDENTIFIER);
                IdentifierTail();
    }
    private void IdentifierTail() throws ParseException
    {
        switch(lookahead){
            case MP_COMMA:
                parseTree(114);
                matchLookAhead(Token.MP_COMMA);
                if(lookahead==Token.MP_IDENTIFIER) {
                    tableEntry.setLexeme(dispatcher.getLexeme());
                }
                matchLookAhead(Token.MP_IDENTIFIER);
                IdentifierTail();
                break;
            case MP_COLON:
                parseTree(115);
                break;
            default:
                LL1error();
        }
    }

}