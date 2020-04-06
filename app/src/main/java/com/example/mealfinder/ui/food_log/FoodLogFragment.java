package com.example.mealfinder.ui.food_log;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mealfinder.R;
import com.example.mealfinder.model.FoodLog;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.Reference;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class FoodLogFragment extends Fragment{
    RecyclerView food_logs;
    FloatingActionButton addLog;
    private FirebaseFirestore mFirestore;
    private FirestoreRecyclerAdapter adapter;

    private static final String TAG = "PrefsFragment";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_food_log, container, false);
        food_logs=root.findViewById(R.id.recycler_view_logs);
        initFirestore();

        addLog=root.findViewById(R.id.addLog);
        addLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GO to add new log fragment
                NavHostFragment.findNavController(FoodLogFragment.this).navigate(R.id.add_new_log);

            }
        });
        return root;
    }

    private void initFirestore(){
        food_logs.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Query query = rootRef.collection("food_logs")
                .orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<FoodLog> response = new FirestoreRecyclerOptions.Builder<FoodLog>()
                .setQuery(query, FoodLog.class)
                .build();

       adapter = new FirestoreRecyclerAdapter<FoodLog, FoodLogViewHolder>(response) {
            @Override
            public void onBindViewHolder(FoodLogViewHolder holder, int position, FoodLog model) {
                holder.logName.setText(model.getLogName());
                holder.mealName.setText(model.getMealName());
                holder.date.setText(model.getDate());
                Glide.with(getContext())
                        .load(model.getPicture())
                        .into(holder.foodLogImage);

            }
            @Override
            public FoodLogViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_food_log, group, false);
                return new FoodLogViewHolder(view);
            }
            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        food_logs.setLayoutManager(new LinearLayoutManager(getContext()));
        food_logs.setAdapter(adapter);
}


    static class FoodLogViewHolder extends RecyclerView.ViewHolder{

        public final View mView;

        ImageView foodLogImage;
        TextView logName;
        TextView mealName;
        TextView date;

        FoodLogViewHolder(View itemView) {

            super(itemView);
            mView=itemView;
            foodLogImage=mView.findViewById(R.id.food_log_item_image);
            logName=mView.findViewById(R.id.food_log_item_log_name);
            mealName=mView.findViewById(R.id.food_log_item_meal_name);
            date=mView.findViewById(R.id.food_log_item_date);
        }
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
