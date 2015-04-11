package com.pauln.armcontrol;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import java.util.HashMap;
import java.util.Map;

/**
 * Keeps a list of buttons and dispatches command to them on up/down events
 * Created by Pauln on 24/02/2015.
 */
public class ButtonDispatcher
{
    /**
     * Map of buttons to commands
     */
    private Map<Integer, ButtonCommand> buttons = new HashMap<>();

    /**
     * Main thread
     */
    private ArmControlActivity armControlActivity;

    public ButtonDispatcher(ArmControlActivity armControlActivity)
    {
        this.armControlActivity = armControlActivity;
    }

    /**
     * Adds button top list of buttons and adds its listener
     * @param button The button
     */
    public void setupButton(ImageButton button, RobotCommand robotCommand)
    {
        button.setOnTouchListener(btnTouch);
        buttons.put(button.getId(), new ButtonCommand(robotCommand));
    }

    /**
     * Listener used by buttons
     */
    private View.OnTouchListener btnTouch = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN || action ==  MotionEvent.ACTION_POINTER_DOWN)    // Since multi touch is enabled listen for multi touch events as well
                buttonDownCommand(v.getId());
            else if (action == MotionEvent.ACTION_UP || action ==  MotionEvent.ACTION_POINTER_UP)
                buttonUpCommand(v.getId());
            return false;   //  the listener has NOT consumed the event, pass it on
        }
    };

    /**
     * Finger has been lifted off so send command to stop movement
     * @param id Button ID
     */
    private void buttonUpCommand(int id)
    {
        ButtonCommand buttonCommand = buttons.get(id);
        if(buttonCommand.getRobotCommand().getButtonType() != ButtonType.Toggle)
        {
            buttonCommand.setCommandState(CommandState.Stopped);
            armControlActivity.getSocketClientThread().sendCommand(buttonCommand);
        }
    }

    /**
     * Button has been pressed and held down so send command to turn on movement
     * @param id Button ID
     */
    private void buttonDownCommand(int id)
    {
        ButtonCommand buttonCommand = buttons.get(id);
        buttonCommand.setCommandState(CommandState.Moving);
        armControlActivity.getSocketClientThread().sendCommand(buttonCommand);
    }

    /**
     * Method used to send button command that are not touch buttons e.g. the speed seekbar
     * @param buttonCommand
     */
    public void buttonCommand(ButtonCommand buttonCommand)
    {
        armControlActivity.getSocketClientThread().sendCommand(buttonCommand);
    }
}
