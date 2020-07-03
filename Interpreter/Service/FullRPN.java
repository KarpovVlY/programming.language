package com.company.Interpreter.Service;

import com.company.AbstractSyntaxTree.FunctionStatements.FunctionCall;
import com.company.Interpreter.Interpreter;
import com.company.Interpreter.Objects.List;
import com.company.Interpreter.Variable.Variable;
import com.company.Interpreter.Variable.VariablesTable;
import com.company.Service.Token;
import com.company.Service.TokenType;

import java.util.ArrayList;
import java.util.Stack;

public class FullRPN
{
    private ArrayList<Object> elements;
    private Interpreter interpreter;

    public FullRPN(ArrayList<Object> elements, Interpreter interpreter)
    {
        this.elements = elements;
        this.interpreter = interpreter;
    }

    public ArrayList<Token> createRPN() throws Exception
    {
        ArrayList<Token> OutputList = new ArrayList<>();
        Stack<Token> stack = new Stack<>();

        Object currentObject;

        for(int i = 0 ; i < elements.size() ; i ++)
        {
            currentObject = elements.get(i);

            if(currentObject instanceof Token)
            {
                Token token = ((Token)currentObject);

                if (token.getTokenType() == TokenType.BOOLEAN || token.getTokenType() == TokenType.ID || token.getTokenType() == TokenType.DIGIT)
                {
                    OutputList.add(token);
                }
                else if (isOperand(token.getTokenType()) || token.getTokenType() == TokenType.LEFT_BRACKET || token.getTokenType() == TokenType.RIGHT_BRACKET)
                {
                    if ((!stack.empty()) && !(token.getTokenType() == TokenType.LEFT_BRACKET))
                    {
                        if (token.getTokenType() == (TokenType.RIGHT_BRACKET))
                        {
                            Token opInStack = stack.pop();

                            while (!opInStack.getTokenType().equals((TokenType.LEFT_BRACKET)))
                            {
                                OutputList.add(opInStack);

                                try { opInStack = stack.pop(); }
                                catch (Exception exception) { throw new Exception("Непредвиденная ошибка в вычислениях"); }

                            }
                        }
                        else
                        {
                            if (priority(token) <= priority(stack.peek()))
                                while (!stack.empty() && priority(token) <= priority(stack.peek()))
                                    OutputList.add(stack.pop());

                            stack.add(token);
                        }
                    }
                    else
                        stack.add(token);

                }
            }
            else if(currentObject instanceof FunctionCall)
            {
                OutputList.add(((List)interpreter.getVariablesTable().getVariable
                        (((FunctionCall) currentObject).getFunctionID().getTokenValue()).getValue()).executeFunction
                        (((FunctionCall) currentObject).getFunctionName().getTokenValue(),
                                ((FunctionCall) currentObject).getFunctionArgs(), true, interpreter));
            }
            else
                throw new Exception("Неизвестное выражение");
        }


        if (!stack.empty())
            OutputList.addAll(stack);


        return OutputList;
    }


