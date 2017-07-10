package pt.se_ulusofona.tarefeiros.controller;


import android.content.Intent;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.io.Serializable;
import java.util.ArrayList;

import pt.se_ulusofona.tarefeiros.R;
import pt.se_ulusofona.tarefeiros.model.Tarefa;
import pt.se_ulusofona.tarefeiros.utils.MyTarefasAdapter;
import pt.se_ulusofona.tarefeiros.utils.RoundImages;

public class ThirdFragment extends Fragment implements View.OnClickListener, Serializable {
    private FirebaseDatabase database;
    private DatabaseReference tarefasRef;

    private View view;

    private ListView tarefasUser;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    private ArrayList<Tarefa> myTarefas;
    private ArrayList<String> dbRefs;

    private ImageView imageViewCriarNova;

    public static final String PREFS_NAME = "MyPrefsFile1";
    public static final int CODIGO_EDIT = 1001;
    public static final int CODIGO_SAVE = 1002;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.third_layout, container, false);

        imageViewCriarNova = (ImageView) view.findViewById(R.id.imageViewNovaTarefa);
        imageViewCriarNova.setOnClickListener(this);
        imageViewCriarNova.bringToFront();


        tarefasUser = (ListView) view.findViewById(R.id.tarefas_user);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        tarefasRef = database.getReference("Tarefas");
        tarefasRef.keepSynced(true);

        myTarefas = new ArrayList<>();
        dbRefs = new ArrayList<>();
        tarefasUser.setAdapter(new MyTarefasAdapter(getContext(), myTarefas));
        tarefasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((MyTarefasAdapter) tarefasUser.getAdapter()).clear();
                dbRefs.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Tarefa tarefa = child.getValue(Tarefa.class);
                    if (tarefa.getUser().equals(firebaseUser.getUid())) {
                        ((MyTarefasAdapter) tarefasUser.getAdapter()).add(tarefa); //Adiciona à lista que está associada ao Array, portanto adiciona ao myTarefas
                        dbRefs.add(child.getKey());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tarefasUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tarefa tarefaEscolhida = myTarefas.get(position);
                Intent intent = new Intent(getActivity(), EditarTarefa.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putSerializable("tarefa", tarefaEscolhida);
                intent.putExtras(bundle);
                startActivityForResult(intent, CODIGO_EDIT); //O MainActivity vai esperar receber algo quando o EditarTarefa fechar //Codigo serve p id o ActivityForResult
            }
        });

        return  view;
    }

    @Override
    public void onClick(View v) {
        if(v == imageViewCriarNova){
            Intent intent = new Intent(getActivity(), AdicionarTarefa.class);
            startActivity(intent);
        }
    }





    public void editarTarefa(Tarefa tarefa, int i) {
        if (tarefa == null) {
            DatabaseReference tarefaEliminar = database.getReference("Tarefas").child(dbRefs.get(i));
            tarefaEliminar.keepSynced(true);
            tarefaEliminar.removeValue();
        } else {
            DatabaseReference tarefaEditada = database.getReference("Tarefas").child(dbRefs.get(i)); //vai buscar a posição dele enviada do thirdFragment para o EditarTarefa para receber de novo aqui a partir do MainActivity
            tarefaEditada.keepSynced(true);
            tarefaEditada.setValue(tarefa); //SetValue dos valores editados
        }
    }

}