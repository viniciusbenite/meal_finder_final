package com.example.mealfinder.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.bumptech.glide.Glide;
import com.example.mealfinder.LoginActivity;
import com.example.mealfinder.MainActivity;
import com.example.mealfinder.R;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.mealfinder.MainActivity.ANONYMOUS;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class ProfileFragment extends Fragment {
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    String mUsername;
    String mPhotoUrl;
    Button diets_choosed;
    Button log_out;
    ImageView profile_image;
    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        log_out=root.findViewById(R.id.log_out);
        profile_image=root.findViewById(R.id.profile_image);
        diets_choosed=root.findViewById(R.id.diets_choosed);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext()) //Use app context to prevent leaks using activity
                //.enableAutoManage(this /* FragmentActivity */, connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        diets_choosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to see diets saved in firestore
                NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.profile_to_diets_choosed);
            }
        });
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mUsername = mFirebaseUser.getDisplayName();
        if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
        }
        ((MainActivity) getActivity()).setActionBarTitle(mUsername);
        Glide.with(getContext()).load(mPhotoUrl).into(profile_image);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signOut();
                getActivity().finish();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void signOut() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

}
