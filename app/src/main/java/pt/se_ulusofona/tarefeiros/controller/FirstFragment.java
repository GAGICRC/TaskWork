package pt.se_ulusofona.tarefeiros.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import pt.se_ulusofona.tarefeiros.R;
import pt.se_ulusofona.tarefeiros.model.Perfil;

import static android.app.Activity.RESULT_OK;


public class FirstFragment extends Fragment implements View.OnClickListener, Serializable{

    View view;

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference photoRef;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageRef;
    private StorageReference imageRef;


    private EditText etName;
    private EditText etEmail;
    private EditText etCountry;
    private EditText etCity;
    private EditText etPhone;
    private EditText etSearchDiameter;
    private ImageView ivPhotoUrl;

    private Button btEditProfile;
    private Button btSave;
    private Button buttonLogout;

    private static final int PICK_IMAGE = 101;

    private Uri imageUri;
    private String photoUrl;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.first_layout, container, false);

        //calling button and clicklistener
        btEditProfile = (Button) view.findViewById(R.id.btEdit);
        ivPhotoUrl = (ImageView)  view.findViewById(R.id.ivPhotoUrl);
        btSave = (Button) view.findViewById(R.id.btSave);
        buttonLogout = (Button) view.findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(this);


        btSave.setOnClickListener(this);
        btEditProfile.setOnClickListener(this);

        etName = (EditText) view.findViewById(R.id.etNome);
        etEmail = (EditText)view.findViewById(R.id.etEmail) ;
        etCountry = (EditText) view.findViewById(R.id.etCountry);
        etCity = (EditText) view.findViewById(R.id.etCity);
        etPhone = (EditText) view.findViewById(R.id.etPhone);
        etSearchDiameter = (EditText) view.findViewById(R.id.etSearchDiameter);


        etName.setEnabled(false);
        etEmail.setEnabled(false);
        etCountry.setEnabled(false);
        etCity.setEnabled(false);
        etPhone.setEnabled(false);
        etSearchDiameter.setEnabled(false);
        btSave.setVisibility(View.GONE);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser =  firebaseAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();

        imageRef = storageRef.child("UsersPhotos").child(firebaseUser.getUid());


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());
        databaseReference.keepSynced(true);
        photoRef = databaseReference.child("photoURL");

        databaseReference.child("email").setValue(firebaseUser.getEmail());
        etEmail.setText(firebaseUser.getEmail());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Perfil userProfile = dataSnapshot.getValue(Perfil.class);
                etName.setText(userProfile.getNome());
                etCountry.setText(userProfile.getCountry());
                etCity.setText(userProfile.getCity());
                etPhone.setText(userProfile.getPhone());
                etSearchDiameter.setText(userProfile.getSearchDiameter());
                if(userProfile.getPhotoUrl() != null) {
                    Picasso.with(getContext()).load(Uri.parse(userProfile.getPhotoUrl())).fit().into(ivPhotoUrl);
                    Log.d("TAG", "onDataChange: "+ userProfile.getPhotoUrl());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == buttonLogout) {
            firebaseAuth.signOut();
            Intent i = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }
        if(v == ivPhotoUrl){
            openGallery();
        }
        if(v == btEditProfile){
            ivPhotoUrl.setOnClickListener(this);
            if(v == ivPhotoUrl){
                openGallery();
            }
            ivPhotoUrl.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            etName.setEnabled(true);
            etName.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            etCountry.setEnabled(true);
            etCountry.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            etCity.setEnabled(true);
            etCity.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            etPhone.setEnabled(true);
            etPhone.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            etSearchDiameter.setEnabled(true);
            etSearchDiameter.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            btSave.setVisibility(View.VISIBLE);
            btEditProfile.setVisibility(View.GONE);
            buttonLogout.setVisibility(View.GONE);
            Toast.makeText(getContext(), R.string.toast_edit, Toast.LENGTH_LONG).show();
        }
        if(v == btSave){
            final Perfil userProfile = new Perfil(etName.getText().toString(), etCountry.getText().toString(), etCity.getText().toString(), etEmail.getText().toString(), etPhone.getText().toString(), etSearchDiameter.getText().toString());

            if(imageUri != null) {
                imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        userProfile.setPhotoUrl(downloadUrl.toString());
                        databaseReference.setValue(userProfile);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }



            databaseReference.setValue(userProfile);
            ivPhotoUrl.setOnClickListener(null);
            ivPhotoUrl.setBackgroundResource(android.R.color.transparent);
            etName.setEnabled(false);
            etName.setBackgroundResource(android.R.color.transparent);
            etCountry.setEnabled(false);
            etCountry.setBackgroundResource(android.R.color.transparent);
            etCity.setEnabled(false);
            etCity.setBackgroundResource(android.R.color.transparent);
            etPhone.setEnabled(false);
            etPhone.setBackgroundResource(android.R.color.transparent);
            etSearchDiameter.setEnabled(false);
            etSearchDiameter.setBackgroundResource(android.R.color.transparent);
            btSave.setVisibility(View.GONE);
            btEditProfile.setVisibility(View.VISIBLE);
            buttonLogout.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), R.string.toast_edit_success, Toast.LENGTH_LONG).show();
        }
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
                imageUri = data.getData();
                Log.d("TAG", "onActivityResult: "+ imageUri);
                Picasso.with(getContext()).load(imageUri).fit().into(ivPhotoUrl);

            }
    }

}
