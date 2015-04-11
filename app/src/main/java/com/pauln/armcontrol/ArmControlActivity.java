package com.pauln.armcontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import static com.pauln.armcontrol.RobotCommand.ArmStop;

/**
 * ArmControl activity.
 * Written by Dr Paul Netherwood
 */
public class ArmControlActivity extends ActionBarActivity implements ButtonDispatcherInterface
{
    /**
     * Main socket thread
     */
    private SocketClientThread socketClientThread;

    /**
     * Button dispatcher used to map buttons to arm commands
     */
    private ButtonDispatcher buttonDispatcher;

    /**
     * Handler for for socket thread to communicate to UI
     */
    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Initialise message handler
        handler = new MessageHandler(this);

        // Create the button dispatcher used by the fragments
        buttonDispatcher = new ButtonDispatcher(this);

        // Create the view
        setContentView(R.layout.arm_control);

        // Put Arm icon on activity bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    @Override
    protected void onStart()
    {
        // Called after onCreate() this method starts the socket thread
        super.onStart();
        startSocketClient();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final String stream = sharedPref.getString(ArmControlSettingsActivity.STREAM_KEY, "http://192.168.1.80:8080/stream");

        // Set web view content to be the raspberry pi camera stream
        final WebView webView = (WebView)findViewById(R.id.webView);
        int default_zoom_level=100;
        webView.setInitialScale(default_zoom_level);

        // Get the width and height of the view because its different for different phone or table layouts
        // Pass these values to the URL in teh web view to display th eHTTP stream
        webView.post(new Runnable()
        {
            @Override
            public void run() {
                int width = webView.getWidth();
                int height = webView.getHeight();
                webView.loadUrl(stream + "?width="+width+"&height="+height);
            }
        });

        // Clicking on webview reloads webpage
        webView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((WebView) v).reload();
            }
        });

    }

    @Override
    protected void onStop()
    {
        clearAll();

        super.onStop();
    }

    private void clearAll()
    {
        // Clear the web view
        WebView webView = (WebView)findViewById(R.id.webView);
        webView.loadUrl("about:blank");

        // App has been stopped so close the socket and stop thread
        socketClientThread.stopRunning();
    }

    private void startSocketClient()
    {
        // Load connection settings - this may have just ben changed in the preferences activity
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String host = sharedPref.getString(ArmControlSettingsActivity.HOST_KEY, "192.168.1.80");
        String port = sharedPref.getString(ArmControlSettingsActivity.PORT_KEY, "5000");

        // Start the socket connection thread
        socketClientThread = new SocketClientThread(host, Integer.parseInt(port));
        socketClientThread.start();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        clearAll();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_arm_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            launchSettingsActivity();
            return true;
        }
        else if (id == R.id.action_reconnect)
        {
            reconnect();
        }
        else if (id == R.id.action_stop)
        {
            sendStop();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Menu method to reconnect
     */
    private void reconnect()
    {
        socketClientThread.stopRunning();
        startSocketClient();
    }

    /**
     * Menu method for sending emergency stop command
     */
    private void sendStop()
    {
        socketClientThread.sendCommand(new ButtonCommand(ArmStop));
        Log.i("RobotCommand", "Arm stopped");
    }

    /**
     * Launch the settings menu
     */
    private void launchSettingsActivity()
    {
        Intent intent = new Intent(this, ArmControlSettingsActivity.class);
        startActivity(intent);
    }

    public final static int CONNECTION_ERROR = 0;
    public final static int CONNECTION_LOST = 1;

    /**
     * Message handler class used to process error messages from socket thread
     */
    private static class MessageHandler extends Handler
    {
        private Context context;
        private MessageHandler(Context context)
        {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case CONNECTION_ERROR:
                    message("Can't connect to the arm");
                    break;
                case CONNECTION_LOST:
                    message("Connection lost to the arm");
                    break;
            }
        }
        void message(String message)
        {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public SocketClientThread getSocketClientThread()
    {
        return socketClientThread;
    }


    @Override
    public ButtonDispatcher getButtonDispatcher()
    {
        return buttonDispatcher;
    }
}
