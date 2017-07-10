package pt.se_ulusofona.tarefeiros.controller;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import pt.se_ulusofona.tarefeiros.R;
import pt.se_ulusofona.tarefeiros.model.Tarefa;
import pt.se_ulusofona.tarefeiros.utils.MyTarefasAdapter;


public class SecondFragment extends Fragment {

    private View view;

    private ListView listaTarefas;

    private FirebaseDatabase database;
    private DatabaseReference tarefasRef;


    private ArrayList<Tarefa> tarefasAll;
    private ArrayList<String> dbRefs;

    private EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.second_layout, container, false);

        listaTarefas = (ListView) view.findViewById(R.id.lista_tarefas);

        editText = (EditText) view.findViewById(R.id.motor_pesquisa);
        editText.setSelected(false);

        database = FirebaseDatabase.getInstance();
        tarefasRef = database.getReference("Tarefas");
        tarefasRef.keepSynced(true);



        //Search View
        //Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        /*setSupportView(toolbar);*/
        //getSupportActionBar().setTitle("Material Search");
        /*toolbar.setTitleTextColor();

        MaterialSearchView searchView = (MaterialSearchView) view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
*/

        tarefasAll = new ArrayList<>();
        dbRefs = new ArrayList<>();
        MyTarefasAdapter myTarefasAdapter = new MyTarefasAdapter(getContext(), tarefasAll);
        listaTarefas.setAdapter(myTarefasAdapter);
        //Simplicação de código acima
        //listaTarefas.setAdapter(new MyTarefasAdapter(getContext(), tarefasAll));
        tarefasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Tarefa tarefa = child.getValue(Tarefa.class);
                    if(dbRefs.contains(child.getKey())) {
                        tarefasAll.set(dbRefs.indexOf(child.getKey()), tarefa);
                        ((MyTarefasAdapter) listaTarefas.getAdapter()).notifyDataSetChanged();
                    } else {
                        ((MyTarefasAdapter) listaTarefas.getAdapter()).add(tarefa);
                        dbRefs.add(child.getKey());
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



                listaTarefas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Tarefa tarefaEscolhida = tarefasAll.get(position);
                        Intent intent = new Intent(getActivity(), VerTarefa.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("tarefa", tarefaEscolhida);
                        intent.putExtras(bundle);
                        startActivity(intent);
            }
        });

        //Motor de pesquisa
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    /*public boolean onCreateOptionMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    //Fechar a procura quando der backPress
    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }*/
}