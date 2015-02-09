package me.hudsonclark.pouroverplus;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class InputFragment extends Fragment implements View.OnClickListener {

    private double numCups;
    private int numCoffee;
    private Button cupsUp;
    private Button cupsDown;
    private Button coffeeDown;
    private Button coffeeUp;
    private TextView cupsText;
    private TextView coffeeText;

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

        setUp(rootView);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void setUp(View view) {
        try {
            numCups = Double.parseDouble(((TextView) view.findViewById(R.id.cups)).getText().toString());
            numCoffee = Integer.parseInt(((TextView) view.findViewById(R.id.coffee)).getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        cupsUp = (Button) view.findViewById(R.id.cups_up);
        cupsDown = (Button) view.findViewById(R.id.cups_down);
        coffeeUp = (Button) view.findViewById(R.id.coffee_up);
        coffeeDown = (Button) view.findViewById(R.id.coffee_down);

        cupsUp.setOnClickListener(this);
        cupsDown.setOnClickListener(this);
        coffeeDown.setOnClickListener(this);
        coffeeUp.setOnClickListener(this);

        cupsText = (TextView) view.findViewById(R.id.cups);
        coffeeText = (TextView) view.findViewById(R.id.coffee);

        numCups = 1;
        numCoffee = 19;

        cupsText.setText("" + numCups);
        coffeeText.setText("" + numCoffee);

    }

    @Override
    public void onClick(View v) {
        if (v == cupsUp) {
            numCups += 0.5;
            numCoffee = (int) Math.round(numCups * 300 / 16);
            coffeeText.setText("" + numCoffee);
            cupsText.setText("" + numCups);
        }
        else if (v == cupsDown && numCups > 0) {
            numCups -= 0.5;
            numCoffee = (int) Math.round(numCups * 300 / 16);
            coffeeText.setText("" + numCoffee);
            cupsText.setText("" + numCups);
        }
    }

}
