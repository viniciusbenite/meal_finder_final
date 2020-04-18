package com.example.mealfinder.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.mealfinder.LoginActivity;
import com.example.mealfinder.MainActivity;
import com.example.mealfinder.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    private GoogleApiClient mGoogleApiClient;
    private String mPhotoUrl;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        Button log_out = root.findViewById(R.id.log_out);
        ImageView profile_image = root.findViewById(R.id.profile_image);
        Button diets_choosed = root.findViewById(R.id.diets_choosed);
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        mGoogleApiClient = new GoogleApiClient.Builder(requireContext()) //Use app context to prevent leaks using activity
                //.enableAutoManage(this /* FragmentActivity */, connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        diets_choosed.setOnClickListener(v -> {
            //go to see diets saved in firestore
            NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.profile_to_diets_choosed);
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        assert mFirebaseUser != null;
        String mUsername = mFirebaseUser.getDisplayName();
        if (mFirebaseUser.getPhotoUrl() != null) {
            mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
        }
        ((MainActivity) requireActivity()).setActionBarTitle(mUsername);
        Glide.with(requireContext()).load(mPhotoUrl).into(profile_image);

        log_out.setOnClickListener(v -> {
            signOut();
            requireActivity().finish();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
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
