package com.multimarca.tae.voceadorestae.wservices;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Erick Samaniego
 * Company Bordev Software Workshop && cometa.tech
 */
public class RestApiClientFile extends AsyncTask<String,String,String> {

    METHOD MethodType;
    String MethodName;
    String Url;
    Object Params;
    HttpURLConnection urlConnection;
    String TAG = getClass().getName();
    Bitmap bitmap;
    private final  RestInterface RestInterface;
    public enum METHOD {
        POST,PUT,GET,DELETE
    }

    /**
     * Especific every param to send for the rest api
     * @param Url Rest url http://api.dominio.com/
     * @param MethodName Rest Method name users
     * @param Params Rest params can be Variable, JsonObject or List
     * @param Method Rest Method type POST, GET, PUT, DELETE the predeterminate is GET
     * @param restInterface Interface of methods pre and post execute
     */
    public RestApiClientFile(String Url, String MethodName, Object Params, Bitmap bitmap, METHOD Method, RestInterface restInterface) {
        this.Url = Url;
        this.Params = Params;
        this.MethodName = MethodName;
        this.MethodType = Method;
        this.RestInterface = restInterface;
        this.bitmap = bitmap;
    }

    public interface RestInterface{
        void onFinish(String Result);
        void onBefore();
    }


    private void setUrlConnection(String paramsURL){
        URL url;
        try {
            if(MethodType == METHOD.GET) {
                url = new URL(Url + MethodName  + "/" + paramsURL);
                Log.d(TAG,Url + MethodName  + "/" + paramsURL);
            }else{
                url = new URL(Url + MethodName);
            }
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(MethodType.name());

            if(MethodType == METHOD.POST){
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Length", String.valueOf(paramsURL.length()));
            }else{
                urlConnection.setDoOutput(false);
            }
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String paramsToUrlParams(){
        Log.d(TAG, "aqui ando dentro de la funcion nomas");

        if (Params instanceof String[]) {
            Log.d(TAG, "aqui ando");
            String[] Parameters = (String[]) Params;
            StringBuilder UrlParam = new StringBuilder();

            for(String param : Parameters)
            {
               UrlParam.append(param).append("/");
            }
            return UrlParam.toString();

        }else if(Params instanceof ArrayList) {
            return null;

        }else if(Params instanceof JSONObject) {
            return Params.toString();
        }else if(MethodType == METHOD.GET){
            return "";
        }else{
            return null;
        }
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String paramsURL = paramsToUrlParams();
            if(paramsURL == null){
                Log.i(TAG, "Param invalid please check the documentation");
                return "";
            }
            Log.d(TAG,paramsURL);
            setUrlConnection(paramsURL);
            OutputStream os;
            if(MethodType != METHOD.GET) {

                os = urlConnection.getOutputStream();
                os.write(paramsURL.getBytes("UTF-8"));
                os.flush();
            }

            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_CREATED && statusCode != HttpURLConnection.HTTP_OK && statusCode != HttpURLConnection.HTTP_ACCEPTED) {
                Log.d(TAG, "doInBackground(): connection failed: statusCode: " + statusCode);

                InputStream content = urlConnection.getErrorStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(content));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                Log.d(TAG, "doInBackground(): connection failed: response: " + sb.toString());
                urlConnection.disconnect();
                return null;
            }

            InputStream content = urlConnection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(content));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }

            in.close();
//                os.close();
            urlConnection.disconnect();

            Log.d(TAG, "doInBackground() responseText: " + sb);
            return sb.toString();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(this.RestInterface != null){
            this.RestInterface.onBefore();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(this.RestInterface != null){
            this.RestInterface.onFinish(result);
        }

    }
}
