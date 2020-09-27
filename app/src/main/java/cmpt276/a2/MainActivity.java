package cmpt276.a2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private Lens_manager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set the toolbar's name
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Depth of Field Calculator");

        // initial the lens manger
        populateListView();

        // switch to another activity - AddLens
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
                Intent i = AddLens.makeLaunchIntent(MainActivity.this, "switch to save lens page!");
                startActivity(i);
        });
    }

    private void populateListView() {
        // some lenses are used to initial the lens manager
        Lens[] lenses = {
                (new Lens("Canon", 1.8, 50)),
                (new Lens("Tamron", 2.8, 90)),
                (new Lens("Sigma", 2.8, 200)),
                (new Lens("Nikon", 4, 200)),
                (new Lens("ElCheepo", 12, 24)),
                (new Lens("Leica", 5.6, 1600)),
                (new Lens("TheWide", 1.0, 16)),
                (new Lens("IWish", 1.0, 200)),
        };
        manager = Lens_manager.getInstance(lenses);
        ArrayAdapter<Lens> adapter = new ArrayAdapter<Lens>(this, R.layout.da_item, manager.getManager());
        ListView list = (ListView) findViewById(R.id.ListViewMain);
        list.setAdapter(adapter);
    }

}