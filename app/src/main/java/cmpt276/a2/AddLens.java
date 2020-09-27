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
    private double focalLength, aperture;
    private String make;

    private EditText focalLengthInput;
    private EditText apertureInput;
    private EditText makeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lens);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Lens Details");

        Intent i = getIntent();
        String message = i.getStringExtra("Extra - message");
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        makeInput = (EditText) findViewById(R.id.makeInput);
        apertureInput = (EditText) findViewById(R.id.apertureInput);
        focalLengthInput = (EditText) findViewById(R.id.focalLengthInput);
//        Log.i("AddLens", String.valueOf(aperture));

        // the up bottom
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_lens, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_data:
                Toast.makeText(this, "saving!", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
//    public static Intent makeLaunchIntent(Context c, String message) {
//        Intent intent = new Intent(c, AddLens.class);
//        intent.putExtra("Extra - message", message);
//        return intent;
//    }

    public static Intent makeLaunchIntent(Context c, String message) {
        Intent intent = new Intent(c, AddLens.class);
        intent.putExtra("Extra - message", message);
        return intent;
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddLens.class);
    }
}