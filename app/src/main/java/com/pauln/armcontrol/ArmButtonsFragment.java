package com.pauln.armcontrol;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 */
public class ArmButtonsFragment extends Fragment
{

    private ButtonDispatcher buttonDispatcher;

    public static ArmButtonsFragment newInstance()
    {
        return new ArmButtonsFragment();
    }

    public ArmButtonsFragment()
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
        View view = inflater.inflate(R.layout.fragment_arm_buttons, container, false);

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
        buttonDispatcher.setupButton((ImageButton) view.findViewById(R.id.baseCW), RobotCommand.BaseCW);
        buttonDispatcher.setupButton((ImageButton) view.findViewById(R.id.baseACW), RobotCommand.BaseACW);
        buttonDispatcher.setupButton((ImageButton) view.findViewById(R.id.shoulderLeft), RobotCommand.ShoulderLeft);
        buttonDispatcher.setupButton((ImageButton) view.findViewById(R.id.shoulderRight), RobotCommand.ShoulderRight);
        buttonDispatcher.setupButton((ImageButton) view.findViewById(R.id.elbowUp), RobotCommand.ElbowUp);
        buttonDispatcher.setupButton((ImageButton) view.findViewById(R.id.elbowDown), RobotCommand.ElbowDown);
        buttonDispatcher.setupButton((ImageButton) view.findViewById(R.id.wristUp), RobotCommand.WristUp);
        buttonDispatcher.setupButton((ImageButton) view.findViewById(R.id.wristDown), RobotCommand.WristDown);
        buttonDispatcher.setupButton((ImageButton) view.findViewById(R.id.gripperOpen), RobotCommand.GripperOpen);
        buttonDispatcher.setupButton((ImageButton) view.findViewById(R.id.gripperClose), RobotCommand.GripperClose);
        buttonDispatcher.setupButton((ImageButton) view.findViewById(R.id.lightOn), RobotCommand.LightOn);
        buttonDispatcher.setupButton((ImageButton) view.findViewById(R.id.lightOff), RobotCommand.LightOff);
    }
}
