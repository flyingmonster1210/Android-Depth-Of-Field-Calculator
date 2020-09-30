package cmpt276.a2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Lens_manager manager;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set the toolbar's name
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Depth of Field Calculator");

        // initialize the lens list
        populateListView();
        // switch to another activity - CalculateDepthOfField
        registerClick();

        // switch to another activity - AddLens
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
                Intent i_AddLens = AddLens.makeLaunchIntent(MainActivity.this, "switch to Lens saving!");
                startActivity(i_AddLens);
        });

        // when the lens list is empty enable follow textView
        TextView emptyListInfo  = findViewById(R.id.emptyListInfo);
        TextView emptyListInfo2 = findViewById(R.id.emptyListInfo2);
        if(manager.getSize() <= 0) {
            emptyListInfo.setVisibility(View.VISIBLE);
            emptyListInfo2.setVisibility(View.VISIBLE);
        }
        else {
            emptyListInfo.setVisibility(View.GONE);
            emptyListInfo2.setVisibility(View.GONE);
        }

        //
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        loadDataBeforeLaunch();
    }

    private void saveDataBeforeTerminate() {
        // to save
        Gson gson = new Gson();
        String strObject;
        if(manager.getSize() > 0) {
            strObject = gson.toJson(manager);
            Toast.makeText(this, "save: strObject is not empty", Toast.LENGTH_SHORT).show();
        }
        else {
            strObject = "";
            Toast.makeText(this, "save: strObject is empty", Toast.LENGTH_SHORT).show();
        }
        editor.putString("last_lens_manager", strObject);
        editor.commit();
    }

    private void loadDataBeforeLaunch() {
        // to retrieve
        Gson gson = new Gson();
        String strObject = preferences.getString("last_lens_manager", "");
        if(strObject == null || strObject.equals("") || strObject.length() <= 0) {
            manager = Lens_manager.getInstance();
            manager.loadSimpleLenses();
            Toast.makeText(this, "load: strObject is empty", Toast.LENGTH_SHORT).show();
        }
        else {
            manager = Lens_manager.getInstance(gson.fromJson(strObject, Lens_manager.class));
            Toast.makeText(this, "load: strObject is not empty", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "count is " + Integer.toString(Lens_manager.getCount()), Toast.LENGTH_SHORT).show();
    }

    private void registerClick() {
        ListView list = (ListView) findViewById(R.id.ListViewMain);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i_Calculator = CalculateDepthOfField.makeLaunchIntent(MainActivity.this, "switch to the calculator!", position);
                startActivity(i_Calculator);
            }
        });
    }

    private void populateListView() {
        manager = Lens_manager.getInstance();
        ArrayAdapter<Lens> adapter = new ArrayAdapter<Lens>(this, R.layout.da_item, manager.getManager());
        ListView list = (ListView) findViewById(R.id.ListViewMain);
        list.setAdapter(adapter);
    }

    public void onDestroy() {
        saveDataBeforeTerminate();
        super.onDestroy();
//        System.exit(0);
    }
}