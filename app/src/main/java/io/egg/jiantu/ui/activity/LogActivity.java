package io.egg.jiantu.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

import io.egg.jiantu.R;
import io.egg.jiantu.util.Debug;
import io.egg.jiantu.util.Log;

public class LogActivity extends Activity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        mListView = (ListView) findViewById(R.id.log_list);

        List<String> data = Log.getLogs();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);

        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
