package com.example.mealfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class FirstTimeUsers extends AppCompatActivity {
    Button button;
    ArrayList<String> diets_saved=new ArrayList<>();
    GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_users);
        button=findViewById(R.id.button2);
        gridLayout=findViewById(R.id.gridLayout);

        setToogleEvent(gridLayout);

    }

    private void setToogleEvent( GridLayout gridLayout){
        for (int i=0; i<gridLayout.getChildCount();i++){
            final CardView cardView=(CardView) gridLayout.getChildAt(i);
            final int finalI=i;
            cardView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                   if (cardView.getCardBackgroundColor().getDefaultColor()==-1){
                       cardView.setCardBackgroundColor(Color.parseColor("#0F9D58"));
                   }
                   else{
                       cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                   }
                }
            });
        }
    }
    public void go_to_main(View view){
        Log.d("array", diets_saved.toString());
        Intent intent= new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
