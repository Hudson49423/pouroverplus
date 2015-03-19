package me.hudsonclark.pouroverplus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScreenSlidePageFragment extends Fragment {

    public static ScreenSlidePageFragment newInstance(int position) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Here we decide which layout to inflate.
        int position = this.getArguments().getInt("position", 0);

        if (position == 1) {
            // Inflate layout one.
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.help_screen_two, container, false);
            return rootView;
        } else {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.help_screen_one, container, false);
            return rootView;
        }
    }
}

