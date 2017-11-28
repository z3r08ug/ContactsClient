package dev.uublabs.contactsclient;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private static final String TAG = MainActivity.class.getSimpleName() + "_TAG";
    private Cursor cursor;
    private List<Contact> contacts;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvContacts);
        Uri uri = Uri.parse("content://dev.uublabs.contacts/contacts");
        ContentProviderClient contentProviderClient = getContentResolver().acquireContentProviderClient(uri);

        contacts = new ArrayList<>();

        try
        {
            cursor = contentProviderClient.query(uri, null, null, null, null);

        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }

        if(cursor.moveToFirst())
        {
            Log.d(TAG, "onCreate: move to first contact");
            do
            {
                Contact contact = new Contact(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4));
                contacts.add(contact);
            }
            while (cursor.moveToNext());
        }



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(contacts);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(recyclerAdapter);

        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed},new ColorDrawable(Color.RED));
        drawable.addState(new int[]{}, new ColorDrawable(Color.GREEN));

        Button button = findViewById(R.id.btnClick);
        button.setBackground(drawable);
    }

    public void clickMe(View view)
    {
//        Toast.makeText(this, "That tickles!", Toast.LENGTH_SHORT).show();
    }
}
