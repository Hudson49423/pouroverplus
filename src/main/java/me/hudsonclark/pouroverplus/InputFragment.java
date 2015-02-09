package me.hudsonclark.pouroverplus;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class InputFragment extends Fragment {

    private EditText coffee;
    private EditText cups;

    public InputFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.input, container, false);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    private void changeCoffee() {}

    private void changeCups() {}
}
