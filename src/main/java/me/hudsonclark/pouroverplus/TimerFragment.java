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
    }

    public class TimerView extends View {

        private int duration;
        private Paint paint1;
        private Paint paint2;

        public TimerView(Context context) {
            super(context);
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
            float centerY = canvas.getHeight() / 2;
            float centerX = canvas.getWidth() / 2;

            canvas.drawRect(centerX - duration, centerY - duration, centerX + duration, centerY + duration, paint1);
        }

        private void init() {
            super.setWillNotDraw(false);
            duration = 100;

            paint1 = new Paint();
            paint1.setStyle(Paint.Style.FILL);
            paint1.setAntiAlias(true);
            paint1.setColor(Color.BLUE);
            //run();
            this.invalidate();
        }

        public void incrementDuration() {
            this.invalidate();
        }
    }
}
