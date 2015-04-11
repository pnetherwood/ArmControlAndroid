package com.pauln.armcontrol;

import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Pauln on 12/02/2015.
 */
public class SocketClientThread extends Thread
{
    private String host;
    private int port;

    // Lock object used for thread synchronisation
    private final Object lock = new Object();
    private boolean running = true;

    // Current command to send to arm
    private ButtonCommand buttonCommand;

    // The socket to the arm
    private Socket socket = null;

    public SocketClientThread(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    private void openSocket() throws IOException
    {
        try
        {
            // Try and connect to socket server
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 1000);    // 1000ms timeout
            Log.i("RobotCommand", "Connected to " + host + " port " + port);
        }
        catch (IOException e)
        {
            Log.e("RobotCommand", "Socket open failed: " + e.getMessage());
            // Send error message to UI
            Message msg = ArmControlActivity.handler.obtainMessage(ArmControlActivity.CONNECTION_ERROR, e.getMessage());
            ArmControlActivity.handler.sendMessage(msg);
            throw e;
        }
    }

    @Override
    public void run()
    {
        try
        {
            openSocket();
        }
        catch (IOException e)
        {
            return;
        }

        try
        {
            while(running)
            {
                synchronized (lock)
                {
                    // Wait to be released to perform action
                    lock.wait();
                    
                    // Do arm command
                    robotCommand();
                }
            }
        }
        catch (InterruptedException e)
        {
            running = false;
        }
    }

    private void robotCommand()
    {
        if(!running)
            return;

        // If socket connection has been lost then try to reconnect
        if(socket == null || !socket.isConnected())
        {
            Log.i("RobotCommand", "Socket lost, retrying to connect");
            try
            {
                openSocket();
            }
            catch (IOException e)
            {
                running = false;
                return;
            }
        }
        
        // Send button command to socket
        try
        {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(buttonCommand.command());
            writer.flush();
         }
        catch (IOException e)
        {
            Log.e("RobotCommand", "Socket write failed: " + e.getMessage());
            // Send error message to UI
            Message msg = ArmControlActivity.handler.obtainMessage(ArmControlActivity.CONNECTION_LOST, e.getMessage());
            ArmControlActivity.handler.sendMessage(msg);
            socket = null;
        }

        // Reset button command
        buttonCommand = null;
    }

    public synchronized void sendCommand(ButtonCommand buttonCommand)
    {
        synchronized (lock)
        {
            this.buttonCommand = buttonCommand;
            lock.notify();  // Release thread to send command
        }
    }
    
    public void stopRunning()
    {
        synchronized (lock)
        {
            running = false;
            if (socket != null)
            {
                try
                {
                    socket.close();
                } catch (IOException e)
                {
                    // Just ignore
                }
            }
            lock.notify();
        }
    }
}
