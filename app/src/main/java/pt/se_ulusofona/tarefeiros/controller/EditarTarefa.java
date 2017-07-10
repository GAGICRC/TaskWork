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

import java.io.Serializable;

import pt.se_ulusofona.tarefeiros.R;
import pt.se_ulusofona.tarefeiros.model.Tarefa;

public class EditarTarefa extends AppCompatActivity implements View.OnClickListener, Serializable{

    private ImageView fotoTarefa;
    private EditText breveDescricao;
    private EditText descricaoCompleta;
    private EditText countryTarefa;
    private EditText cityTarefa;
    private EditText pagamento;
    private Button btGuardarTarefa;
    private Button btEliminarTarefa;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference imageRef;
    private StorageReference storageRef;

    private FirebaseDatabase database;
    private DatabaseReference tarefasdbRef;
    private int posicao;

    private static final int PICK_IMAGE = 101;

    private Uri imageUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_tarefa);

        breveDescricao = (EditText) findViewById(R.id.breve_descricao_edit);
        descricaoCompleta = (EditText) findViewById(R.id.descricao_completa_edit);
        cityTarefa = (EditText) findViewById(R.id.etCity);
        countryTarefa = (EditText) findViewById(R.id.etCountry);
        pagamento = (EditText) findViewById(R.id.pagammento_edit);

        btGuardarTarefa = (Button) findViewById(R.id.guardar_tarefa_edit);
        btGuardarTarefa.setOnClickListener(this);

        btEliminarTarefa = (Button) findViewById(R.id.eliminar_tarefa);
        btEliminarTarefa.setOnClickListener(this);

        fotoTarefa = (ImageView) findViewById(R.id.imagem_local_edit);
        fotoTarefa.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();
        imageRef = storageRef.child("UsersTarefas");

        Bundle bundle = getIntent().getExtras();
        Tarefa tarefaRecebida = (Tarefa) bundle.getSerializable("tarefa");
        posicao = bundle.getInt("position");

        breveDescricao.setText(tarefaRecebida.getDescricaoBreve());
        descricaoCompleta.setText(tarefaRecebida.getDescricaoCompleta());
        cityTarefa.setText(tarefaRecebida.getCityTarefa());
        countryTarefa.setText(tarefaRecebida.getCountryTarefa());
        pagamento.setText(tarefaRecebida.getRemuneracao());
        if(tarefaRecebida.getTaskPhotoUrl() != null){
            Picasso.with(getApplicationContext()).load(Uri.parse(tarefaRecebida.getTaskPhotoUrl())).fit().into(fotoTarefa);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btGuardarTarefa) {
            //Remover a tarefa antiga
            final Tarefa tarefa = new Tarefa(breveDescricao.getText().toString(), descricaoCompleta.getText().toString(), countryTarefa.getText().toString(), cityTarefa.getText().toString(), pagamento.getText().toString(), firebaseUser.getUid());
            if (breveDescricao.getText().toString().equals("")) {
                Toast.makeText(this, R.string.toast_edit_title, Toast.LENGTH_SHORT).show();
            } else if (descricaoCompleta.getText().toString().equals("")) {
                Toast.makeText(this, R.string.toast_edit_description, Toast.LENGTH_SHORT).show();
            } else if (countryTarefa.getText().toString().equals("")) {
                Toast.makeText(this, R.string.toast_edit_country, Toast.LENGTH_SHORT).show();
            } else if (cityTarefa.getText().toString().equals("")) {
                Toast.makeText(this, R.string.toast_edit_city, Toast.LENGTH_SHORT).show();
            }else if (pagamento.getText().toString().equals("") || !pagamento.getText().toString().equals("")) {
                pagamento.setText("");
                if(imageUri != null) {
                    imageRef.child(imageUri.getLastPathSegment()).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            tarefa.setTaskPhotoUrl(downloadUrl.toString());
                            Toast.makeText(getApplicationContext(), R.string.toast_edit_success, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("position", posicao);
                            bundle.putSerializable("tarefa", tarefa);
                            intent.putExtras(bundle);
                            setResult(ThirdFragment.CODIGO_EDIT, intent); //Associa a activity com o resultado
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
                Toast.makeText(getApplicationContext(), R.string.toast_edit_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", posicao);
                bundle.putSerializable("tarefa", tarefa);
                intent.putExtras(bundle);
                setResult(ThirdFragment.CODIGO_EDIT, intent); //Associa a activity com o resultado
                finish(); //acaba a activity
            }
        }
        if(v == btEliminarTarefa) {
            Tarefa tarefa = null;
            Intent intent = new Intent(this, MainActivity.class);
            Bundle bundleEditado = new Bundle();
            bundleEditado.putInt("position", posicao);
            bundleEditado.putSerializable("tarefa", tarefa);
            intent.putExtras(bundleEditado);
            setResult(ThirdFragment.CODIGO_EDIT, intent);
            finish();
        }
        if(v == fotoTarefa){
            openGallery();
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
            Picasso.with(getApplicationContext()).load(imageUri).fit().into(fotoTarefa);

        }
    }
}
