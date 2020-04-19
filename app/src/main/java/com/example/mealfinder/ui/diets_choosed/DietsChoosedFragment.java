package com.example.mealfinder.ui.diets_choosed;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealfinder.R;
import com.example.mealfinder.adapter.DietsAdapter;
import com.example.mealfinder.model.Diet;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DietsChoosedFragment extends Fragment {

    View root;
    RecyclerView diets_recycler_view;
    FloatingActionButton addDiet;
    ArrayList<String> diets_added = new ArrayList<>();
    ObservableSnapshotArray dietsReceived;
    ArrayList<String> dietsReceivedStr= new ArrayList<>();
    ArrayList<String> dietsNotInReceivedStr= new ArrayList<>();
    ArrayList<String> allDiets= new ArrayList<String>();
    ArrayList<String> dietsToShow= new ArrayList<>();
    private FirebaseFirestore mFirestore;
    private DietsAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_diets_choosed, container, false);
        diets_recycler_view = root.findViewById(R.id.diets_recycler_view);
        addDiet = root.findViewById(R.id.addDiet);
        allDiets.add("Vegan");
        allDiets.add("Vegetarian");
        allDiets.add("Paleo");
        allDiets.add("Macrobiotic");
        initFirestore();
        addDiet.setOnClickListener(view -> {
            //show dialog with diets available
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Choose your diets");
            mFirestore = FirebaseFirestore.getInstance();


            for (Object d: dietsReceived)
                dietsReceivedStr.add(d.toString());



            for (String s: allDiets){
                if (!dietsReceivedStr.contains(s)){
                    dietsToShow.add(s);
                }
            }
            Log.d("dietstoShow", dietsToShow.toString());
            final String[] diets = dietsToShow.toArray(new String[dietsToShow.size()]);

            builder.setMultiChoiceItems(diets, null, (dialog, which, isChecked) -> {
                // The user checked or unchecked a box
                if (isChecked) {
                    diets_added.add(diets[which]);
                    Log.d("diets", diets_added.toString());
                } else {
                    diets_added.remove(diets[which]);
                    Log.d("diets", diets_added.toString());
                }
            });
            // Add OK and Cancel buttons
            builder.setPositiveButton("OK", (dialog, which) -> {
                // The user clicked OK
                //send to firebase
                mFirestore = FirebaseFirestore.getInstance();
                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                CollectionReference dietsCol = mFirestore.collection("users").document(uid).collection("diets");
                for (String d : diets_added) {
                    dietsCol.add(new Diet(d));
                }
                dialog.dismiss();
                diets_added.clear();
                dietsReceivedStr.clear();
                dietsToShow.clear();
            });
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                diets_added.clear();
                dietsReceivedStr.clear();
                dietsToShow.clear();
            });

            // Create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();

        });


        return root;
    }

    private void initFirestore() {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        Query query = rootRef.collection("users").document(uid).collection("diets");
        FirestoreRecyclerOptions<Diet> options = new FirestoreRecyclerOptions.Builder<Diet>()
                .setQuery(query, Diet.class)
                .build();
        adapter = new DietsAdapter(options, getContext());
        dietsReceived=options.getSnapshots();
        adapter.notifyDataSetChanged();
        diets_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        diets_recycler_view.setAdapter(adapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(diets_recycler_view);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
