package com.darrylfernandez.homeautomation;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpHandler extends AsyncTask<String, Void, String> {

    public OkHttpClient client = new OkHttpClient();
    public AsyncResponse delegate = null;

    public OkHttpHandler(){}

    @Override
    protected String doInBackground(String... params) {

        Request r = new Request.Builder()
                .url(params[0])
                .build();

        Response response = null;

        try {
            response = client.newCall(r).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response == null) return "";

        try {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(delegate != null) delegate.processFinish(s);
    }
}
