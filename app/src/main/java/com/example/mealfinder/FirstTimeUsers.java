package com.example.mealfinder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealfinder.model.Diet;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FirstTimeUsers extends AppCompatActivity {
    MaterialButton submit;
    // Has to be a set so the values don't repeat
    Set<Diet> diets = new HashSet<>();
    GridLayout gridLayout;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_users);
        submit = findViewById(R.id.submit);
        gridLayout = findViewById(R.id.gridLayout);
        initFirestore();
        setToogleEvent(gridLayout);

        submit.setOnClickListener(v -> {
            //save to firestore the diets
            String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            CollectionReference diets_collection = mFirestore.collection("users").document(uid).collection("diets");
            for (Diet diet : diets)
                diets_collection.add(diet);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

    }

    private void setToogleEvent(GridLayout gridLayout) {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            final MaterialCardView cardView = (MaterialCardView) gridLayout.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(v -> {
                if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {
                    cardView.setCardBackgroundColor(Color.parseColor("#0F9D58"));
                    if (finalI == 0) {
                        diets.add(new Diet("Vegan"));
                    } else if (finalI == 1) {
                        diets.add(new Diet("Vegetarian"));
                    } else if (finalI == 2) {
                        diets.add(new Diet("Macrobiotic"));
                    } else if (finalI == 3) {
                        diets.add(new Diet("Paleo"));
                    }
                } else {
                    cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    if (finalI == 0) {
                        diets.remove(new Diet("Vegan"));
                    } else if (finalI == 1) {
                        diets.remove(new Diet("Vegetarian"));
                    } else if (finalI == 2) {
                        diets.remove(new Diet("Macrobiotic"));
                    } else if (finalI == 3) {
                        diets.remove(new Diet("Paleo"));
                    }
                }
            });
        }
    }

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

}
