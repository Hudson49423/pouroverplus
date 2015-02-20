package me.hudsonclark.pouroverplus.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
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

    private final int bloomTime = 30;

    // The percent done with the animation.
    private float progress;

    // For the actual timer.
    private long startTime;
    private long currentTime;

    // The total brew time.
    private int duration;

    // For the text in the animations these variables are reused.
    private String line1;
    private String line2;

    public TimerView(Context context) {
        super(context);
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {

        // The center of the canvas.
        float centerY = canvas.getHeight() / 2;
        float centerX = canvas.getWidth() / 2;

        // Display this if the animation is not running.
        if (stop) {

        }

        // Display this if the animation is currently running.
        else {
            // As a test lets make a simple progress bar
            canvas.drawRect(centerX - 20, centerY + 200 - (progress / 50), centerX + 20, centerY + 200, fillPaint);
            canvas.drawRect(centerX - 20, centerY + 200 - (20000 / 50), centerX + 20, centerY + 200, strokePaint);

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

        // Call the onDraw method.
        this.invalidate();
    }

    public void animate(double cups, int grams) {
        stop = false;

        startTime = 0;
        progress = 1;

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

                if (currentTime < bloomTime) {
                    line1 = "Pour just enough water to completely wet the grounds,";
                    line2 = "and let the coffee bloom for 30 seconds";
                }

                // Make a simple progress bar that runs for 20 seconds.
                if (currentTime < 20000) {
                    progress = currentTime;
                }

                // Stop the animation after 20 seconds.
                if (currentTime > 20000) {
                    Log.v("From TimerView", "Runnable ended.");
                    return;

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
}