package com.example.mealfinder.ui.diets_choosed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealfinder.R;
import com.example.mealfinder.adapter.DietsAdapter;
import com.example.mealfinder.model.Diet;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DietsChoosedFragment extends Fragment {

    View root;
    private FirebaseFirestore mFirestore;
    private DietsAdapter adapter;
    RecyclerView diets_recycler_view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_diets_choosed, container, false);
        diets_recycler_view=root.findViewById(R.id.diets_recycler_view);
        initFirestore();

        return root;
    }

    private void initFirestore(){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = rootRef.collection("users").document(uid).collection("diets");
        FirestoreRecyclerOptions<Diet> options = new FirestoreRecyclerOptions.Builder<Diet>()
                .setQuery(query, Diet.class)
                .build();
        adapter= new DietsAdapter(options, getContext());
        adapter.notifyDataSetChanged();
        diets_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        diets_recycler_view.setAdapter(adapter);
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
