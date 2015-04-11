package com.pauln.armcontrol;

/**
 * Maps server robot commands to enums
 */
public enum RobotCommand
{
    BaseCW("BC", "Base clockwise"),
    BaseACW("BA", "Base anti-clockwise"),
    ShoulderLeft("SU", "Shoulder left"),
    ShoulderRight("SD", "Shoulder right"),
    ElbowUp("EU" , "Elbow up"),
    ElbowDown("ED", "Elbow down"),
    WristUp("WU", "Wrist up"),
    WristDown("WD", "Wrist down"),
    GripperOpen("GO", "Gripper open"),
    GripperClose("GC", "Gripper close"),
    LightOn("LO", "Light on", ButtonType.Toggle),
    LightOff("LF", "Light off", ButtonType.Toggle),
    Forward("FF", "Forward"),
    Back("BB", "Back"),
    Left("LL", "Left"),
    Right("RR", "Right"),
    Speed("SS", "Speed", ButtonType.Value),
    ArmStop(".", "Arm stop", ButtonType.Toggle);

    private String commandCode;
    private String description;
    private ButtonType buttonType;

    RobotCommand(String commandCode, String description)
    {
        this.commandCode = commandCode;
        this.description = description;
        this.buttonType = ButtonType.Touch;
    }

    RobotCommand(String commandCode, String description, ButtonType buttonType)
    {
        this.commandCode = commandCode;
        this.description = description;
        this.buttonType = buttonType;
    }

    public String getCommandCode()
    {
        return commandCode;
    }

    public String getDescription()
    {
        return description;
    }

    public ButtonType getButtonType()
    {
        return buttonType;
    }
}
