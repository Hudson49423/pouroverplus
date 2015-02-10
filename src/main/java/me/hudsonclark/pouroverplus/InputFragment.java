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
    private int coffeeGrams;
    private double coffeeTablespoons;

    private Button cupsUp;
    private Button cupsDown;

    private TextView cupsText;
    private TextView coffeeGramsText;
    private TextView coffeeTablespoonsText;

    private int gramsRatio;
    private int tablespoonRatio;

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
        cupsUp = (Button) view.findViewById(R.id.cups_up);
        cupsDown = (Button) view.findViewById(R.id.cups_down);


        cupsUp.setOnClickListener(this);
        cupsDown.setOnClickListener(this);

        cupsText = (TextView) view.findViewById(R.id.cups);
        coffeeGramsText = (TextView) view.findViewById(R.id.coffee);
        coffeeTablespoonsText = (TextView) view.findViewById(R.id.coffee_tbsp);

        numCups = 1;
        coffeeGrams = 19;
        coffeeTablespoons = 2;

        cupsText.setText("" + numCups);
        coffeeGramsText.setText("" + coffeeGrams  + "g");
        coffeeTablespoonsText.setText(("" + coffeeTablespoons + "tbsp"));


        gramsRatio = 16;
        tablespoonRatio = 2;
    }

    @Override
    public void onClick(View v) {
        if (v == cupsUp) {
            numCups += 0.5;
            update();
        }
        else if (v == cupsDown && numCups > 0) {
            numCups -= 0.5;
            update();
        }
    }

    private void update() {
        // Calculate grams.
        coffeeGrams = (int) Math.round(numCups * 300 / gramsRatio);

        // Calculate tablespoons.
        coffeeTablespoons = numCups * tablespoonRatio;

        // Update the text.
        coffeeGramsText.setText("" + coffeeGrams  + "g");
        coffeeTablespoonsText.setText("" + coffeeTablespoons + "tbsp");
        cupsText.setText("" + numCups);
    }

}
