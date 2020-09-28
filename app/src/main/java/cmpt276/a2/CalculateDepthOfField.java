package cmpt276.a2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
    private double far, near, DepthField, HyperFocalDis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_depth_of_field);

        // set toolbar's name
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Calculate Depth of Field");

        // initialize Intent i, and print information - "switch to the calculator!"
        Intent i = getIntent();
        String message = i.getStringExtra(EXTRA_MESSAGE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // set up the "up" bottom
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        
        // calculation and updateUI
//        calculateAll();
        updateUI();
    }

    private void calculateAll() {
        while(true) {
            if(inputAperture.equals("") || inputDistance.equals(""))
//                return;
                continue;
            double distance, aperture;
            inputAperture = (EditText) findViewById(R.id.inputAperture);
            inputDistance = (EditText) findViewById(R.id.inputDistance);
            aperture = Double.valueOf(inputAperture.getText().toString());
            distance = Double.valueOf(inputDistance.getText().toString());
            Lens_manager manager = Lens_manager.getInstance();
            Lens lens = manager.getByIndex(index);
            Depth_calculator calculator = new Depth_calculator(lens, distance*1000, aperture);
            far  = calculator.far_focal_point()/1000;
            near = calculator.near_focal_point()/1000;
            DepthField = calculator.depth_field()/1000;
            HyperFocalDis = calculator.hyper_focal_distance()/1000;
//            outputNear.setText(Double.toString(near));
//            outputFar.setText(Double.toString(far));
//            outputDepth.setText(Double.toString(DepthField));
//            outputHyperDis.setText(Double.toString(HyperFocalDis));

            updateUI();
        }
    }

    private void updateUI() {
        outputHyperDis = findViewById(R.id.outputHyperDis);
        outputDepth = findViewById(R.id.outputDepth);
        outputNear = findViewById(R.id.outputNear);
        outputFar = findViewById(R.id.outputFar);
        outputHyperDis.setText(String.format(Locale.CANADA, "%.2f", HyperFocalDis));
        outputDepth.setText(String.format(Locale.CANADA, "%.2f", DepthField));
        outputNear.setText(String.format(Locale.CANADA, "%.2f", near));
        outputFar.setText(String.format(Locale.CANADA, "%.2f", far));
    }

    // interface for MainActivity to switch to AddLens activity
    public static Intent makeLaunchIntent(Context c, String message, int position) {
        index = position;
        Intent intent = new Intent(c, CalculateDepthOfField.class);
        intent.putExtra("Extra - message", message);
        return intent;
    }
}