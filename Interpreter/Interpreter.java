package com.company.Interpreter;

import com.company.AbstractSyntaxTree.BaseStatements.BaseStatement;
import com.company.AbstractSyntaxTree.ConditionStatements.IfStatement;
import com.company.AbstractSyntaxTree.CyclesStatements.WhileStatement;
import com.company.AbstractSyntaxTree.ExpressionStatements.ExpressionStatement;
import com.company.AbstractSyntaxTree.ExpressionStatements.VariableDeclarationStatement;
import com.company.AbstractSyntaxTree.FunctionStatements.FunctionCall;
import com.company.AbstractSyntaxTree.ObjectStatements.ObjectStatement;
import com.company.AbstractSyntaxTree.PrintStatements.PrintStatement;
import com.company.AbstractSyntaxTree.PrintStatements.PrintlnStatement;
import com.company.Interpreter.Objects.List;
import com.company.Interpreter.Service.FullRPN;
import com.company.Interpreter.Service.LanguageList;
import com.company.Interpreter.Variable.Variable;
import com.company.Interpreter.Variable.VariablesTable;
import com.company.Service.Token;
import com.company.Service.TokenType;

import java.util.ArrayList;

public class Interpreter
{

    private final VariablesTable variablesTable;
    public final Interpreter parent;


    public Interpreter(Interpreter parent)
    {
        variablesTable = new VariablesTable(this);
        this.parent = parent;
    }

    public void execute(BaseStatement statement) throws Exception
    {
        if(statement instanceof VariableDeclarationStatement)
        {
            executeVariableDeclarationStatement((VariableDeclarationStatement)statement);
        }
        else if(statement instanceof ExpressionStatement)
        {
            executeExpressionStatementStatement((ExpressionStatement)statement);
        }
        else if(statement instanceof FunctionCall)
        {
            executeFunctionCall((FunctionCall)statement);
        }
        else if(statement instanceof IfStatement)
        {
            executeIfStatement((IfStatement)statement);
        }
        else if(statement instanceof WhileStatement)
        {
            executeWhileStatement((WhileStatement)statement);
        }
        else if(statement instanceof PrintStatement)
        {
            executePrintStatement((PrintStatement)statement);
        }
        else if(statement instanceof PrintlnStatement)
        {
            executePrintlnStatement((PrintlnStatement)statement);
        }
    }

    private void executeVariableDeclarationStatement(VariableDeclarationStatement variableDeclarationStatement) throws Exception
    {
        ExpressionStatement expressionStatement = variableDeclarationStatement.getExpressionStatement();

        if(!variablesTable.checkVariable(expressionStatement.getID().getTokenValue()))
        {
            ArrayList<Object> elements = expressionStatement.getTokens();

            if(elements.size() == 0)
            {
                variablesTable.addVariable(expressionStatement.getID().getTokenValue(),
                        null,
                        null);
            }
            else
            {
                if(elements.size() == 1)
                {
                    if(expressionStatement.getTokens().get(0) instanceof ObjectStatement)
                    {
                        if(((ObjectStatement)elements.get(0)).getObjectId().getTokenType() == TokenType.LIST)
                        {
                            variablesTable.addVariable(expressionStatement.getID().getTokenValue(),
                                    new List(),
                                    TokenType.LIST);
                        }
                        else
                            throw new Exception("Неизвестный объект");
                    }
                    else
                    {
                        Token res = new FullRPN(elements, this).getResult();

                        variablesTable.addVariable(expressionStatement.getID().getTokenValue(),
                                res.getTokenValue(),
                                res.getTokenType());
                    }
                }
                else
                {
                    Token res = new FullRPN(elements, this).getResult();

                    variablesTable.addVariable(expressionStatement.getID().getTokenValue(),
                            res.getTokenValue(),
                            res.getTokenType());
                }
            }
        }
        else
            throw new Exception("Переменная " + expressionStatement.getID().getTokenValue() + " уже существует");
    }

