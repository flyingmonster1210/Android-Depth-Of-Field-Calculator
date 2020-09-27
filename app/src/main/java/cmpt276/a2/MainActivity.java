package cmpt276.a2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private Lens_manager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        populateListView();
//        FloatingActionButton fab = findViewById(R.id.)
    }

    private void populateListView() {
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