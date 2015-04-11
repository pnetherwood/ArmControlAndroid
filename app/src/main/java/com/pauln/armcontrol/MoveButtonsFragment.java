package com.pauln.armcontrol;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;


/**
 *
 */
public class MoveButtonsFragment extends Fragment implements SeekBar.OnSeekBarChangeListener
{
    private ButtonDispatcher buttonDispatcher;

    private SeekBar speedBar;

    public static MoveButtonsFragment newInstance()
    {
        return new MoveButtonsFragment();
    }

    public MoveButtonsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_move_buttons, container, false);

        // Setup the listeners and map an arm command for each button
        initialiseButtons(view);

        return view;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            // Get the button dispatcher held by the main activity
            ButtonDispatcherInterface buttonDispatcherInterface = (ButtonDispatcherInterface) activity;
            buttonDispatcher = buttonDispatcherInterface.getButtonDispatcher();
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " must implement ButtonDispatcherInterface");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        buttonDispatcher = null;
    }

    /**
     * Setup listeners and button commands for all buttons in this fragment
     */
    private void initialiseButtons(View view)
    {
        buttonDispatcher.setupButton((ImageButton) view.findViewById(R.id.moveForwardButton), RobotCommand.Forward);
        buttonDispatcher.setupButton((ImageButton) view.findViewById(R.id.moveLeftButton), RobotCommand.Left);
        buttonDispatcher.setupButton((ImageButton) view.findViewById(R.id.moveRightButton), RobotCommand.Right);
        buttonDispatcher.setupButton((ImageButton) view.findViewById(R.id.moveBackButton), RobotCommand.Back);

        // Initialise the speed setting bar
        speedBar = (SeekBar)view.findViewById(R.id.speedBar);
        speedBar.setOnSeekBarChangeListener(this); // set seekbar listener.
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        // Send the new speed to the robot
        buttonDispatcher.buttonCommand(new ButtonCommand(RobotCommand.Speed, progress));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
    }
}
