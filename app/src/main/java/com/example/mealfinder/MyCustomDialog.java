package com.example.mealfinder;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
public class MyCustomDialog extends DialogFragment {
    private static final String TAG="MyCustomDialog";

    public interface OnInputSelected{
        void sendInput(String input);
    }
    public OnInputSelected onInputSelected;
    TextView log_name;
    TextView meal_name;
    TextView startDate;
    Button submit;
    Button cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setTitle("New Log");
        View view = inflater.inflate(R.layout.add_log_dialog, container, false);
        log_name=view.findViewById(R.id.choose_log_name);
        meal_name=view.findViewById(R.id.choose_meal_name);
        startDate=view.findViewById(R.id.choose_startDate);
        submit=view.findViewById(R.id.buttonSubmit);
        cancel=view.findViewById(R.id.buttonCancel);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input=log_name.getText().toString();
                if (!input.equals("")){
                    onInputSelected.sendInput(input);

                }
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            onInputSelected=(OnInputSelected) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: "+e.getMessage());
        }
    }


}