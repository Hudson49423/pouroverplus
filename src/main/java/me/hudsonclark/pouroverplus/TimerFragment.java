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

import me.hudsonclark.pouroverplus.view.TimerView;

public class TimerFragment extends Fragment {

    private TimerView tv;

    public void startTimer(double cups, int grams) {
        tv.animate(cups, grams);
    }


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
        tv.stop();
    }

    @Override
    public void onPause() {
        super.onPause();
        tv.stop();
    }
}
