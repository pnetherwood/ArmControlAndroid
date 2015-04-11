package com.pauln.armcontrol;

/**
 * Created by Pauln on 10/02/2015.
 */
public enum CommandState
{
    Moving("+"), Stopped("-");

    final private String command;
    CommandState(String command)
    {
        this.command = command;
    }
    public String getCommand()
    {
        return command;
    }


}
