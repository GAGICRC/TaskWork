package pt.se_ulusofona.tarefeiros.model;

import android.net.Uri;

import java.io.Serializable;

public class Tarefa implements Serializable{
    private String descricaoBreve;
    private String descricaoCompleta;
    private String countryTarefa;
    private String cityTarefa;
    //Opcional
    private String taskPhotoUrl;
    private String remuneracao;
    private String user;

    public Tarefa() {
    }

    public Tarefa(String descricaoBreve, String descricaoCompleta, String countryTarefa, String cityTarefa, String remuneracao, String user) {
        this.descricaoBreve = descricaoBreve;
        this.descricaoCompleta = descricaoCompleta;
        this.cityTarefa = cityTarefa;
        this.countryTarefa = countryTarefa;
        this.remuneracao = remuneracao;
        this.user = user;
    }

    public String getDescricaoBreve() {
        return descricaoBreve;
    }
    public void setDescricaoBreve(String descricaoBreve) {
        this.descricaoBreve = descricaoBreve;
    }
    public String getDescricaoCompleta() {
        return descricaoCompleta;
    }
    public void setDescricaoCompleta(String descricaoCompleta) {
        this.descricaoCompleta = descricaoCompleta;
    }

    public String getCountryTarefa() {
        return countryTarefa;
    }
    public void setCountryTarefa(String countryTarefa) {
        this.countryTarefa = countryTarefa;
    }

    public String getCityTarefa() {
        return cityTarefa;
    }
    public void setCityTarefa(String cityTarefa) {
        this.cityTarefa = cityTarefa;
    }

    public String getTaskPhotoUrl() {
        return taskPhotoUrl;
    }
    public void setTaskPhotoUrl(String taskPhotoUrl) {
        this.taskPhotoUrl = taskPhotoUrl;
    }

    public String getRemuneracao() {
        return remuneracao;
    }
    public void setRemuneracao(String remuneracao) {
        this.remuneracao = remuneracao;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
}
