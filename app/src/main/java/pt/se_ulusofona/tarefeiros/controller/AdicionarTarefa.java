package pt.se_ulusofona.tarefeiros.controller;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.security.Timestamp;

import pt.se_ulusofona.tarefeiros.R;
import pt.se_ulusofona.tarefeiros.model.Tarefa;

public class AdicionarTarefa extends AppCompatActivity implements View.OnClickListener{

    private ImageView fotoTarefa;
    private EditText breveDescricao;
    private EditText descricaoCompleta;
    private EditText countryTarefa;
    private EditText cityTarefa;
    private EditText pagamento;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference tarefasRef;

    private FirebaseStorage firebaseStorage;
    private StorageReference imageRef;
    private StorageReference storageReference;

    private Button buttonApagar;
    private Button btGuardarTarefa;

    private Uri imageUri;

    public static final int CODIGO_SAVE = 1002;

    private static final int PICK_IMAGE = 101;

    private Timestamp timestamp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_tarefa);


        breveDescricao = (EditText) findViewById(R.id.breve_descricao_edit);
        descricaoCompleta = (EditText) findViewById(R.id.descricao_completa_edit);
        cityTarefa = (EditText) findViewById(R.id.etCity);
        countryTarefa = (EditText) findViewById(R.id.etCountry);
        pagamento = (EditText) findViewById(R.id.pagammento_edit);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        tarefasRef = database.getReference("Tarefas");
        tarefasRef.keepSynced(true);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        imageRef = storageReference.child("UsersTarefas");


        buttonApagar = (Button) findViewById(R.id.eliminar_tarefa);
        buttonApagar.setVisibility(View.GONE);

        btGuardarTarefa = (Button) findViewById(R.id.guardar_tarefa_edit);
        btGuardarTarefa.setOnClickListener(this);

        fotoTarefa = (ImageView) findViewById(R.id.imagem_local_edit);
        fotoTarefa.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if(v == fotoTarefa){
            openGallery();
        }
        if(v == btGuardarTarefa) {


            final Tarefa tarefa = new Tarefa(breveDescricao.getText().toString(), descricaoCompleta.getText().toString(), countryTarefa.getText().toString(), cityTarefa.getText().toString(), pagamento.getText().toString(), firebaseUser.getUid());
            final DatabaseReference tarefasdbrefKey = tarefasRef.push();
            if (breveDescricao.getText().toString().equals("")) {
                Toast.makeText(AdicionarTarefa.this, R.string.toast_edit_title, Toast.LENGTH_SHORT).show();
            } else if (descricaoCompleta.getText().toString().equals("")) {
                Toast.makeText(AdicionarTarefa.this, R.string.toast_edit_description, Toast.LENGTH_SHORT).show();
            } else if (countryTarefa.getText().toString().equals("")) {
                Toast.makeText(AdicionarTarefa.this, R.string.toast_edit_country, Toast.LENGTH_SHORT).show();
            } else if (cityTarefa.getText().toString().equals("")) {
                Toast.makeText(AdicionarTarefa.this, R.string.toast_edit_city, Toast.LENGTH_SHORT).show();
            }else if (pagamento.getText().toString().equals("") || !pagamento.getText().toString().equals("")) {
                pagamento.setText("");
                if(imageUri != null) {
                    imageRef.child(imageUri.getLastPathSegment()).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            tarefa.setTaskPhotoUrl(downloadUrl.toString());
                            tarefasdbrefKey.setValue(tarefa);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
                tarefasdbrefKey.setValue(tarefa);
                Toast.makeText(AdicionarTarefa.this, R.string.toast_edit_success, Toast.LENGTH_SHORT).show();
                finish();
            }

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
            Picasso.with(this).load(imageUri).into(fotoTarefa);

        }
    }
}
