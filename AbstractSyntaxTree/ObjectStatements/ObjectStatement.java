package com.company.AbstractSyntaxTree.ObjectStatements;

import com.company.Service.Token;

public class ObjectStatement
{
    Token objectId;

    public ObjectStatement(Token objectId)
    {
        this.objectId = objectId;
    }

    public Token getObjectId() { return objectId; }
}
