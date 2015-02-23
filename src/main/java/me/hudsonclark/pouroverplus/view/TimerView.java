package me.hudsonclark.pouroverplus.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

/**
 * Created by hudson49423 on 2/19/15.
 */
public class TimerView extends View {

    // Whether the animation is able to run.
    public static boolean stop;

    private Handler mHandler;
    private Runnable runnable;

    // Various paint.
    private Paint fillPaint;
    private Paint strokePaint;
    private Paint textPaint;
    private Paint timerPaint;

    // For the actual timer.
    private long startTime;
    private long currentTime;

    // For the text in the animations these variables are reused.
    private String line1;
    private String line2;
    private String line3;
    Rect bounds1;
    Rect bounds2;
    Rect bounds3;

    // Various times and amounts.
    private double cups;
    private int ml;
    private int grams;
    private int bloomTime;
    private int firstPourAmount;
    private int secondPour;
    private int endTime; // The end time in seconds.

    private int minutes;
    private int seconds;
    private int remainingMinutes;
    private int remainingSeconds;
    private String timeString;
    private String remainingTimeString;
    private int re;

    public TimerView(Context context) {
        super(context);
        init();
        stop = true;
    }

    @Override
    public void onDraw(Canvas canvas) {

        // The center of the canvas.
        float centerY = canvas.getHeight() / 2;
        float centerX = canvas.getWidth() / 2;

        float height = canvas.getHeight();
        float width = canvas.getWidth();

        // Display this if the animation is not running.
        if (stop) {

        }

        // Display this if the animation is currently running.
        else {

            // Draw the instructions.
            textPaint.getTextBounds(line1, 0, line1.length(), bounds1);
            canvas.drawText(line1, (centerX - (bounds1.width() / 2)), (float) (height * .05), textPaint);

            textPaint.getTextBounds(line2, 0, line2.length(), bounds2);
            canvas.drawText(line2, (centerX - (bounds2.width() / 2)), (float) (height * .1), textPaint);

            textPaint.getTextBounds(line3, 0, line3.length(), bounds3);
            canvas.drawText(line3, (centerX - (bounds3.width() / 2)), (float) (height * .15), textPaint);

            // Draw the current Time
            canvas.drawText("Current", (float) (width * .05), ((float) (height * .8)), timerPaint);
            canvas.drawText(timeString, (float) (width * .1), ((float) (height * .8)) + 100, timerPaint);

            // Draw the remaining time.
            canvas.drawText("Remaining", (float) (width * .6), ((float) (height * .8)), timerPaint);
            canvas.drawText(remainingTimeString, (float) (width * .65), ((float) (height * .8)) + 100, timerPaint);
        }

    }

    private void init() {
        // Make sure that the onDraw method will get called.
        super.setWillNotDraw(false);

        // Create the paint.
        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        fillPaint.setAntiAlias(true);
        fillPaint.setColor(Color.BLUE);

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(Color.BLUE);

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(70);

        timerPaint = new Paint();
        timerPaint.setStyle(Paint.Style.STROKE);
        timerPaint.setAntiAlias(true);
        timerPaint.setColor(Color.BLUE);
        timerPaint.setTextSize(100);

        // Create the strings
        line1 = "";
        line2 = "";
        line3 = "";
        bounds1 = new Rect();
        bounds2 = new Rect();
        bounds3 = new Rect();

        // Calculate all of the times for the pourover.


        // Call the onDraw method.
        this.invalidate();
    }

    public void animate(double cups, int grams) {
        stop = false;

        calculateTimesandAmounts(cups, grams);

        startTime = 0;
        mHandler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                Log.v("From TimerView", "Runnable going.");

                // See if we should stop the animation.
                if (stop)
                    return;

                // Set the starttime if we need to.
                // Maybe move this outside of the main running loop since
                // it only ever needs to be executed once.
                if (startTime == 0)
                    startTime = System.currentTimeMillis();


                currentTime = System.currentTimeMillis() - startTime;

                // Get the seconds and minutes -----------------------------------------------------
                seconds = Math.round(currentTime / 1000) % 60;
                minutes = Math.round(currentTime / 1000) / 60;
                re = endTime - Math.round(currentTime / 1000);
                remainingMinutes = re / 60;
                remainingSeconds = re % 60;

                if (seconds < 10)
                    timeString = "" + minutes + ":0" + seconds;
                else
                    timeString = "" + minutes + ":" + seconds;
                if (remainingSeconds < 10)
                    remainingTimeString = "" + remainingMinutes + ":0" + remainingSeconds;
                else
                    remainingTimeString = "" + remainingMinutes + ":" + remainingSeconds;

                // From 0 to the bloom time.  ------------------------------------------------------
                if (currentTime < bloomTime * 1000) {
                    line1 = "Pour just enough water to completely";
                    line2 = "cover the coffee and let the coffee bloom";
                    line3 = "for 30 seconds";
                }

                // From the end of the bloom time to the start of the second pour.
                else if (currentTime < secondPour * 1000) {
                    line1 = "Pour half of the water (" + firstPourAmount + "mL)";
                    line2 = "over the coffee in a circular motion.";
                    line3 = "Be careful not to hit the sides of the filter!";
                }

                // From the end of the first pour to the end time.
                else if (currentTime > secondPour * 1000) {
                    line1 = "Pour the remaining water over the coffee";
                    line2 = "As soon as all water has drained, enjoy!";
                    line3 = "";
                }

                // Stop the animation.
                if (currentTime > endTime * 1000) {
                    Log.v("From TimerView", "Runnable ended.");
                    stop = true;
                }
                // Wait one tenth of a second before updating the view.
                mHandler.postDelayed(this, 100);

                // Invalidate the old view in order to draw the new one.
                TimerView.this.invalidate();
            }
        };

        runnable.run();

        this.invalidate();
    }


    // Calculate the times and amounts of various things.
    private void calculateTimesandAmounts(double cups, int grams) {
        this.grams = grams;
        this.cups = cups;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        try {
            bloomTime = Integer.parseInt(prefs.getString("bloom", "30"));
            ml = (int) Math.round(Integer.parseInt(prefs.getString("ml", "300")) * cups);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            bloomTime = 30;
            ml = (int) Math.round(300 * cups);
        }

        // calculate the end time.
        endTime = (int) Math.round(cups * 3 * 60);
        // Calculate the amount of water to pour for the first pour.
        // This assumes that the amount of coffee used in the bloom was around 40 grams.
        firstPourAmount = (ml / 2) - 40;

        // Calculate how long the two pours should.
        // It is half the total time with the bloom time added on.
        secondPour = (endTime / 2) + bloomTime;


    }
}