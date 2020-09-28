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
//    private double distance, aperture;
    private EditText inputDistance, inputAperture;
    private TextView outputNear, outputFar, outputDepth, outputHyperDis;
    private double far, near, DepthField, HyperFocalDis, distance, aperture;;
    private boolean past1 = false, past2 = false;

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
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // get the lens which is selected by the user, and print it
        Lens_manager manager = Lens_manager.getInstance();
        Lens lens = manager.getByIndex(index);
        TextView lensDetails = findViewById(R.id.lensDetails);
        lensDetails.setText(lens.toString());

        // calculation and updateUI
//        calculateAll_2(lens);
//        calculateAll();
//        updateUI();

        // changes in input
        inputAperture = (EditText) findViewById(R.id.inputAperture);
        inputDistance = (EditText) findViewById(R.id.inputDistance);
        inputDistance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                Toast.makeText(CalculateDepthOfField.this, "distance: "+s.toString(), Toast.LENGTH_SHORT).show();
                past1 = true;
                String strDistance = s.toString();
                if(strDistance == null || strDistance.equals("")) past1 = false;
                else {
                    distance = Double.valueOf(strDistance);
                    if (distance < 0) past1 = false;
                    if (past1 && past2) {
                        calculateAll_2(lens);
                    }
                }
                updateUI();
            }
        });
        inputAperture.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
//                Toast.makeText(CalculateDepthOfField.this, "aperture: "+s.toString(), Toast.LENGTH_SHORT).show();
                past2 = true;
                String strAperture = s.toString();
                if(strAperture == null || strAperture.equals("")) past2 = false;
                else {
                    aperture = Double.valueOf(strAperture);
                    if (aperture < lens.getF_num() || aperture > 22) past2 = false;
                    if (past1 && past2) {
                        calculateAll_2(lens);
                    }
                }
                updateUI();
            }
        });
    }


    private void calculateAll_2(Lens lens) {
//        Toast.makeText(this, "working in calculateAll_2()", Toast.LENGTH_SHORT).show();
        Depth_calculator calculator = new Depth_calculator(lens, distance * 1000, aperture);
        far = calculator.far_focal_point() / 1000;
        near = calculator.near_focal_point() / 1000;
        DepthField = calculator.depth_field() / 1000;
        HyperFocalDis = calculator.hyper_focal_distance() / 1000;
    }

    private void updateUI() {
        Toast.makeText(CalculateDepthOfField.this, "past1: "+Boolean.toString(past1) + " past2: "+Boolean.toString(past2), Toast.LENGTH_SHORT).show();
        outputHyperDis = findViewById(R.id.outputHyperDis);
        outputDepth = findViewById(R.id.outputDepth);
        outputNear = findViewById(R.id.outputNear);
        outputFar = findViewById(R.id.outputFar);
        if(past1 && past2) outputHyperDis.setText(String.format(Locale.CANADA, "%.2f", HyperFocalDis));
        else outputHyperDis.setText("invalid");

        if(past1 && past2) outputDepth.setText(String.format(Locale.CANADA, "%.2f", DepthField));
        else outputDepth.setText("invalid");

        if(past1 && past2) outputNear.setText(String.format(Locale.CANADA, "%.2f", near));
        else outputNear.setText("invalid");

        if(past1 && past2) outputFar.setText(String.format(Locale.CANADA, "%.2f", far));
        else outputFar.setText("invalid");
    }

    // interface for MainActivity to switch to AddLens activity
    public static Intent makeLaunchIntent(Context c, String message, int position) {
        index = position;
        Intent intent = new Intent(c, CalculateDepthOfField.class);
        intent.putExtra("Extra - message", message);
        return intent;
    }

    //    private void calculateAll() {
//            double distance, aperture;
//            inputAperture = (EditText) findViewById(R.id.inputAperture);
//            inputDistance = (EditText) findViewById(R.id.inputDistance);
//            String strAperture = inputAperture.getText().toString();
//            String strDistance = inputDistance.getText().toString();
//            if(!TextUtils.isEmpty(inputAperture.getText().toString()) && !TextUtils.isEmpty(inputDistance.getText().toString())) {
//
//                Log.i("calculation", "Correctly!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//            aperture = Double.valueOf(inputAperture.getText().toString());
//            distance = Double.valueOf(inputDistance.getText().toString());
//            Lens_manager manager = Lens_manager.getInstance();
//            Lens lens = manager.getByIndex(index);
//            Depth_calculator calculator = new Depth_calculator(lens, distance * 1000, aperture);
//            far = calculator.far_focal_point() / 1000;
//            near = calculator.near_focal_point() / 1000;
//            DepthField = calculator.depth_field() / 1000;
//            HyperFocalDis = calculator.hyper_focal_distance() / 1000;
//            outputNear.setText(Double.toString(near));
//            outputFar.setText(Double.toString(far));
//            outputDepth.setText(Double.toString(DepthField));
//            outputHyperDis.setText(Double.toString(HyperFocalDis));

//            updateUI();
//
//            }
//    }
}