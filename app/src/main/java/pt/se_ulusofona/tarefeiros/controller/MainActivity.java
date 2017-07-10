package pt.se_ulusofona.tarefeiros.controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pt.se_ulusofona.tarefeiros.R;
import pt.se_ulusofona.tarefeiros.model.Tarefa;
import pt.se_ulusofona.tarefeiros.utils.FragmentPageAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FragmentPageAdapter simpleFragmentPageAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);



        simpleFragmentPageAdapter = new FragmentPageAdapter(getSupportFragmentManager());
        simpleFragmentPageAdapter.addFragments(new FirstFragment());
        simpleFragmentPageAdapter.addFragments(new SecondFragment());
        simpleFragmentPageAdapter.addFragments(new ThirdFragment());

        viewPager.setAdapter(simpleFragmentPageAdapter);

        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.mipmap.user);
        tabLayout.getTabAt(1).setIcon(R.mipmap.search_task);
        tabLayout.getTabAt(2).setIcon(R.mipmap.edit_task);
    }


    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ThirdFragment.CODIGO_EDIT) { //podem haver varios codigos porque posso ter varios ActivityResult
            ThirdFragment thirdFragment = (ThirdFragment) simpleFragmentPageAdapter.getItem(2); //Vai buscar o thirdFragment
            Bundle bundle = data.getExtras();
            Tarefa tarefaRecebida = (Tarefa) bundle.getSerializable("tarefa");
            int posicao = bundle.getInt("position");
            thirdFragment.editarTarefa(tarefaRecebida, posicao);

        }
    }
}
