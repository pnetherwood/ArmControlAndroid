package com.pauln.armcontrol;

/**
 * Holds arm command for the button plus its state
 */
public class ButtonCommand
{
    private final RobotCommand robotCommand;
    private CommandState commandState;

    private final int value;

    ButtonCommand(RobotCommand robotCommand)
    {
        this.robotCommand = robotCommand;
        value = 0;
    }

    ButtonCommand(RobotCommand robotCommand, int value)
    {
        this.robotCommand = robotCommand;
        this.value = value;
    }

    public void setCommandState(CommandState commandState)
    {
        this.commandState = commandState;
    }

    public RobotCommand getRobotCommand()
    {
        return robotCommand;
    }

    /**
     * Build a single string to be sent as a command which combines code and state/value
     * @return The command
     */
    public String command()
    {
        String command = robotCommand.getCommandCode();
        switch(robotCommand.getButtonType())
        {
            case Touch:
                command += commandState.getCommand();
                break;
            case Value:
                command += Integer.toString(value);
                break;
            default:
                break;
        }
        return command;
    }
}