    public Token calculateRPN(ArrayList<Token> RPN) throws Exception
    {
        Stack<Token> st_calculate = new Stack<>();

        String operation;

        for (Token token : RPN)
        {
            if (token.getTokenType() == TokenType.DIGIT || token.getTokenType() == TokenType.BOOLEAN)
            {
                st_calculate.push(token);
            }
            else if (isOperand(token.getTokenType()))
            {
                operation = token.getTokenValue();

                try
                {
                    if (operation.compareTo("+") == 0)
                    {
                        int first = Integer.parseInt(st_calculate.pop().getTokenValue());
                        int second = Integer.parseInt(st_calculate.pop().getTokenValue());

                        st_calculate.push(new Token(Integer.toString(second + first), TokenType.DIGIT));
                    }
                    else if (operation.compareTo("-") == 0)
                    {
                        int first = Integer.parseInt(st_calculate.pop().getTokenValue());
                        int second = Integer.parseInt(st_calculate.pop().getTokenValue());

                        st_calculate.push(new Token(Integer.toString(second - first), TokenType.DIGIT));
                    }
                    else if (operation.compareTo("*") == 0)
                    {
                        int first = Integer.parseInt(st_calculate.pop().getTokenValue());
                        int second = Integer.parseInt(st_calculate.pop().getTokenValue());

                        st_calculate.push(new Token(Integer.toString(second * first), TokenType.DIGIT));
                    }
                    else if (operation.compareTo("/") == 0)
                    {
                        int first = Integer.parseInt(st_calculate.pop().getTokenValue());
                        int second = Integer.parseInt(st_calculate.pop().getTokenValue());

                        st_calculate.push(new Token(Integer.toString(second / first), TokenType.DIGIT));
                    }
                    else if (operation.compareTo("%") == 0)
                    {
                        int first = Integer.parseInt(st_calculate.pop().getTokenValue());
                        int second = Integer.parseInt(st_calculate.pop().getTokenValue());

                        st_calculate.push(new Token(Integer.toString(second % first), TokenType.DIGIT));
                    }
                    else if (operation.compareTo(">") == 0)
                    {
                        st_calculate.push(new Token(Boolean.toString(
                                Integer.parseInt(st_calculate.pop().getTokenValue()) <
                                        Integer.parseInt(st_calculate.pop().getTokenValue())), TokenType.BOOLEAN));
                    }
                    else if (operation.compareTo("<") == 0)
                    {
                        st_calculate.push(new Token(Boolean.toString(
                                Integer.parseInt(st_calculate.pop().getTokenValue()) >
                                        Integer.parseInt(st_calculate.pop().getTokenValue())), TokenType.BOOLEAN));
                    }
                    else if (operation.compareTo("<=") == 0)
                    {
                        st_calculate.push(new Token(Boolean.toString(
                                Integer.parseInt(st_calculate.pop().getTokenValue()) >=
                                        Integer.parseInt(st_calculate.pop().getTokenValue())), TokenType.BOOLEAN));
                    }
                    else if (operation.compareTo(">=") == 0)
                    {
                        st_calculate.push(new Token(Boolean.toString(
                                Integer.parseInt(st_calculate.pop().getTokenValue()) <=
                                        Integer.parseInt(st_calculate.pop().getTokenValue())), TokenType.BOOLEAN));
                    }
                    else if (operation.compareTo("==") == 0)
                    {
                        Token rightToken = st_calculate.pop();
                        Token leftToken = st_calculate.pop();

                        if (leftToken.getTokenType() == rightToken.getTokenType())
                        {
                            if (leftToken.getTokenType() == TokenType.DIGIT)
                                st_calculate.push(new Token(Boolean.toString(
                                        Integer.parseInt(leftToken.getTokenValue()) ==
                                                Integer.parseInt(rightToken.getTokenValue())), TokenType.BOOLEAN));
                            else
                                st_calculate.push(new Token(Boolean.toString(
                                        Boolean.parseBoolean(leftToken.getTokenValue()) ==
                                                Boolean.parseBoolean(rightToken.getTokenValue())), TokenType.BOOLEAN));
                        }
                        else
                            throw new Exception("Ошибка в вычислениях, несовместимость типов");

                    }
                    else if (operation.compareTo("!=") == 0)
                    {

                        Token rightToken = st_calculate.pop();
                        Token leftToken = st_calculate.pop();

                        if (leftToken.getTokenType() == rightToken.getTokenType())
                        {
                            if (leftToken.getTokenType() == TokenType.DIGIT)
                                st_calculate.push(new Token(Boolean.toString(
                                        Integer.parseInt(leftToken.getTokenValue()) !=
                                                Integer.parseInt(rightToken.getTokenValue())), TokenType.BOOLEAN));
                            else
                                st_calculate.push(new Token(Boolean.toString(
                                        Boolean.parseBoolean(leftToken.getTokenValue()) !=
                                                Boolean.parseBoolean(rightToken.getTokenValue())), TokenType.BOOLEAN));
                        }
                        else
                            throw new Exception("Ошибка в вычислениях, несовместимость типов");

                    }
                }
                catch (Exception ignored) { throw new Exception("Непредвиденная ошибка в вычислениях"); }
            }
            else if (token.getTokenType() == TokenType.ID)
            {
                st_calculate.push(getVariableValue(token));
            }
            else
                throw new Exception("Некорректно введено выражение");

        }

        return st_calculate.pop();
    }


    public int priority(Token token)
    {
        if (token.getTokenType() == TokenType.MULTIPLY) return 3;
        else if (token.getTokenType() == TokenType.DIVINE) return 3;
        else if (token.getTokenType() == TokenType.PERCENT) return 3;
        else if (token.getTokenType() == TokenType.MINUS) return 2;
        else if (token.getTokenType() == TokenType.PLUS) return 2;
        else return 0;
    }

    private boolean isOperand(TokenType tokenType)
    {
        return tokenType == TokenType.MULTIPLY || tokenType == TokenType.DIVINE
                || tokenType == TokenType.PERCENT || tokenType == TokenType.PLUS || tokenType == TokenType.MINUS
                || tokenType == TokenType.POSITIVE_EQUAL || tokenType == TokenType.NEGATIVE_EQUAL
                || tokenType == TokenType.MORE || tokenType == TokenType.MORE_EQUAL
                || tokenType == TokenType.LESS || tokenType == TokenType.LESS_EQUAL;
    }



    private Token getVariableValue(Token token) throws Exception
    {
        Variable variable = interpreter.getVariablesTable().getVariable(token.getTokenValue());
        if(variable != null)
        {
            if(variable.getValue() instanceof String)
            {
                return new Token((String) variable.getValue(), variable.getVariableType());
            }
            else
                return null;
        }
        else
            throw new Exception("Переменной " + token.getTokenValue() + " не существует");

    }

    public Token getResult() throws Exception { return calculateRPN(createRPN()); }
}