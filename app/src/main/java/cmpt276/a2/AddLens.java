package cmpt276.a2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddLens extends AppCompatActivity {
    private static final String EXTRA_MESSAGE = "Extra - message";

    private double focalLength, aperture;
    private String make;
    private EditText focalLengthInput;
    private EditText apertureInput;
    private EditText makeInput;

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

        // set up the "up" bottom
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

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
                Toast.makeText(this, "saved!", Toast.LENGTH_SHORT).show();
                make = makeInput.getText().toString();
                aperture = Double.valueOf(apertureInput.getText().toString());
                focalLength = Double.valueOf(focalLengthInput.getText().toString());
                addLensToManager();
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

    // add a lens to the exist manager object
    private void addLensToManager() {
        Lens_manager manager = Lens_manager.getInstance();
        manager.add(new Lens(make, aperture, focalLength));
    }
}