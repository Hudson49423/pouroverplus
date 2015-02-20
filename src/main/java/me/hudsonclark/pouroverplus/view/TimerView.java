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

    public static boolean stop;

    private Handler mHandler;
    private Runnable runnable;

    private int duration;
    private Paint fillPaint;
    private Paint strokePaint;

    private long startTime;
    private int bloomTime;
    private long endTime;
    private long currentTime;

    private String text;
    private String text2;

    // For making a sample progress bar
    private float progress;

    public TimerView(Context context) {
        super(context);
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {
        float centerY = canvas.getHeight() / 2;
        float centerX = canvas.getWidth() / 2;

//        if (text != null && text2 != null) {
//            canvas.drawText(text, (float) (canvas.getWidth() * .10), (float) (canvas.getHeight() * .10), textPaint);
//            canvas.drawText(text2, (float) (canvas.getWidth() * .12), (float) (canvas.getHeight() * .20), textPaint);
//
//        }

        // As a test lets make a simple progress bar
        canvas.drawRect( centerX - 20, centerY + 200 - (progress / 50), centerX + 20, centerY + 200, fillPaint);
        canvas.drawRect( centerX - 20, centerY + 200 - (20000 / 50), centerX + 20, centerY + 200, strokePaint);
    }

    private void init() {
        super.setWillNotDraw(false);
        duration = 100;

        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        fillPaint.setAntiAlias(true);
        fillPaint.setColor(Color.BLUE);

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(Color.BLUE);
        this.invalidate();
        this.invalidate();
    }

    public void animate(double cups, int grams) {
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

                if (currentTime < 10000) {
                    text = "Pour just enough water to wet the grounds,";
                    text2 = "and let the coffee bloom for 30 seconds";
                } else {
                    text = null;
                    text2 = null;
                }

                // Make a simple progress bar.
                if (currentTime < 20000) {
                    progress =  currentTime;
                }

                // Stop the animation after 11 seconds.
                if (currentTime > 20000) {
                    Log.v("From TimerView", "Runnable ended.");
                    return;

                }

                // Wait one second before updating the view.
                mHandler.postDelayed(this, 100);

                // Invalidate the old view in order to draw the new one.
                TimerView.this.invalidate();
            }
        };

        runnable.run();
    }

    /**
     * Stop the animation.
     */
    public void stop() {
        stop = true;
    }
}