package me.hudsonclark.pouroverplus;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TimerFragment extends Fragment {

    private Handler mHandler;
    private Runnable runnable;

    public void startTimer(double cups, int grams) {
        // Here we actually start the animation.
        // Eventually user will specify time etc.
        tv.animate(cups, grams);
    }

    private TimerView tv;

    public TimerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tv = new TimerView(getActivity());
        return tv;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHandler.removeCallbacks(runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("From Fragment", "Runnable ended.");

    }


    // ---------------------------------------------------------------------------------------------

    public class TimerView extends View {

        private int duration;
        private Paint textPaint;
        private Paint paint2;

        private long startTime;
        private int bloomTime;
        private long endTime;
        private long currentTime;

        private String text;
        private String text2;

        public TimerView(Context context) {
            super(context);
            init();
        }

        @Override
        public void onDraw(Canvas canvas) {
            float centerY = canvas.getHeight() / 2;
            float centerX = canvas.getWidth() / 2;

            //canvas.drawRect(centerX - duration, centerY - duration, centerX + duration, centerY + duration, paint1);


            if (text != null && text2 != null) {
                canvas.drawText(text, (float) (canvas.getWidth() * .10), (float) (canvas.getHeight() * .10), textPaint);
                canvas.drawText(text2, (float) (canvas.getWidth() * .12), (float) (canvas.getHeight() * .20), textPaint);

            }
        }

        private void init() {
            super.setWillNotDraw(false);
            duration = 100;

            textPaint = new Paint();
            textPaint.setStyle(Paint.Style.STROKE);
            textPaint.setAntiAlias(true);
            textPaint.setColor(Color.BLUE);
            textPaint.setTextSize(60f);
            this.invalidate();
        }

        public void animate(double cups, int grams) {
            startTime = 0;

            mHandler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    // Do the work.

                    Log.v("From TimerView", "Runnable going.");

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

                    if (currentTime > 11000) {
                        Log.v("From TimerView", "Runnable ended.");
                        return;

                    }

                    mHandler.postDelayed(this, 1000);
                    TimerView.this.invalidate();
                }
            };
            runnable.run();

        }
    }
}
