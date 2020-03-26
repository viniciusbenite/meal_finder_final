package com.example.mealfinder.ui.food_log;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealfinder.MyCustomDialog;
import com.example.mealfinder.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FoodLogFragment extends Fragment implements MyCustomDialog.OnInputSelected {
    FloatingActionButton addLog;
    private static final String TAG = "PrefsFragment";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_food_log, container, false);

        addLog=root.findViewById(R.id.addLog);
        addLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        return root;
    }

    private void showDialog(){
        MyCustomDialog dialog= new MyCustomDialog();
        dialog.setTargetFragment(FoodLogFragment.this, 1);
        dialog.show(getFragmentManager(), "MyCustomDialog");
    }

    @Override
    public void sendInput(String input) {
        Log.d(TAG, "found incoming input: "+input);

    }
}
