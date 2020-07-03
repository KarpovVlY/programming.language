package com.company.Parser;

import com.company.AbstractSyntaxTree.BaseStatements.BaseStatement;
import com.company.AbstractSyntaxTree.ConditionStatements.ElseStatement;
import com.company.AbstractSyntaxTree.CyclesStatements.WhileStatement;
import com.company.AbstractSyntaxTree.ExpressionStatements.ExpressionStatement;
import com.company.AbstractSyntaxTree.ConditionStatements.IfStatement;
import com.company.AbstractSyntaxTree.ExpressionStatements.VariableDeclarationStatement;
import com.company.AbstractSyntaxTree.FunctionStatements.FunctionCall;
import com.company.AbstractSyntaxTree.ObjectStatements.ObjectStatement;
import com.company.AbstractSyntaxTree.PrintStatements.PrintStatement;
import com.company.AbstractSyntaxTree.PrintStatements.PrintlnStatement;
import com.company.Service.ComplexStatement;
import com.company.Service.Token;
import com.company.Service.TokenType;

import java.util.ArrayList;

public class Parser
{
    private int position = 0;
    private ArrayList<Token> tokens;

    private BaseStatement currentStatement;

    private boolean isBlocked = false;
    private int tabOrder = 0;

    public Parser(ArrayList<Token> tokens) throws Exception
    {
        this.tokens = tokens;

        startProcessing();
    }

    private void startProcessing() throws Exception
    {
        if(_tab_())
        {
            ++tabOrder;
            startProcessing();
        }
        else if (_var_())
        {
            processVariableDeclaration();
        }
        else if (_id_())
        {
            processExpressionStatement();
        }
        else if (_if_())
        {
            processIfStatement();
        }
        else if (_else_())
        {
            processElseStatement();
        }
        else if (_while_())
        {
            processWhileStatement();
        }
        else if(_print_())
        {
            processPrintStatement();
        }
        else if(_println_())
        {
            processPrintlnStatement();
        }
        else if(position >= tokens.size())
        {

        }
        else
        {
            processExpression();
        }
    }


    private void processVariableDeclaration() throws Exception
    {
        currentStatement = new VariableDeclarationStatement();

        if(_id_())
        {
            ((VariableDeclarationStatement)currentStatement).setExpressionStatement(addTokenToStatement());

            if (_equal_())
                processExpression();
            else if(_new_())
                processNewObject();
            else if (position != tokens.size())
                throw new Exception("Некорректный ввод выражения");
        }
        else
            throw new Exception("Пропущен идентификатор после var");

    }

    private void processExpressionStatement() throws Exception
    {
        Token id = addTokenToStatement();

        if(_equal_())
        {
            currentStatement = new ExpressionStatement(id);
            processExpression();
        }
        else if(_call_())
        {
            currentStatement = processFunctionCall(id);
        }
        else if(_new_())
        {
            currentStatement = new ExpressionStatement(id);
            processNewObject();
        }
        else
            throw new Exception("Некорректный ввод выражения");
    }

    private void processIfStatement() throws Exception
    {
        currentStatement = new IfStatement(tabOrder);

        processExpression();

        if(addTokenToStatement().getTokenType() != TokenType.COLON)
            throw new Exception("Ошибка, пропущено : при обработке if");

        isBlocked = true;
    }

    private void processElseStatement() throws Exception
    {
        currentStatement = new ElseStatement(tabOrder);

        if(tokens.get(position).getTokenType() != TokenType.COLON)
            throw new Exception("Ошибка, пропущено : при обработке else");

        isBlocked = true;
    }

    private void processWhileStatement() throws Exception
    {
        currentStatement = new WhileStatement(tabOrder);

        processExpression();

        if(addTokenToStatement().getTokenType() != TokenType.COLON)
            throw new Exception("Ошибка, пропущено : при обработке while");

        isBlocked = true;
    }

    private void processNewObject() throws Exception
    {
        if(_list_())
            currentStatement.addToken(new ObjectStatement(addTokenToStatement()));
        else
            throw new Exception("Неизвестный объект");
    }


    private void processExpression() throws Exception
    {
        if(_id_())
            processSequence(addTokenToStatement(), false);
        else if(_digit_() || _boolean_())
            processSequence(null, false);
        else if(_left_bracket_())
            processSequence(null, true);
        else
            throw new Exception("Ошибка в выражении, пропущено значение");
    }



    private FunctionCall processFunctionCall(Token functionId) throws Exception
    {
        FunctionCall functionCall = new FunctionCall(functionId);

        if(_id_())
        {
            functionCall.setFunctionName(addTokenToStatement());

            if (_left_bracket_())
                processFunctionSequence(functionCall);
        }

        functionCall.process();

        return functionCall;
    }

