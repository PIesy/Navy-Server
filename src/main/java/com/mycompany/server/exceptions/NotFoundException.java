package com.mycompany.server.exceptions;

public class NotFoundException extends Exception
{

    public NotFoundException()
    {
    }

    public NotFoundException(String string)
    {
        super(string);
    }

    private static final long serialVersionUID = 1473684668098975313L;
}
