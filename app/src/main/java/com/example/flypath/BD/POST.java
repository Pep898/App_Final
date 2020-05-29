package com.example.flypath.BD;

import android.os.AsyncTask;

public class POST extends AsyncTask {
    String email,nom,passowrd;

    public POST(String email, String nom, String password) {
        this.email=email;
        this.nom=nom;
        this.passowrd=password;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}
