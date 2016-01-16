package com.bordag.legislative;

import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.app.ListActivity;
import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;


public class MainActivity extends Activity {

    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private BillDbAdapter mDbHelper;
    private Cursor mBillCursor;

    private ArrayList<String> mTitles;

    /**Called when the main activity is first created**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = new BillDbAdapter(this);
        mDbHelper.open();
        Log.v("test", "this is a test");
        fillData();
        //registerForContextMenu(getListView());
    }

    public void color(View v) {
        if (v.getTag() == "green") {
            v.setTag("red");
            v.setBackgroundColor(Color.RED);
        }
        else {
            v.setTag("green");
            v.setBackgroundColor(Color.GREEN);
        }
    }

    private void fillData() {
        //Instantiating XML parser
        Bill bill = new Bill();
        try {
            BillXMLParser aParser = new BillXMLParser(getResources().openRawResource(R.raw.introduced));

            mTitles = new ArrayList<String>();

            for (int i = 0; i < aParser.getNumOfBills(); i++) {
                bill.setTitle(aParser.getBillTitle(i));
                String title = bill.getTitle();
                Log.d("Asdf", title);
                mTitles.add(title.trim());
            }

            aParser = new BillXMLParser(getResources().openRawResource(R.raw.introduced2));

            for (int i = 0; i < aParser.getNumOfBills(); i++) {
                bill.setTitle(aParser.getBillTitle(i));
                String title = bill.getTitle();
                Log.d("Asdf", title);
                mTitles.add(title.trim());
            }

            ArrayAdapter<String> arrayAdapter =
                    new ArrayAdapter<String>(this,R.layout.bill_row, mTitles);

            ListView list = (ListView)findViewById(R.id.list);



            list.setAdapter(arrayAdapter);


            //System.out.println(title);
           // TextView cText = (TextView)findViewById(R.id.text2);
          //  cText.setText("Welcome to Know Your Congress App");
            //cText.setText(title);
        }
        catch(Exception error) {
            System.out.println(error.getMessage());
        }

        Log.d("Asdf", "end of block");
        String title = bill.getTitle();
        Log.d("Asdf", title);



        /*try {
            BillXMLParser aParser = new BillXMLParser(getResources().openRawResource(R.raw.introduced));
            Log.d("ASDF", "ASFDAS");
            Bill bill = new Bill();
            bill.setTitle(aParser.getBillTitle(1));

            String title = aParser.getBillTitle(1);
            Log.d("Asdf", title);
            //System.out.println(title);
            // TextView cText = (TextView)findViewById(R.id.text2);
            //  cText.setText("Welcome to Know Your Congress App");
            //cText.setText(title);
        }
        catch(Exception error) {
            System.out.println(error.getMessage());
        }*/

        // Get all of the rows from the database and create the list of bill
        mBillCursor = mDbHelper.fetchAllbills();
        startManagingCursor(mBillCursor);

        // Create an array to specify the fields we want to display in the list (only the TITLE of the bill)
        /*String[] from = new String[]{BillDbAdapter.KEY_TITLE};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1};

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.bill_row, mBillCursor, from, to);
        setListAdapter(notes); */









        //--------------------------- begin http request and json parsing

        DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
        HttpGet httppost = new HttpGet("http://api.nytimes.com/svc/politics/v3/us/legislative/congress/113/house/bills/introduced.json?api-key=c6ecb21102f048f8c5ab3a0e04ac43de:19:73976728");
        // Depends on your web service
        httppost.setHeader("Content-type", "application/json");

        InputStream inputStream = null;
        String result = null;

        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();


            inputStream = entity.getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();
            Log.v("test", result);
        } catch (Exception e) {
            // Oops
        }
        finally {
            try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
        }

    }










    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createBill();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, "Delete Bill");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deletebill(info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createBill() {
        Intent i = new Intent(this, BillEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    /*@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = mBillCursor;
        c.moveToPosition(position);
        Intent i = new Intent(this, BillEdit.class);
        i.putExtra(BillDbAdapter.KEY_ROWID, id);
        i.putExtra(BillDbAdapter.KEY_TITLE, c.getString(
                c.getColumnIndexOrThrow(BillDbAdapter.KEY_TITLE)));
        i.putExtra(BillDbAdapter.KEY_BODY, c.getString(
                c.getColumnIndexOrThrow(BillDbAdapter.KEY_BODY)));
        startActivityForResult(i, ACTIVITY_EDIT);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bundle extras = intent.getExtras();

        switch(requestCode) {
            case ACTIVITY_CREATE:
                String title = extras.getString(BillDbAdapter.KEY_TITLE);
                String body = extras.getString(BillDbAdapter.KEY_BODY);
                mDbHelper.createbill(title, body);
                fillData();
                break;
            case ACTIVITY_EDIT:
                Long mRowId = extras.getLong(BillDbAdapter.KEY_ROWID);
                if (mRowId != null) {
                    String editTitle = extras.getString(BillDbAdapter.KEY_TITLE);
                    String editBody = extras.getString(BillDbAdapter.KEY_BODY);
                    mDbHelper.updatebill(mRowId, editTitle, editBody);
                }
                fillData();
                break;
        }
        // TODO: fill in rest of method

    }
}