    private void processFunctionSequence(FunctionCall functionCall)
    {
        Token currentToken;
        int[] brackets = {1, 0};

        for(;position < tokens.size(); position++)
        {
            currentToken = tokens.get(position);

            if (currentToken.getTokenType() == TokenType.RIGHT_BRACKET)
                ++brackets[1];
            else if (currentToken.getTokenType() == TokenType.LEFT_BRACKET)
                ++brackets[0];

            if (brackets[0] == brackets[1])
                break;

            functionCall.addToken(tokens.get(position));
        }
        ++position;

    }


    private void processSequence(Token id, boolean step) throws Exception
    {
        Token previous = id;

        if(previous == null)
            fillStatement(addTokenToStatement());

        while (position != tokens.size() && !_colon_())
        {
            if(step)
            {
                step = false;

                if(_digit_() || _boolean_())
                    fillStatement(addTokenToStatement());
                else if(_id_())
                    previous = addTokenToStatement();
                else if(_left_bracket_())
                {
                    fillStatement(addTokenToStatement());
                    step = true;
                }
                else
                    throw new Exception("Ошибка в выражении, пропущено значение");
            }
            else
            {
                if(_math_operand_() || _boolean_operand_())
                {
                    step = true;

                    if(previous != null)
                    {
                        fillStatement(previous);
                        previous = null;
                    }

                    fillStatement(addTokenToStatement());
                }
                else if(_right_bracket_())
                {
                    fillStatement(addTokenToStatement());
                    step = false;
                }
                else
                {
                    if(_call_())
                    {
                        fillStatement(processFunctionCall(previous));
                        previous = null;
                    }
                    else
                        throw new Exception("Ошибка в выражении, пропущен операнд");
                }
            }
        }

        if(previous != null)
            currentStatement.addToken(previous);

        if(step)
            throw new Exception("Ошибка в выражении, пропущено значение");
    }

    private void fillStatement(Object ob) throws Exception
    {
        if(currentStatement != null)
            currentStatement.addToken(ob);
    }


    private void processPrintStatement() throws Exception
    {
        currentStatement = new PrintStatement();

        if(position != tokens.size())
            processExpression();
    }

    private void processPrintlnStatement() throws Exception
    {
        currentStatement = new PrintlnStatement();

        if(position != tokens.size())
            processExpression();
    }



    private boolean _var_() { return match(TokenType.VARIABLE_DECLARATION); }
    private boolean _while_() { return match(TokenType.WHILE); }
    private boolean _if_() { return match(TokenType.IF); }
    private boolean _else_() { return match(TokenType.ELSE); }

    private boolean _id_() { return match(TokenType.ID); }
    private boolean _digit_() { return match(TokenType.DIGIT); }

    private boolean _boolean_() { return match(TokenType.BOOLEAN); }

    private boolean _print_() { return match(TokenType.PRINT); }
    private boolean _println_() { return match(TokenType.PRINTLN); }

    private boolean _equal_() { return match(TokenType.EQUAL); }
    private boolean _colon_() { return match(TokenType.COLON); }
    private boolean _tab_() { return match(TokenType.TAB); }

    private boolean _new_() { return match(TokenType.NEW); }
    private boolean _call_() { return match(TokenType.CALL); }
    private boolean _list_() { return match(TokenType.LIST); }

    private boolean _left_bracket_() { return match(TokenType.LEFT_BRACKET); }
    private boolean _right_bracket_() { return match(TokenType.RIGHT_BRACKET); }
    private boolean _coma_() { return match(TokenType.COMA); }

    private boolean _math_operand_() { return match(TokenType.PLUS) || match(TokenType.MINUS)
            || match(TokenType.DIVINE) || match(TokenType.MULTIPLY) || match(TokenType.PERCENT); }

    private boolean _boolean_operand_() { return match(TokenType.POSITIVE_EQUAL) || match(TokenType.NEGATIVE_EQUAL)
            || match(TokenType.LESS) || match(TokenType.LESS_EQUAL)
            || match(TokenType.MORE) || match(TokenType.MORE_EQUAL); }






    private boolean match(TokenType tokenType)
    {

        if(position < tokens.size())
        {
            if(tokens.get(position).getTokenType() == tokenType)
            {
                ++position;
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }

    private Token addTokenToStatement() { return tokens.get(position - 1); }

    public ComplexStatement getParserResult() { return new ComplexStatement(currentStatement, tabOrder, isBlocked); }
}
