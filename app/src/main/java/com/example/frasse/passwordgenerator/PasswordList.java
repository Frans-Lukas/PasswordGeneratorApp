package com.example.frasse.passwordgenerator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Frans-Lukas on 11/22/2016.
 */

public class PasswordList extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> passwords;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_list);
        passwords = new ArrayList<>();
        try {
            FileInputStream inputStream = openFileInput("passwords.txt");
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String line;
            while((line = bufferedReader.readLine()) != null){
                passwords.add(line);
            }
            bufferedReader.close();
            streamReader.close();
            inputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        listView = (ListView) findViewById(R.id.passwordList);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, passwords);
        listView.setAdapter(adapter);
        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int pos, long id) {
                // ListView Clicked item index
                position = pos;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Are You Sure You Want To Remove This Password?");
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int itemPosition = position;

                        //clear file
                        String filename = "passwords.txt";
                        FileOutputStream outputStream;
                        try{
                            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                            outputStream.write("".getBytes());
                            outputStream.close();
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        // ListView Clicked item value
                        String itemValue = (String) listView.getItemAtPosition(position);
                        for(int i = 0; i < passwords.size(); i++){
                            if(!passwords.get(i).equals(itemValue)){
                                saveGivenPassword(passwords.get(i));
                            }
                        }
                        adapter.remove(adapter.getItem(position));
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();



            }

        });
    }
    public void saveGivenPassword(String password){
        String filename = "passwords.txt";
        FileOutputStream outputStream;
        try {
            String output = password + System.getProperty("line.separator");
            outputStream = openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write(output.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void goBackToMain(View view) {
        Intent intent = new Intent();
        setResult(RESULT_OK);
        finish();
    }
}
