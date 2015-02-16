package me.hudsonclark.pouroverplus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by hudson49423 on 2/15/15.
 */
public class TimerView extends View{

    private int duration;
    private Paint paint1;
    private Paint paint2;

    public TimerView(Context context) {
        super(context);
        init();
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void run() {
        final Handler mhandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Do the work.
                mhandler.postDelayed(this, 50);
                TimerView.this.invalidate();
            }
        };

        runnable.run();
    }

    @Override
    public void onDraw(Canvas canvas) {
        duration += 1;
        //canvas.drawRect(((float)duration), ((float)duration), ((float)duration), ((float)duration), paint1);

        float centerY = canvas.getHeight() / 2;
        float centerX = canvas.getWidth() / 2;

        canvas.drawRect(centerX - duration, centerY - duration, centerX + duration, centerY + duration,  paint1);
        Log.v("Test", "ondraw() called");
    }

    private void init() {
        super.setWillNotDraw(false);
        duration = 100;

        paint1 = new Paint();
        paint1.setStyle(Paint.Style.FILL);
        paint1.setAntiAlias(true);
        paint1.setColor(Color.BLUE);


        Log.v("Test", "init called");
        run();
        this.invalidate();
    }

}