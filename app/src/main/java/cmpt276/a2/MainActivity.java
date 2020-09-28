package cmpt276.a2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

        // initialize the lens manger
        populateListView();
        registerClick();

        // switch to another activity - AddLens
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
                Intent i_AddLens = AddLens.makeLaunchIntent(MainActivity.this, "switch to Lens saving!");
                startActivity(i_AddLens);
        });
    }

    private void registerClick() {
        ListView list = (ListView) findViewById(R.id.ListViewMain);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i_Calculator = CalculateDepthOfField.makeLaunchIntent(MainActivity.this, "switch to the calculator!", position);
                startActivity(i_Calculator);
//                TextView textView = (TextView) view;
//                String message = "You click" + textView.getText().toString();
            }
        });
    }

    private void populateListView() {
        manager = Lens_manager.getInstance();
        ArrayAdapter<Lens> adapter = new ArrayAdapter<Lens>(this, R.layout.da_item, manager.getManager());
        ListView list = (ListView) findViewById(R.id.ListViewMain);
        list.setAdapter(adapter);
    }

}