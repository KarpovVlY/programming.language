package com.company.Interpreter.Variable;

import com.company.Service.TokenType;

public class Variable
{
    private String id;
    private Object value;
    private TokenType variableType;


    public Variable(String id, Object value, TokenType variableType)
    {
        this.id = id;
        this.value = value;
        this.variableType = variableType;
    }

    public String getId() { return id; }
    public Object getValue() { return value; }
    public TokenType getVariableType() { return variableType; }

    public void changeVariable(Object value, TokenType variableType)
    {
        this.value = value;
        this.variableType = variableType;
    }
}
