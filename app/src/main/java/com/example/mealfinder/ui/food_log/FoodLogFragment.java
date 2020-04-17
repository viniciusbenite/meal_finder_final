package com.example.mealfinder.ui.food_log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealfinder.R;
import com.example.mealfinder.adapter.FoodLogAdapter;
import com.example.mealfinder.model.FoodLog;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FoodLogFragment extends Fragment {
    private static final String TAG = "PrefsFragment";
    RecyclerView food_logs;
    FloatingActionButton addLog;
    private FirebaseFirestore mFirestore;
    private FoodLogAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_food_log, container, false);
        food_logs = root.findViewById(R.id.recycler_view_logs);
        initFirestore();

        addLog = root.findViewById(R.id.addLog);
        addLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GO to add new log fragment
                NavHostFragment.findNavController(FoodLogFragment.this).navigate(R.id.add_new_log);

            }
        });

        return root;
    }

    private void initFirestore() {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = rootRef.collection("users").document(uid).collection("food_logs")
                .orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<FoodLog> options = new FirestoreRecyclerOptions.Builder<FoodLog>()
                .setQuery(query, FoodLog.class)
                .build();

        adapter = new FoodLogAdapter(options, getContext());
        adapter.notifyDataSetChanged();
        food_logs.setLayoutManager(new LinearLayoutManager(getContext()));
        food_logs.setAdapter(adapter);
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
        }).attachToRecyclerView(food_logs);
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
