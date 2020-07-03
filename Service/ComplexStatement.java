package com.company.Service;

import com.company.AbstractSyntaxTree.BaseStatements.BaseStatement;

public class ComplexStatement
{
    private final BaseStatement statement;
    private final boolean availableBlock;
    private final int tabOrder;

    public ComplexStatement(BaseStatement statement, int tabOrder,  boolean availableBlock)
    {
        this.availableBlock = availableBlock;
        this.statement = statement;
        this.tabOrder = tabOrder;
    }

    public BaseStatement getStatement() { return statement; }
    public boolean isAvailableBlock() { return availableBlock; }
    public int getTabOrder() { return tabOrder; }
}
