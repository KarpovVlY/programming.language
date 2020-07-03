package com.company.AbstractSyntaxTree.BaseStatements;

import com.company.AbstractSyntaxTree.ConditionStatements.ElseStatement;
import com.company.AbstractSyntaxTree.ConditionStatements.IfStatement;
import com.company.AbstractSyntaxTree.CyclesStatements.WhileStatement;
import com.company.Service.ComplexStatement;

import java.util.ArrayList;

public class BlockStatement extends BaseStatement
{
    protected int tabOrder;
    protected ArrayList<ComplexStatement> complexStatements;
    protected ArrayList<BaseStatement> statements;

    public void addComplexStatement(ComplexStatement statement) { this.complexStatements.add(statement); }
    public void addStatement(BaseStatement statement) { this.statements.add(statement); }

    @Override
    public void process() throws Exception
    {
        statements = new ArrayList<>();

        ComplexStatement complexStatement;

        for(int i = 0; i < complexStatements.size() ; i ++)
        {
            complexStatement = complexStatements.get(i);

            if(complexStatement.getTabOrder() == this.tabOrder + 1)
            {
                if(complexStatement.isAvailableBlock())
                {
                    ++i;
                    ComplexStatement bufferComplexStatement;

                    for( ; i < complexStatements.size() ; i ++)
                    {
                        bufferComplexStatement = complexStatements.get(i);

                        if(bufferComplexStatement.getTabOrder() != complexStatement.getTabOrder())
                        {
                            if(complexStatement.getStatement() instanceof WhileStatement || complexStatement.getStatement() instanceof IfStatement)
                                ((BlockStatement) complexStatement.getStatement()).addComplexStatement(bufferComplexStatement);
                            else
                                throw new Exception("Непредвиденная ошибка");
                        }
                        else
                        {

                            if(bufferComplexStatement.getStatement() instanceof ElseStatement)
                            {
                                if(complexStatement.getStatement() instanceof IfStatement)
                                    complexStatement.getStatement().process();
                                else
                                    throw new Exception("Ошибка : else может идти только после if");
                            }
                            else
                            {
                                --i;
                                break;
                            }
                        }
                    }

                    complexStatement.getStatement().process();
                }

                this.addStatement(complexStatement.getStatement());
            }
            else
                throw new Exception("Ошибка при обработке табуляции");

        }

        this.complexStatements = null;
    }
}
