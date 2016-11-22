package com.example.frasse.passwordgenerator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void GenerateNewPassword(View view) {

        CheckBox dictionaryGeneration = (CheckBox) findViewById(R.id.DictionaryPass);
        EditText passwordField = (EditText) findViewById(R.id.Password);
        String password;

        if(dictionaryGeneration.isChecked()){
            int defaultLength = 3;
            password = generateDictionaryPassword(defaultLength);
        } else{
            int defaultLength = 12;
            password = generateNormalPassword(defaultLength);
        }

        passwordField.setText(password, TextView.BufferType.EDITABLE);

    }

    public String generateDictionaryPassword(int length){
        InputStream is = getResources().openRawResource(R.raw.words);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        ArrayList<String> dictionary = new ArrayList<String>();
        String word = null;
        StringBuilder password = new StringBuilder();

        try{
            while((word = br.readLine()) != null){
                word = word.substring(0, 1).toUpperCase() + word.substring(1);
                dictionary.add(word);
            }

            is.close();
            br.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        Random rand = new Random();

        for(int i = 0; i < length; i++){
            int randNum = rand.nextInt(dictionary.size());
            password.append(dictionary.get(randNum));
        }
        System.out.println(password);
        return password.toString();
    }
    public String generateNormalPassword(int length){
        StringBuilder password = new StringBuilder();

        return password.toString();

    }
}
