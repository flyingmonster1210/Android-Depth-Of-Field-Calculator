package cmpt276.a2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;

public class CalculateDepthOfField extends AppCompatActivity {
    private static final String EXTRA_MESSAGE = "Extra - message";

    private static int index = 0; // get lens by index in manager
    private EditText inputDistance, inputAperture;
    private TextView outputNear, outputFar, outputDepth, outputHyperDis;
    private double far, near, DepthField, HyperFocalDis, distance, aperture;;
    private boolean past1 = false, past2 = false;
    private boolean[] validCheck = {false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_depth_of_field);

        // set toolbar's name
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Calculate Depth of Field");

        // set up the "up" bottom
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // initialize Intent i, and print information - "switch to the calculator!"
        Intent i = getIntent();
        String message = i.getStringExtra(EXTRA_MESSAGE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // get the lens which is selected by the user, and print it
        Lens_manager manager = Lens_manager.getInstance();
        Lens lens = manager.getByIndex(index);
        TextView lensDetails = findViewById(R.id.lensDetails);
        lensDetails.setText(lens.toString());
        TextView apertureLimit = findViewById(R.id.apertureLimit);
        apertureLimit.setText("[" + lens.getF_num() + ", 22]");

        // changes in input
        inputDistance = (EditText) findViewById(R.id.inputDistance);
        inputAperture = (EditText) findViewById(R.id.inputAperture);
        monitorEditText(inputDistance, lens, 0);
        monitorEditText(inputAperture, lens, 1);
    }

    // do calculations always called by function - monitorEditText()
    private void calculateAll(Lens lens) {
//        Toast.makeText(this, "working in calculateAll_2()", Toast.LENGTH_SHORT).show();
        Depth_calculator calculator = new Depth_calculator(lens, distance * 1000, aperture);
        far = calculator.far_focal_point() / 1000;
        near = calculator.near_focal_point() / 1000;
        DepthField = calculator.depth_field() / 1000;
        HyperFocalDis = calculator.hyper_focal_distance() / 1000;
    }

    // update UI
    private void updateUIALL() {
//        Toast.makeText(CalculateDepthOfField.this, "past1: "+Boolean.toString(validCheck[0]) + " past2: "+Boolean.toString(validCheck[1]), Toast.LENGTH_SHORT).show();
        updateUISingle(outputHyperDis, R.id.outputHyperDis, HyperFocalDis);
        updateUISingle(outputDepth, R.id.outputDepth, DepthField);
        updateUISingle(outputNear, R.id.outputNear, near);
        updateUISingle(outputFar, R.id.outputFar, far);
    }
    // always call by function - updateUIAll()
    private void updateUISingle(TextView textView, final int textViewID, double value) {
        textView = (TextView) findViewById(textViewID);
        if(validCheck[0] && validCheck[1])
            textView.setText(String.format(Locale.CANADA, "%.2f(m)", value));
        else
            textView.setText("Invalid");
    }

    // interface for MainActivity to switch to AddLens activity
    public static Intent makeLaunchIntent(Context c, String message, int position) {
        index = position;
        Intent intent = new Intent(c, CalculateDepthOfField.class);
        intent.putExtra("Extra - message", message);
        return intent;
    }

    // it is used to capture the information from each editText
    private void monitorEditText(EditText editText, Lens lens, int position) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
//                Toast.makeText(CalculateDepthOfField.this, "text: "+s.toString(), Toast.LENGTH_SHORT).show();
                validCheck[position] = true;
                String string = s.toString();
                if(position == 1) {
                    if (string == null || string.equals("")) validCheck[position] = false;
                    else {
                        aperture = Double.valueOf(string);
                        if (aperture < lens.getF_num() || aperture > 22)
                            validCheck[position] = false;
                        if (validCheck[0] && validCheck[1]) {
                            calculateAll(lens);
                        }
                    }
                }
                else {
                    if (string == null || string.equals("")) validCheck[position] = false;
                    else {
                        distance = Double.valueOf(string);
                        if (distance < 0)
                            validCheck[position] = false;
                        if (validCheck[0] && validCheck[1]) {
                            calculateAll(lens);
                        }
                    }
                }
                updateUIALL();
            }
        });
    }
}