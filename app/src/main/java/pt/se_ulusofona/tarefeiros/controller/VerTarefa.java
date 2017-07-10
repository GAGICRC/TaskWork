package pt.se_ulusofona.tarefeiros.controller;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import pt.se_ulusofona.tarefeiros.R;
import pt.se_ulusofona.tarefeiros.model.Perfil;
import pt.se_ulusofona.tarefeiros.model.Tarefa;

public class VerTarefa extends AppCompatActivity {

    private ImageView ivUserPhoto;
    private ImageView ivTaskPhoto;
    private TextView tvTaskTitle;
    private TextView tvUserName;
    private TextView tvDescription;
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvCountry;
    private TextView tvCity;
    private TextView tvPayment;

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference emailReference;

    private FirebaseDatabase database;
    private DatabaseReference userRef;

    private Tarefa tarefaRecebida;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_tarefa);

        database = FirebaseDatabase.getInstance();

        ivUserPhoto = (ImageView) findViewById(R.id.ivUserPhoto);
        ivTaskPhoto = (ImageView) findViewById(R.id.ivTaskPhoto);
        tvTaskTitle = (TextView) findViewById(R.id.tvTaskTitle);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvCountry = (TextView) findViewById(R.id.tvCountry);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvPayment = (TextView) findViewById(R.id.tvPayment);

        Bundle bundle = getIntent().getExtras();
        tarefaRecebida = (Tarefa) bundle.getSerializable("tarefa");


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser =  firebaseAuth.getCurrentUser();


        userRef = database.getReference("Users");
        userRef.keepSynced(true);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(tarefaRecebida.getUser())){
                    Perfil userProfile = dataSnapshot.child(tarefaRecebida.getUser()).getValue(Perfil.class);
                    tvEmail.setText(userProfile.getEmail());
                    tvUserName.setText(userProfile.getNome());
                    tvPhone.setText(userProfile.getPhone());
                    if(userProfile.getPhotoUrl() != null){
                        Picasso.with(getApplicationContext()).load(Uri.parse(userProfile.getPhotoUrl())).fit().into(ivUserPhoto);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        
        tvTaskTitle.setText(tarefaRecebida.getDescricaoBreve());
        tvDescription.setText(tarefaRecebida.getDescricaoCompleta());
        tvCountry.setText(tarefaRecebida.getCountryTarefa());
        tvCity.setText(tarefaRecebida.getCityTarefa());
        tvPayment.setText(tarefaRecebida.getRemuneracao());
        if(tarefaRecebida.getTaskPhotoUrl() != null){
            Picasso.with(getApplicationContext()).load(Uri.parse(tarefaRecebida.getTaskPhotoUrl())).fit().into(ivTaskPhoto);

        }
    }


}
