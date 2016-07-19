package com.hardwareanddevices.paintbrush;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class MainScreen extends Activity implements SensorEventListener {

    // Sensor manager which manages the sensors
    private SensorManager mSensorManager;
    // The light sensor
    private Sensor mLightSensor;
    // The accelerometer
    private Sensor mAccelerometer;
    // The proximity Sensor
    private Sensor mProxSensor;

    // Used in the shaking code
    long mPreviousTime = 0, mDifference;

    long mPreviousDrawTime = 0, mDrawDifference;
    float mCurrentDrawX, mCurrentDrawY, mCurrentDrawZ;
    float mPreviousDrawX = 0, mPreviousDrawY = 0;

    float mCurrentX, mCurrentY, mCurrentZ;
    float mPreviousX = 0, mPreviousY = 0, mPreviousZ = 0;

    // Contains what the current brush type is
    int currentBrushType;
    // Vibrator of the phone
    Vibrator vibe;

    Toast toast;

    // Contains the brush thickness types
    int thickness;

    // The radio buttons
    RadioButton rbtnOne;
    RadioButton rbtnTwo;
    RadioButton rbtnThree;
    RadioButton rbtnFour;
    RadioButton rbtnFive;

    ImageButton paintButton;
    ImageButton settingsButton;

    ImageView paintImage;

    //Boolean used for initiating a socket
    //boolean initiate = true;
    // This is the AsyncTask which runs in parallel to the main thread
    backgroundTask bgThread;

    // LinearLayout which contains the frame to change the colour
    private LinearLayout colourSelector;
    // SeekBars to adjust colour values
    private SeekBar redSeek, blueSeek, greenSeek;
    // Colour values
    int red, blue, green;

    // Used to display the colour the user is using
    private ImageButton blob;

    // This is used to connect to the server
    Socket recSocket;

    boolean paint;

    /**
     * When the application is first started
     * Used to initiate
     * @param savedInstanceState used on created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        paintImage = (ImageView) findViewById(R.id.paintBrush);

        // Frame which allows a user to select a colour
        colourSelector = (LinearLayout) findViewById(R.id.colour_selector);

        // TextView which displays what the current colour is
        blob = (ImageButton) findViewById(R.id.blob);
        blob.bringToFront();

        // Radio buttons for brush thickness
        rbtnOne = (RadioButton) findViewById(R.id.rbtn_one);
        rbtnTwo = (RadioButton) findViewById(R.id.rbtn_two);
        rbtnThree = (RadioButton) findViewById(R.id.rbtn_three);
        rbtnFour = (RadioButton) findViewById(R.id.rbtn_four);
        rbtnFive = (RadioButton) findViewById(R.id.rbtn_five);

        // SeekBars to change colour values
        redSeek = (SeekBar) findViewById(R.id.redSeekBar);
        greenSeek = (SeekBar) findViewById(R.id.greenSeekBar);
        blueSeek = (SeekBar) findViewById(R.id.blueSeekBar);

        // The listeners that listen when the bars change value
        redSeek.setOnSeekBarChangeListener(new seekBarListen());
        greenSeek.setOnSeekBarChangeListener(new seekBarListen());
        blueSeek.setOnSeekBarChangeListener(new seekBarListen());

        // Sensor manager to allow us to use the sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Initiates light sensor
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }
        // Initiates Accelerometer
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        // Initiates proximity sensor
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
            mProxSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }

        // Initiates the brush type variables (Starts at medium
        currentBrushType = 0;
        // Initiates vibrator
        vibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Thickness of brush
        thickness = 5;

        settingsButton = (ImageButton) findViewById(R.id.colour);
        paintButton = (ImageButton) findViewById(R.id.paint);
        paintButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        paint = true;
                        paintButton.setImageResource(R.drawable.button_pressed);
                        paintImage.setImageResource(R.drawable.painting_brush);
                        settingsButton.setEnabled(false);
                        blob.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        paint = false;
                        paintButton.setImageResource(R.drawable.button_unpressed);
                        paintImage.setImageResource(R.drawable.templatebrush);
                        settingsButton.setEnabled(true);
                        blob.setEnabled(true);
                }

                return false;
            }
        });

        toast = new Toast(this);

        // Starts the thread that starts the socket communication
        bgThread = new backgroundTask();
        bgThread.execute();
    }

    // do Something here if sensor accuracy changes
    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {}

    // When any of the sensor value changes
    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor is used to change the opacity of the paintbrush, closer to 0 is full
        // whereas 100 is transparent
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            // Light sensor value
            float lightValue = event.values[0];
            // Gets the maximum range of the light sensor of the device
            float maxValue = mLightSensor.getMaximumRange();
            // Normalises the value to be between 0 and 100
            int normalisedValue = Math.round(((lightValue * 255) / maxValue));
            if (!paint) {
                bgThread.postData("ALPH:" +
                        Integer.toString(red) + "," + Integer.toString(green) + ","
                        + Integer.toString(blue) + "," + Integer.toString(normalisedValue));
            }
        }

        // If the sensor is a proximity sensor then we change the brush type
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            // Gets whether if the proximity sensor has been activated or not
            float proxValue = event.values[0];

            // If it has then we change the brush
            if (proxValue != event.sensor.getMaximumRange() && !paint) {
                changeBrush();
            }
        }

        // If the accelerometer is used
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER ) {
            // Code to detect a shake and to draw
            // We get the current system time
            long currentSystemTime = System.currentTimeMillis();

            // Used for drawing
            int DRAW_TIME = 50;
            if ((currentSystemTime - mPreviousDrawTime) > DRAW_TIME) {
                // Works out the time differences
                mDrawDifference = currentSystemTime - mPreviousDrawTime;
                mPreviousDrawTime = currentSystemTime;
                // Gets the current x,y,z values of the accelerometer
                mCurrentDrawX = event.values[0] * 10;
                mCurrentDrawY = event.values[1] * 10;
                mCurrentDrawZ = event.values[2] * 10;
                // If true then we are painting, false otherwise
                if (paint) {
                    // if the paint brush isn't isn't centered then we draw
                    if (mCurrentDrawX > 10 || mCurrentDrawX < -10) {
                        // If the value has changed to it's previous position
                        if (mCurrentDrawX > mPreviousDrawX) {
                                bgThread.postData("POS:LEFT");
                        } else {
                            // Draw right
                            bgThread.postData("POS:RIGHT");
                        }
                    }

                    // If we draw on the y axis
                    if (mCurrentDrawY > 40 || mCurrentDrawY < 10) {
                        if (mCurrentDrawY > mPreviousDrawY) {
//                          // Draw up
                            bgThread.postData("POS:UP");
                        } else {
                            // Draw down
                            bgThread.postData("POS:DOWN");
                        }
                    }
                    // Gets the current values and places them in the previous
                    mPreviousX = mCurrentX;
                    mPreviousY = mCurrentY;
                    mPreviousZ = mCurrentZ;
                }
            }

            // Used if the phone is shook
            int SHAKE_TIME = 200;
            // if the phone has been shaken
            if ((currentSystemTime - mPreviousTime) > SHAKE_TIME) {
                // Works out the time differences
                mDifference = currentSystemTime - mPreviousTime;
                mPreviousTime = currentSystemTime;
                // Gets the current x,y,z values of the accelerometer
                mCurrentX = event.values[0];
                mCurrentY = event.values[1];
                mCurrentZ = event.values[2];
                // Work out the shake from the current values and the previous
                float shake = Math.abs((mCurrentX + mCurrentY + mCurrentZ)
                        - (mPreviousX - mPreviousY - mPreviousZ)) / mDifference * 10000;
               // Log.d("SHAKE", Float.toString(shake));
                int SHAKE_VALUE = 1500;
                if (shake > SHAKE_VALUE && !paint) {
                    // If the device had been shaking over a period of time then we run the shake
                    shakeItOff();
                }
                // Gets the current values and places them in the previous
                mPreviousX = mCurrentX;
                mPreviousY = mCurrentY;
                mPreviousZ = mCurrentZ;

            }
        }
    }

    // If the user presses the select colour button then we make the colour selector visible
    public void selectColour(View view) {
        colourSelector.setVisibility(View.VISIBLE);
        colourSelector.bringToFront();
        paintButton.setEnabled(false);
        switch (thickness) {
            case 1:
                rbtnTwo.setChecked(false);
                rbtnThree.setChecked(false);
                rbtnFour.setChecked(false);
                rbtnFive.setChecked(false);
                rbtnOne.setChecked(true);
                break;
            case 2:
                rbtnOne.setChecked(false);
                rbtnThree.setChecked(false);
                rbtnFour.setChecked(false);
                rbtnFive.setChecked(false);
                rbtnTwo.setChecked(true);
                break;
            case 3:
                rbtnOne.setChecked(false);
                rbtnTwo.setChecked(false);
                rbtnFour.setChecked(false);
                rbtnFive.setChecked(false);
                rbtnThree.setChecked(true);
                break;
            case 4:
                rbtnOne.setChecked(false);
                rbtnTwo.setChecked(false);
                rbtnThree.setChecked(false);
                rbtnFive.setChecked(false);
                rbtnFour.setChecked(true);
                break;
            case 5:
                rbtnOne.setChecked(false);
                rbtnTwo.setChecked(false);
                rbtnThree.setChecked(false);
                rbtnFour.setChecked(false);
                rbtnFive.setChecked(true);
                break;
        }
    }

    // If the user presses the Ok button in the colour selector
    public void confirm(View view) {
        // Makes the colour selector invisible
        colourSelector.setVisibility(View.INVISIBLE);
        // Sets the new colour values
        red = redSeek.getProgress();
        blue = blueSeek.getProgress();
        green = greenSeek.getProgress();
        // Send the colour values
        bgThread.postData("COL:" +
                Integer.toString(red) + "," + Integer.toString(green) +","
                + Integer.toString(blue) + ",100");
        bgThread.postData("THICK:" + Integer.toString(thickness));
        paintButton.setEnabled(true);
    }

    public void centerImage(View view) {
        bgThread.postData("CENT:0");
    }

    public void onBrushThickClick(View view) {

        boolean check = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbtn_one:
                if(check) {
                    thickness = 1;
                }
                break;
            case R.id.rbtn_two:
                if(check) {
                    thickness = 2;
                }
                break;

            case R.id.rbtn_three:
                if (check) {
                    thickness = 3;
                }
                break;

            case R.id.rbtn_four:
                if (check) {
                    thickness = 4;
                }
                break;

            case R.id.rbtn_five:
                if (check) {
                    thickness = 5;
                }
                break;
        }

    }

    /**
     * When the app is resumed
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Reignites the sensors
        mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mProxSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * When the app is paused we unregister the sensor manager
     */
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    // Changes the brush type
    public void changeBrush() {
        // If the current brush type is 5(Max size), we set it back to 0(smallest)
        if(currentBrushType == 10) {
            currentBrushType = 1;
           bgThread.postData("BRTY:1");
        } else {
            // Increases brush size
            currentBrushType++;
            bgThread.postData("BRTY:" + Integer.toString(currentBrushType));
        }
    }

    // If the phone is shaken
    public void shakeItOff() {
        // TayTay: Ooh ooh oh oooooo
        Toast.makeText(this, "UNDO", Toast.LENGTH_LONG).show();
        bgThread.postData("UNDO:0");
    }

    // Listener which listens for when the seek bars change
    private class seekBarListen implements SeekBar.OnSeekBarChangeListener {
        // When the seek bars are changed
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // Switch statement which contains each seek bar
            switch (seekBar.getId()) {
                // If the red seek bar is changed
                case R.id.redSeekBar:
                    blob.setBackgroundColor(Color.rgb(
                            redSeek.getProgress(), greenSeek.getProgress(),blueSeek.getProgress()));
                    break;
                // If the green seek bar is changed
                case R.id.greenSeekBar:
                    blob.setBackgroundColor(Color.rgb(
                            redSeek.getProgress(), greenSeek.getProgress(),blueSeek.getProgress()));
                    break;
                // If the blue seek bar is changed
                case R.id.blueSeekBar:
                    blob.setBackgroundColor(Color.rgb(
                            redSeek.getProgress(), greenSeek.getProgress(),blueSeek.getProgress()));
                    break;
            }
        }

        // Uneeded methods
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }

    /*
 * This inner class will, once executed, run in parallel to the main thread. We do this as no
 * networking can be done on the main thread.
 */
    private class backgroundTask extends AsyncTask<String, Integer, Double> {
        /**
         * This method is run in the background of the main method, effectively a new thread
         */
        //@Override
      //  protected Double doInBackground(Socket... params) {}

        ObjectOutputStream output;

        @Override
        protected Double doInBackground(String... params) {

            try {
                // Points to the localhost so that we can receive the information on port 8888
                recSocket = new Socket("172.20.10.5", 8888);

                // Read from the "server"
                output = new ObjectOutputStream(recSocket.getOutputStream());

                // Parse the received data and add contacts to the ArrayList
                //while (true) {
                  //  if (output != null) {
                    //    output.writeUTF(("BrushSize:22"));
                      //  output.flush();
                    //}
               //}
            } catch (IOException e) {
                Log.d("SOCKET ERROR", "Something went wrong: " + e.toString());
            }
            return null;
        }

        protected void postData(String data) {
            try {

                if(output != null) {
                    output.writeUTF(data);
                    output.flush();
                }
            } catch(IOException e) {
                Log.d("WRITING ERROR", "Something went wrong: " + e.toString());
            }
        }

        // Method not needed so it is not implemented
        protected void onPostExecute(Double result) {}

    }


}