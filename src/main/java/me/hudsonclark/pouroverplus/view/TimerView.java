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
 * A simple view class for animating pour over timers and
 * an animation for the brew process.
 */
public class TimerView extends View {

    // Whether the animation is able to run.
    public static boolean stop;
    private boolean initHasBeenCalled;

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

    // String constants to use as instructions.
    private final String bloomInstruction1 = "Pour just enough water to completely";
    private final String bloomInstruction2 = "cover the coffee and let it bloom";
    // No instruction three because we need to incorporate time into the string.
    // No instruction one for same reason.
    private final String firstPourInstruction2 = "over the coffee in a circular motion";
    private final String firstPourInstruction3 = "Try not to hit the sides of the filter!";
    private final String secondPourInstruction1 = "Pour the remaining water over the coffee";
    private final String secondPourInstruction2 = "as soon as all water has drained, enjoy!";
    // private final String secondPourInstruction3 = "";

    // Used to decide whether or not to start "draining"
    private boolean drain;

    // This is the percentage of the pourover that is filled.
    private double progress;

    public TimerView(Context context) {
        super(context);
        init();
        stop = true;
    }

    @Override
    public void onDraw(Canvas canvas) {

        canvas.drawColor(Color.argb(245,132,72,0));

        // The center of the canvas.
        //float centerY = canvas.getHeight() / 2;
        float centerX = canvas.getWidth() / 2;

        float height = canvas.getHeight();
        float width = canvas.getWidth();

        // Display this if the animation is not running.
        if (stop) {
            timeString = "0:00";
            remainingTimeString = "0:00";
            progress = 0;
            textPaint.setTextSize(75);
            line1 = "Touch the '?' for help or to learn";
            line2 = "more about making a pour over!";
            //line3 = "";

        }

        // Makes sure that everything has been set up already.
        if (initHasBeenCalled) {

            // Draw the instructions.
            textPaint.getTextBounds(line1, 0, line1.length(), bounds1);
            canvas.drawText(line1, (centerX - (bounds1.width() / 2)), (float) (height * .075), textPaint);

            textPaint.getTextBounds(line2, 0, line2.length(), bounds2);
            canvas.drawText(line2, (centerX - (bounds2.width() / 2)), (float) (height * .15), textPaint);

            textPaint.getTextBounds(line3, 0, line3.length(), bounds3);
            canvas.drawText(line3, (centerX - (bounds3.width() / 2)), (float) (height * .225), textPaint);

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

            waterPath.reset(); // Important.
            waterPath.moveTo(centerX, lowHeight); // Starting point is always the same.

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

        mHandler = new Handler();

        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        fillPaint.setAntiAlias(true);
        fillPaint.setColor(Color.argb(255,63,81,181));

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStrokeWidth(15);

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(60);

        timerPaint = new Paint();
        timerPaint.setStyle(Paint.Style.STROKE);
        timerPaint.setAntiAlias(true);
        timerPaint.setColor(Color.WHITE);
        timerPaint.setTextSize(100);

        // Create the strings.
        line1 = "";
        line2 = "";
        line3 = "";

        // Need to create it so that we don't get any null pointers
        timeString = "0:00";
        remainingTimeString = "0:00";

        // Create the bounds that are used to center strings.
        bounds1 = new Rect();
        bounds2 = new Rect();
        bounds3 = new Rect();
        oval = new RectF();

        // Create the path that is used to draw the triangle of water.
        waterPath = new Path();

        // Drain should start as false.
        drain = false;

        initHasBeenCalled = true;

        // Call the onDraw method.
        this.invalidate();
    }

    public void animate(double cups, int grams) {
        this.invalidate();
        stop = false;
        calculateTimesAndAmounts(cups, grams);
        drain = false;
        progress = 0;
        startTime = System.currentTimeMillis();
        runnable = new Runnable() {
            @Override
            public void run() {
                // See if we should stop the animation.
                if (stop)
                    return;
                // Get the seconds and minutes -----------------------------------------------------
                currentTime = (System.currentTimeMillis() - startTime) / 1000;
                seconds = Math.round(currentTime % 60);
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
                    line1 = bloomInstruction1;
                    line2 = bloomInstruction2;
                    line3 = "for " + bloomTime + " seconds";
                    // In this stage, water should be filled up a little, and should not drain,
                    // Therefore, progress should climb to .3 and stay there the remained for the
                    // Stage.
                    // Stop the progress at .3
                    if (progress < .3) {
                        progress = progress + .005; // Constant rate.
                    }
                }
                // From the end of the bloom time to the start of the second pour.
                else if (currentTime < (secondPour + bloomTime)) {
                    line1 = "Pour half of the water (" + secondPour + "mL) slowly";
                    line2 = firstPourInstruction2;
                    line3 = firstPourInstruction3;
                    // Here water should be filled up more.
                    // After it is filled up more it should "drain".
                    // The rate changes depending on how long the
                    // pour should take.
                    //Decide whether or not we should start draining the water.
                    if (progress > .69) {
                        drain = true;
                    }
                    if (!drain) {
                        // Take 1/3 of the time to fill up with water.
                        progress = progress + (.4f / ((secondPour / 2) * 20f));
                    } else {
                        // Take 2/3 of the time to drain some of the water.
                        progress = progress - (.3f / ((secondPour / 2) * 20f));
                    }
                }

                // From the end of the first pour to the end time.
                else if (currentTime > (secondPour + bloomTime)) {
                    line1 = secondPourInstruction1;
                    line2 = secondPourInstruction2;
                    line3 = "";
                    // in this stage we want to pour the rest of the water and
                    // then let all of the water drain.
                    // as soon as the water has gotten up to this point,
                    // it is time to drain completely.
                    if (progress > .78) {
                        drain = false;
                    }
                    // Drain is reversed because of the previous stage.
                    if (drain) {
                        // take 1/3 of the time to fill up with water.
                        progress = progress + (.4f / ((secondPour / 2) * 20f));
                    } else {
                        // take 2/3 of the time to drain some of the water.
                        progress = progress - (.8f / ((secondPour / 2) * 20f));
                    }
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

        // The endtime is always 3 minutes * the number of cups.
        // So, we should first take to bloom time out of the
        // Time since the user picks it.
        // Now, we say that the pours should be split evenly.
        secondPour = ((endTime - bloomTime) / 2);
    }
}