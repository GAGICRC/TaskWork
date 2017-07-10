package pt.se_ulusofona.tarefeiros.utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import pt.se_ulusofona.tarefeiros.R;
import pt.se_ulusofona.tarefeiros.model.Tarefa;

public class MyTarefasAdapter extends ArrayAdapter<Tarefa>{
    private Context context;

    public MyTarefasAdapter(@NonNull Context context, ArrayList<Tarefa> myTarefas) {
        super(context, 0, myTarefas);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Tarefa tarefa = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.virgula = (TextView) convertView.findViewById(R.id.virgula);
            viewHolder.tarefaDescricao = (TextView) convertView.findViewById(R.id.tvTask);
            viewHolder.cityTarefa = (TextView) convertView.findViewById(R.id.tvCity);
            viewHolder.countryTarefa = (TextView) convertView.findViewById(R.id.tvCountry);
            viewHolder.taskPhoto = (ImageView) convertView.findViewById(R.id.ivTaskPhoto);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tarefaDescricao.setText(tarefa.getDescricaoBreve());
        viewHolder.countryTarefa.setText(tarefa.getCountryTarefa());
        viewHolder.cityTarefa.setText(tarefa.getCityTarefa());
        if(tarefa.getTaskPhotoUrl() != null){
            Picasso.with(getContext()).load(Uri.parse(tarefa.getTaskPhotoUrl())).fit().into(viewHolder.taskPhoto);
        }

        if(viewHolder.countryTarefa.getText().toString().equals("") && viewHolder.cityTarefa.getText().toString().equals("")){
            viewHolder.virgula.setVisibility(View.GONE);
        }
        return convertView;
    }
    private class ViewHolder {
        private TextView tarefaDescricao;
        private TextView countryTarefa;
        private TextView cityTarefa;
        private TextView virgula;
        private ImageView taskPhoto;
    }
}
