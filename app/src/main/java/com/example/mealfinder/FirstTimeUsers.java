package com.example.mealfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

public class FirstTimeUsers extends AppCompatActivity {
    Button button;
    ArrayList<String> diets_saved=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_users);
        button=findViewById(R.id.button2);

    }

    public void onCheckboxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {

            case R.id.checkBox:
                 if (checked){
                     Toast.makeText(getApplicationContext(), "Vegan added", Toast.LENGTH_LONG);
                     diets_saved.add("Vegan");

                 }
                 else{
                     Toast.makeText(getApplicationContext(), "Vegan removed", Toast.LENGTH_LONG);
                     diets_saved.remove("Vegan");
                 }

                 break;
        case R.id.checkBox2:
                if (checked){
                    Toast.makeText(getApplicationContext(), "Vegetarian added", Toast.LENGTH_LONG);
                    diets_saved.add("Vegetarian");
                }

                else{
                    Toast.makeText(getApplicationContext(), "Vegetarian removed", Toast.LENGTH_LONG);
                    diets_saved.remove("Vegetarian");
                }

                break;

            case R.id.checkBox3:

                if (checked){
                    Toast.makeText(getApplicationContext(), "Macrobiotic added", Toast.LENGTH_LONG);
                    diets_saved.add("Macrobiotic");
                }

                else{
                    Toast.makeText(getApplicationContext(), "Macrobitotic removed", Toast.LENGTH_LONG);
                    diets_saved.remove("Macrobiotic");
                }

                break;

        }
    }
    public void go_to_main(View view){
        Log.d("array", diets_saved.toString());
        Intent intent= new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