    private void executeExpressionStatementStatement(ExpressionStatement expressionStatement) throws Exception
    {
        if(variablesTable.checkVariable(expressionStatement.getID().getTokenValue()))
        {
            ArrayList<Object> elements = expressionStatement.getTokens();

            if(elements.size() == 1)
            {
                if(expressionStatement.getTokens().get(0) instanceof ObjectStatement)
                {
                    if(((ObjectStatement)elements.get(0)).getObjectId().getTokenType() == TokenType.LIST)
                    {
                        variablesTable.changeVariable(expressionStatement.getID().getTokenValue(),
                                new LanguageList<Token>(),
                                TokenType.LIST);
                    }
                    else
                        throw new Exception("Неизвестный объект");
                }
                else
                {
                    Token res = new FullRPN(elements, this).getResult();

                    variablesTable.changeVariable(expressionStatement.getID().getTokenValue(),
                            res.getTokenValue(),
                            res.getTokenType());
                }
            }
            else
            {
                Token res = new FullRPN(elements, this).getResult();

                variablesTable.changeVariable(expressionStatement.getID().getTokenValue(),
                        res.getTokenValue(),
                        res.getTokenType());
            }
        }
        else
            throw new Exception("Переменной " + expressionStatement.getID().getTokenValue() + " не существует");
    }

    private void executeFunctionCall(FunctionCall functionCall) throws Exception
    {
        Variable variable = variablesTable.getVariable(functionCall.getFunctionID().getTokenValue());

        if(variable == null)
        {
            throw new Exception("Переменной не существует");
        }
        else if(variable.getValue() instanceof Token)
        {
            throw new Exception("Несоответствие типов");
        }
        else if(variable.getValue() instanceof List)
        {
            ((List) variable.getValue()).executeFunction(functionCall.getFunctionName().getTokenValue(), functionCall.getFunctionArgs(), false, this);
        }
        else
            throw new Exception("Невозможно выполнить вызов функции");
    }

    private void executeIfStatement(IfStatement ifStatement) throws Exception
    {
        Token res = new FullRPN(ifStatement.getTokens(), this).getResult();

        if(res.getTokenType() == TokenType.BOOLEAN)
        {
            if(res.getTokenValue().compareTo("true") == 0)
            {
                if(ifStatement.getPositiveStatements() != null && ifStatement.getPositiveStatements().size() != 0)
                {
                    Interpreter ifInterpreter = new Interpreter(this);

                    for(int i = 0 ; i < ifStatement.getPositiveStatements().size() ; i ++)
                        ifInterpreter.execute(ifStatement.getPositiveStatements().get(i));
                }
            }
            else if(res.getTokenValue().compareTo("false") == 0)
            {
                if(ifStatement.getNegativeStatements() != null && ifStatement.getNegativeStatements().size() != 0)
                {
                    Interpreter ifInterpreter = new Interpreter(this);

                    for(int i = 0 ; i < ifStatement.getNegativeStatements().size() ; i ++)
                        ifInterpreter.execute(ifStatement.getNegativeStatements().get(i));
                }
            }
        }
        else
            throw new Exception("Ошибка при обработке аргументов : ожидалось BOOLEAN");

    }

    private void executeWhileStatement(WhileStatement whileStatement) throws Exception
    {
        Token res = new FullRPN(whileStatement.getTokens(), this).getResult();

        if(res.getTokenType() == TokenType.BOOLEAN)
        {
            if(res.getTokenValue().compareTo("true") == 0)
            {

                while(res.getTokenValue().compareTo("true") == 0)
                {
                    Interpreter whileInterpreter = new Interpreter(this);

                    for(int i = 0 ; i < whileStatement.getStatements().size() ; i ++)
                        whileInterpreter.execute(whileStatement.getStatements().get(i));

                    res = new FullRPN(whileStatement.getTokens(), this).getResult();
                }
            }
        }
        else
            throw new Exception("Ошибка при обработке аргументов : ожидалось BOOLEAN");
    }

    private void executePrintStatement(PrintStatement printStatement) throws Exception
    {
        if(printStatement.getTokens().size() != 0)
            System.out.print(new FullRPN(printStatement.getTokens(),this).getResult().getTokenValue() + "\t");
        else
            System.out.print('\t');
    }

    private void executePrintlnStatement(PrintlnStatement printlnStatement) throws Exception
    {
        if(printlnStatement.getTokens().size() != 0)
            System.out.println(new FullRPN(printlnStatement.getTokens(),this).getResult().getTokenValue());
        else
            System.out.println('\n');
    }

    public VariablesTable getVariablesTable() { return variablesTable; }
}
