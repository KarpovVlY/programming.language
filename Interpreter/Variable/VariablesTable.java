package com.company.Interpreter.Variable;

import com.company.Interpreter.Interpreter;
import com.company.Service.TokenType;

import java.util.ArrayList;

public class VariablesTable
{

    private ArrayList<Variable> variables;
    private final Interpreter parent;

    public VariablesTable(Interpreter parent)
    {
        variables = new ArrayList<>();
        this.parent = parent;
    }


    public void addVariable(String id, Object value, TokenType tokenType)
    {
        variables.add(new Variable(id,value,tokenType));
    }

    public Variable getVariable(String id)
    {
        Variable variable;
        Interpreter currentInterpreter = parent;

        while(currentInterpreter != null)
        {
            for(int i  = 0 ; i< currentInterpreter.getVariablesTable().variables.size() ; i ++)
            {
                variable = currentInterpreter.getVariablesTable().variables.get(i);

                if(variable.getId().compareTo(id) == 0)
                    return variable;
            }
            currentInterpreter = currentInterpreter.parent;
        }

        return null;
    }

    public void changeVariable(String id, Object value, TokenType tokenType)
    {
        Variable variable;
        Interpreter currentInterpreter = parent;

        while(currentInterpreter != null)
        {
            for(int i  = 0 ; i< currentInterpreter.getVariablesTable().variables.size() ; i ++)
            {
                variable = currentInterpreter.getVariablesTable().variables.get(i);

                if(variable.getId().compareTo(id) == 0)
                {
                    variable.changeVariable(value, tokenType);
                    return;
                }
            }
            currentInterpreter = currentInterpreter.parent;
        }
    }

    public boolean checkVariable(String id)
    {
        Variable variable;
        Interpreter currentInterpreter = parent;

        while(currentInterpreter != null)
        {
            for(int i  = 0 ; i< currentInterpreter.getVariablesTable().variables.size() ; i ++)
            {
                variable = currentInterpreter.getVariablesTable().variables.get(i);

                if(variable.getId().compareTo(id) == 0)
                    return true;
            }
            currentInterpreter = currentInterpreter.parent;
        }

        return false;
    }
}
