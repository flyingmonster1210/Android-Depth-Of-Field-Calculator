/**
 * CalculateDepthOfField.java
 * Weijie Zeng
 * 301379422
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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddLens extends AppCompatActivity {
    private static final String EXTRA_MESSAGE = "Extra - message";
    private static int index = -1;

    private double focalLength, aperture;
    private String make;
    private EditText focalLengthInput;
    private EditText apertureInput;
    private EditText makeInput;
    private boolean canSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lens);

        // set toolbar's name
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Lens Details");

        // initialize Intent i, and print information - "switch to Lens saving!"
        Intent i = getIntent();
        String message = i.getStringExtra(EXTRA_MESSAGE);
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // receive information from user
        makeInput = (EditText) findViewById(R.id.makeInput);
        apertureInput = (EditText) findViewById(R.id.apertureInput);
        focalLengthInput = (EditText) findViewById(R.id.focalLengthInput);
        makeInput.addTextChangedListener(assignCanSave);
        apertureInput.addTextChangedListener(assignCanSave);
        focalLengthInput.addTextChangedListener(assignCanSave);

        // set up the "up" bottom
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }
    // do inputs error checking
    private TextWatcher assignCanSave = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String strMake = makeInput.getText().toString().trim();
            String strAperture = apertureInput.getText().toString().trim();
            String strFocalLength = focalLengthInput.getText().toString().trim();
            if(strMake.length() <= 0 || strAperture.length() <= 0 || strFocalLength.length() <= 0) {
                canSave = false;
                return ;
            }
            double tAperture = Double.valueOf(apertureInput.getText().toString());
            double tFocalLength = Double.valueOf(focalLengthInput.getText().toString());
            if(tAperture >= 1.0 && tAperture <= 22 && tFocalLength > 0)
                canSave = true;
            else
                canSave = false;
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    // set up the toolbar - connect it to menu_add_lens.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_lens, menu);
        return true;
    }

    // functions that shown on the toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_data:
                if(canSave) {
                    Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                    make = makeInput.getText().toString();
                    aperture = Double.valueOf(apertureInput.getText().toString());
                    focalLength = Double.valueOf(focalLengthInput.getText().toString());
                    if(index < 0) addLensToManager();
                    else editLensInManager();
                }
                else {
                    Toast.makeText(this, "Cannot save with invalid inputs!", Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // interface for MainActivity to switch to AddLens activity
    public static Intent makeLaunchIntent(Context c, String message) {
        Intent intent = new Intent(c, AddLens.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        return intent;
    }
    public static Intent makeLaunchIntent(Context c, String message, int position) {
        Intent intent = new Intent(c, AddLens.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        index = position;
        return intent;
    }

    // add a lens to the exist manager object
    private void addLensToManager() {
        Lens_manager manager = Lens_manager.getInstance();
        manager.add(new Lens(make, aperture, focalLength));
    }

    private void editLensInManager() {
        Lens_manager manager = Lens_manager.getInstance();
        manager.getByIndex(index).setF_num(aperture);
        manager.getByIndex(index).setFocal_len(focalLength);
        manager.getByIndex(index).setMake(make);
    }

    private void setIndex(int position) {
        index = position;
    }

    @Override
    protected void onDestroy() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        super.onDestroy();
        setIndex(-1);
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
        this.finish();
    }


}