package pt.se_ulusofona.tarefeiros.model;

import android.icu.text.StringSearch;
import android.net.Uri;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

public class Perfil implements Serializable{
    private String nome;
    private String country; //https://docs.oracle.com/javase/7/docs/api/java/util/Locale.html
    private String photoUrl;

    private String phone;
    private String email;

    private String city;
    private String searchDiameter;

    public Perfil(){}

    public Perfil(String nome, String country, String city, String email, String phone, String searchDiameter) {
        this.nome = nome;
        this.country = country;
        this.phone = phone;
        this.email = email;
        this.city = city;
        this.searchDiameter = searchDiameter;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getSearchDiameter() {
        return searchDiameter;
    }
    public void setSearchDiameter(String searchDiameter) {
        this.searchDiameter = searchDiameter;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

}
