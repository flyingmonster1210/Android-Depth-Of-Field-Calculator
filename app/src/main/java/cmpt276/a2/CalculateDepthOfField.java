/**
 * file: CalculateDepthOfField.java
 * Author: Weijie Zeng
 * ID: 301379422
 *
 * can do the input error checking
 * and auto-recalculation
 */
package cmpt276.a2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;

public class CalculateDepthOfField extends AppCompatActivity {
    private static final String EXTRA_MESSAGE = "Extra - message";
    private static final int REQUEST_CODE_AddLENS = 11; // just a random number

    private static int index = 0; // get lens by index in manager
    private EditText inputDistance, inputAperture;
    private TextView outputNear, outputFar, outputDepth, outputHyperDis, lensDetails;
    private double far, near, DepthField, HyperFocalDis, distance, aperture;;
    private boolean past1 = false, past2 = false;
    private boolean[] validCheck = {false, false}; // validCheck[0] - check inputDistance, validCheck[1] - check inputAperture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_depth_of_field);

        // set toolbar's name
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Calculating DoF");

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
        lensDetails = findViewById(R.id.lensDetails);
        lensDetails.setText(lens.toString());
        TextView apertureLimit = findViewById(R.id.apertureLimit);
        apertureLimit.setText("[" + lens.getF_num() + ", 22]");

        // changes in input
        inputDistance = (EditText) findViewById(R.id.inputDistance);
        inputAperture = (EditText) findViewById(R.id.inputAperture);
        monitorEditText(inputDistance, 0);
        monitorEditText(inputAperture, 1);
    }

    // do calculations always called by function - monitorEditText()
    private void calculateAll(Lens lens) {
        Depth_calculator calculator = new Depth_calculator(lens, distance * 1000, aperture);
        far = calculator.far_focal_point() / 1000;
        near = calculator.near_focal_point() / 1000;
        DepthField = calculator.depth_field() / 1000;
        HyperFocalDis = calculator.hyper_focal_distance() / 1000;
    }

    // update UI
    private void updateUIALL() {
        updateUISingle(outputHyperDis, R.id.outputHyperDis, HyperFocalDis);
        updateUISingle(outputDepth, R.id.outputDepth, DepthField);
        updateUISingle(outputNear, R.id.outputNear, near);
        updateUISingle(outputFar, R.id.outputFar, far);
        Lens_manager manager = Lens_manager.getInstance();
        lensDetails.setText(manager.getByIndex(index).toString());
        TextView apertureLimit = findViewById(R.id.apertureLimit);
        apertureLimit.setText("[" + manager.getByIndex(index).getF_num() + ", 22]");
    }
    // always called by function - updateUIAll()
    private void updateUISingle(TextView textView, final int textViewID, double value) {
        // validCheck[0] - check inputDistance, validCheck[1] - check inputAperture;
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

    // following two override functions will set up the remove button on the tool bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calculate_depth_of_field, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_lens:
                Lens_manager manager = Lens_manager.getInstance();
                if(index >= 0 && index < manager.getSize()) {
//                    Toast.makeText(this, "lens" + manager.getByIndex(index) + " is removed!", Toast.LENGTH_SHORT).show();
                    manager.removeLens(index);
                    this.finish();
                }
                else
                    Toast.makeText(this, "please chose another lens.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_edit_lens:
                Intent i_AddLens = AddLens.makeLaunchIntent(CalculateDepthOfField.this, "switch to Lens saving!", index);
                startActivityForResult(i_AddLens, REQUEST_CODE_AddLENS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_AddLENS) {
            updateUIALL();
        }
    }

    // it is used to capture the information from each editText
    private void monitorEditText(EditText editText, int position) {
        // validCheck[0] - check inputDistance, validCheck[1] - check inputAperture;
        Lens_manager manager = Lens_manager.getInstance();
        Lens lens = manager.getByIndex(index);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
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

    @Override
    protected void onDestroy() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        super.onDestroy();
    }
}