package com.example.frasse.passwordgenerator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String passwordKey = "";
    private ArrayList<String> dictionary = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ((RadioButton) findViewById(R.id.randomWordsRadio)).toggle();
        disableCheckBoxes(findViewById(R.id.randomLettersRadio));
        InputStream is = getResources().openRawResource(R.raw.words);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String word;
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
    }

    //button generatePassButton calls this function to generate a new password.
    public void GenerateNewPassword(View view) {
        RadioButton passByWordRb = (RadioButton) findViewById(R.id.randomWordsRadio);
        RadioButton passByLettersRb= (RadioButton) findViewById(R.id.randomLettersRadio);
        EditText passwordField = (EditText) findViewById(R.id.password);
        String password = new String();

        boolean upperCaseAllowed = ((CheckBox) findViewById(R.id.upperCharsCheckBox)).isChecked();
        boolean specialAllowed = ((CheckBox) findViewById(R.id.specialCharsCheckBox)).isChecked();
        boolean numbersAllowed = ((CheckBox) findViewById(R.id.numberCharsCheckBox)).isChecked();

        if(passByWordRb.isChecked()){
            int defaultLength = Integer.parseInt(((EditText) findViewById(R.id.numOfWordsEdit)).getText().toString());
            password = generateDictionaryPassword(defaultLength);
        } else if (passByLettersRb.isChecked()){
            int defaultLength = Integer.parseInt(((EditText) findViewById(R.id.numOfLettersEdit)).getText().toString());
            password = generateNormalPassword(defaultLength, numbersAllowed, upperCaseAllowed, specialAllowed);
        }
        passwordField.setText(password, TextView.BufferType.EDITABLE);

    }
    //function to generate random password by adding words together.
    public String generateDictionaryPassword(int length){
        StringBuilder password = new StringBuilder();
        Random rand = new Random();
        for(int i = 0; i < length; i++){
            int randNum = rand.nextInt(dictionary.size());
            password.append(dictionary.get(randNum));
        }
        return password.toString();
    }
    //Function for generation random character password.
    public String generateNormalPassword(int length, boolean numbersAllowed,
                                         boolean uppercaseAllowed, boolean othersAllowed){

        StringBuilder password = new StringBuilder();
        ArrayList<Character> totalCharacters = new ArrayList<>();
        char letters[] = {'a','b','c','d','e','f','g','h','i','j','k','l',
                'm','n','o','p','q','r','s','t','u','v','w','x','y','z'};

        char numbers[] = {'0','1','2','3','4','5','6','7','8','9'};
        char others[] = {'&','%','$','"','#','!','?'};

        for(int i = 0; i < letters.length; i++){
            totalCharacters.add(letters[i]);
        }
        if(numbersAllowed){
            for(int i = 0; i < numbers.length; i++){
                totalCharacters.add(numbers[i]);
            }
        }
        if(othersAllowed){
            for(int i = 0; i < others.length; i++){
                totalCharacters.add(others[i]);
            }
        }
        for(int i = 0; i < length; i++){
            Random rand = new Random();
            int index = rand.nextInt(totalCharacters.size());
            char charToAdd = totalCharacters.get(index);
            if(Character.isLetter(charToAdd) && uppercaseAllowed){
                if(rand.nextInt(2) == 1){
                    charToAdd = Character.toUpperCase(charToAdd);
                }
            }
            password.append(charToAdd);
        }

        return password.toString();
    }

    public void savePassword(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tag The Password");
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                passwordKey = input.getText().toString();
                savePasswordWithKey(passwordKey);
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

    public void savePasswordWithKey(String key){
        String passwordToSave = ((EditText) findViewById(R.id.password)).getText().toString();
        String filename = "passwords.txt";
        FileOutputStream outputStream;
        try {
            String output = key + ":" + passwordToSave + System.getProperty("line.separator");
            outputStream = openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write(output.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disableCheckBoxes(View view) {
        ((CheckBox)findViewById(R.id.numberCharsCheckBox)).setEnabled(false);
        ((CheckBox)findViewById(R.id.upperCharsCheckBox)).setEnabled(false);
        ((CheckBox)findViewById(R.id.specialCharsCheckBox)).setEnabled(false);
        enableNumOfWordsInput();
        disableNumOfLettersInput();
    }

    public void enableCheckBoxes(View view) {
        ((CheckBox)findViewById(R.id.numberCharsCheckBox)).setEnabled(true);
        ((CheckBox)findViewById(R.id.upperCharsCheckBox)).setEnabled(true);
        ((CheckBox)findViewById(R.id.specialCharsCheckBox)).setEnabled(true);
        disableNumOfWordsInput();
        enableNumOfLettersInput();
    }
    public void disableNumOfWordsInput(){
        ((EditText)findViewById(R.id.numOfWordsEdit)).setEnabled(false);
        ((TextView)findViewById(R.id.numOfWordsText)).setEnabled(false);
    }
    public void enableNumOfWordsInput(){
        ((EditText)findViewById(R.id.numOfWordsEdit)).setEnabled(true);
        ((TextView)findViewById(R.id.numOfWordsText)).setEnabled(true);
    }
    public void disableNumOfLettersInput(){
        ((EditText)findViewById(R.id.numOfLettersEdit)).setEnabled(false);
        ((TextView)findViewById(R.id.numOfLettersText)).setEnabled(false);
    }
    public void enableNumOfLettersInput(){
        ((EditText)findViewById(R.id.numOfLettersEdit)).setEnabled(true);
        ((TextView)findViewById(R.id.numOfLettersText)).setEnabled(true);
    }

    public void editNumWords(View view) {
        ((EditText)findViewById(R.id.numOfWordsEdit)).setText("");
    }

    public void editNumLetters(View view) {
        ((EditText)findViewById(R.id.numOfLettersEdit)).setText("");
    }

    public void goToShowPasswords(View view) {
        Intent intentToGoSeePasswords = new Intent(view.getContext(), PasswordList.class);
        startActivityForResult(intentToGoSeePasswords, 0);
    }
}
