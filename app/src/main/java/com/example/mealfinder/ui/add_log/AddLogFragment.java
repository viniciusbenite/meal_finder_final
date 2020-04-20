package com.example.mealfinder.ui.add_log;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealfinder.R;
import com.example.mealfinder.model.FoodLog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import static android.app.Activity.RESULT_OK;


public class AddLogFragment extends Fragment {
    TextView log_name;
    TextView meal_name;
    TextView date_log;
    Button submit;
    Button cancel;
    ImageView chooseImage;
    Calendar myCalendar;
    View view;

    private static final int ImageBack = 1;
    private FirebaseFirestore mFirestore;
    private StorageReference folder;
    private Uri imageData;
    private String picture;

    private Button takePictureButton;
    private Uri file;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_log, container, false);
        log_name=view.findViewById(R.id.choose_log_name);
        meal_name=view.findViewById(R.id.choose_meal_name);
        date_log=view.findViewById(R.id.choose_startDate);
        submit=view.findViewById(R.id.buttonSubmit);
        cancel=view.findViewById(R.id.buttonCancel);
        chooseImage=view.findViewById(R.id.choose_photo);
        myCalendar = Calendar.getInstance();
        folder= FirebaseStorage.getInstance().getReference().child("ImageFolder");

        takePictureButton = (Button) view.findViewById(R.id.button_image);

        FirebaseFirestore.setLoggingEnabled(true);
        initFirestore();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.e("ERROR", "No permissions granted");
            takePictureButton.setEnabled(false);
            requestPermissions(new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture(view);
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        date_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SEND TO FIREBASE

                String logName=log_name.getText().toString();
                String mealName=meal_name.getText().toString();
                String date=date_log.getText().toString();
                onSubmitClicked(new FoodLog(logName, picture, mealName, date));
                Toast.makeText(getContext(), "Log created", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(AddLogFragment.this).navigate(R.id.new_log_to_logs);
            }
        });
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, ImageBack);
            }
        });
        return view;
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        date_log.setText(sdf.format(myCalendar.getTime()));
    }

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    private void onSubmitClicked(FoodLog foodLog) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference food_logs = mFirestore.collection("users").document(uid).collection("food_logs");
        food_logs.add(foodLog);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if (requestCode == ImageBack){
            Log.d("User option", "Upload image");
            if (resultCode == RESULT_OK){
                imageData=data.getData();
                final StorageReference imageName=folder.child("image"+imageData.getLastPathSegment());
                imageName.putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                picture=String.valueOf(uri);
                            }
                        });
                    }
                });
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContext().getContentResolver().query(imageData,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                chooseImage.setImageBitmap(bitmap);
            }
        } else if (requestCode == 100){
            Log.d("User option", "Take picture");
            if (resultCode == RESULT_OK) {
                chooseImage.setImageURI(file);
//                file = data.getData();
                final StorageReference imageName = folder.child("image" + file.getLastPathSegment());
                imageName.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                picture = String.valueOf(uri);
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d("Request permission", "OK");
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Button enable", "OK");
                takePictureButton.setEnabled(true);
            }
        }
    }

    private void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = FileProvider.getUriForFile(getContext(), getActivity().getApplicationContext().getPackageName() + ".provider", getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        startActivityForResult(intent, 100);
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

}
