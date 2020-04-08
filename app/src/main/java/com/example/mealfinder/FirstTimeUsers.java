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

import com.example.mealfinder.model.Diet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FirstTimeUsers extends AppCompatActivity {
    Button submit;
    ArrayList<Diet> diets=new ArrayList<>();
    GridLayout gridLayout;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_users);
        submit=findViewById(R.id.submit);
        gridLayout=findViewById(R.id.gridLayout);
        initFirestore();
        setToogleEvent(gridLayout);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save to firestore the diets
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                CollectionReference diets_collection = mFirestore.collection("users").document(uid).collection("diets");
                for (Diet diet: diets)
                    diets_collection.add(diet);

                Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

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
                       if (finalI==0){
                           diets.add(new Diet("Vegan"));
                       }
                       else if (finalI==1){
                           diets.add(new Diet("Vegetarian"));
                       }
                       else if (finalI==2){
                           diets.add(new Diet("Macrobiotic"));
                       }
                       else if (finalI==3){
                           diets.add(new Diet("Paleo"));
                       }
                   }
                   else{
                       cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                       if (finalI==0){
                           diets.remove(new Diet("Vegan"));
                       }
                       else if (finalI==1){
                           diets.remove(new Diet("Vegetarian"));
                       }
                       else if (finalI==2){
                           diets.remove(new Diet("Macrobiotic"));
                       }
                       else if (finalI==3){
                           diets.remove(new Diet("Paleo"));
                       }
                   }
                }
            });
        }
    }

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

}
