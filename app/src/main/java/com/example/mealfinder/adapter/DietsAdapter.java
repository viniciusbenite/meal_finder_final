package com.example.mealfinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mealfinder.R;
import com.example.mealfinder.model.Diet;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DietsAdapter extends FirestoreRecyclerAdapter<Diet, DietsAdapter.DietsViewHolder> {

    private Context context;
    public DietsAdapter(@NonNull FirestoreRecyclerOptions<Diet> options, Context context) {
        super(options);
        this.context=context;
    }
    @Override
    public void onBindViewHolder(DietsViewHolder holder, int position, Diet model) {
        holder.dietName.setText(model.getDiet());
    }
    @Override
    public DietsViewHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.diets_item, group, false);
        return new DietsViewHolder(view);
    }
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    static class DietsViewHolder extends RecyclerView.ViewHolder{

        public final View mView;

        TextView dietName;

        DietsViewHolder(View itemView) {

            super(itemView);
            mView=itemView;
            dietName=mView.findViewById(R.id.diet_name);
        }
    }
}
