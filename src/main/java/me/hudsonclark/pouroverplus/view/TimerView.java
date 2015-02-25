package me.hudsonclark.pouroverplus.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
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
    private Rect bounds1;
    private Rect bounds2;
    private Rect bounds3;
    private RectF oval;
    private Path waterPath;

    // Various times and amounts.
    private double cups;
    private int ml;
    private int grams;
    private int bloomTime;
    private int firstPourAmount;
    private int secondPour;
    private int endTime; // The end time in seconds.

    // Used in creating the timers.
    private int minutes;
    private int seconds;
    private int remainingMinutes;
    private int remainingSeconds;
    private String timeString;
    private String remainingTimeString;
    private int re; // The remaining time in seconds.

    // The stage that the pour over in currently in.
    private int stage;

    // These variables are used in the onDraw method.
    private double theta;
    private float waterWidth;
    private float waterHeight;
    private float lowHeight;
    private float highHeight;
    private int i;

    // This is the percentage of the pourover that is filled.
    private double progress;

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
            canvas.drawText("Current", (float) (width * .05), ((float) (height * .85)), timerPaint);
            canvas.drawText(timeString, (float) (width * .1), ((float) (height * .85)) + 100, timerPaint);

            // Draw the remaining time.
            canvas.drawText("Remaining", (float) (width * .6), ((float) (height * .85)), timerPaint);
            canvas.drawText(remainingTimeString, (float) (width * .7), ((float) (height * .85)) + 100, timerPaint);


            // Draw the actual pourover.
            oval.set((float) (width * .2), (float) (height * .3), (float) (width * .8), (float) (height * .35));
            canvas.drawOval(oval, strokePaint);


            lowHeight = (float) (height * .75);
            highHeight = (float) (height * .325);

            canvas.drawLine((float) (width * .2), highHeight, centerX, lowHeight, strokePaint);
            canvas.drawLine((float) (width * .8), highHeight, centerX, lowHeight, strokePaint);

            theta = Math.atan((centerX - (width * .2)) / (lowHeight - highHeight));

            waterPath.moveTo(centerX,lowHeight); // Starting point is always the same.

            // Here is where the height of the "water" is actually determined.
            waterHeight = ((float) (lowHeight - ((lowHeight - highHeight) * progress)));
            waterWidth = ((float) (Math.tan(theta) * (lowHeight - waterHeight)));

            waterPath.lineTo(centerX + waterWidth, waterHeight);
            waterPath.lineTo(centerX - waterWidth, waterHeight);

            waterPath.lineTo(centerX, lowHeight); // Ending point is always the same.

            // Draw the path that was just created.
            canvas.drawPath(waterPath, fillPaint);

        }

    }

    private void init() {
        // Make sure that the onDraw method will get called.
        super.setWillNotDraw(false);

        // Create the handler.
        mHandler = new Handler();

        // Create the paint.
        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        fillPaint.setAntiAlias(true);
        fillPaint.setColor(Color.BLUE);

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(Color.BLUE);
        strokePaint.setStrokeWidth(5);

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

        // Create the bounds that are used to center strings.
        bounds1 = new Rect();
        bounds2 = new Rect();
        bounds3 = new Rect();
        oval = new RectF();

        // Create the path that is used to draw the triangle of water.
        waterPath = new Path();

        // Call the onDraw method.
        this.invalidate();
    }

    public void animate(double cups, int grams) {
        stop = false;

        calculateTimesAndAmounts(cups, grams);
        i = 0;

        startTime = System.currentTimeMillis();
        runnable = new Runnable() {
            @Override
            public void run() {
                i++;

                // See if we should stop the animation.
                if (stop)
                    return;

                currentTime = (System.currentTimeMillis() - startTime) / 1000;

                // Get the seconds and minutes -----------------------------------------------------
                seconds = Math.round(currentTime % 600);
                minutes = Math.round(currentTime / 60);
                re = endTime - Math.round(currentTime);
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
                // ---------------------------------------------------------------------------------


                // From 0 to the bloom time.
                if (currentTime < bloomTime) {
                    line1 = "Pour just enough water to completely";
                    line2 = "cover the coffee and let the coffee bloom";
                    line3 = "for 30 seconds";

                    // In this stage, water should be filled up a little, and should not drain,
                    // Therefore, progress should climb to .3 and stay there the remained for the
                    // Stage.

                    // Stop the progress at .3
                    if (progress < .3) {
                        progress = ((float) i / 1000) * 4.5; // 4.5 is accelerating it.
                    }
                    stage = 1;
                }

                // From the end of the bloom time to the start of the second pour.
                else if (currentTime < secondPour) {
                    line1 = "Pour half of the water (" + firstPourAmount + "mL)";
                    line2 = "over the coffee in a circular motion.";
                    line3 = "Be careful not to hit the sides of the filter!";
                    stage = 2;
                }

                // From the end of the first pour to the end time.
                else if (currentTime > secondPour) {
                    line1 = "Pour the remaining water over the coffee";
                    line2 = "As soon as all water has drained, enjoy!";
                    line3 = "";
                    stage = 3;
                }

                // Stop the animation.
                if (currentTime > endTime) {
                    Log.v("From TimerView", "Runnable ended.");
                    stop = true;
                }
                // Wait 1/20 of a second before updating the view.
                mHandler.postDelayed(this, 50);

                // Call the onDraw method.
                TimerView.this.invalidate();
            }
        };

        runnable.run();

        this.invalidate();
    }


    // Calculate the times and amounts of various things.
    private void calculateTimesAndAmounts(double cups, int grams) {
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