package com.teamproject.inspectionframework.Application_Layer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.Log;




import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


/**
 * Created by Tobias on 28.01.15.
 */
public class RESTServices {

    //Var-declaration
    private Context context;

    //Constructor
    public RESTServices() {
    }

    //Read and Access the server
    //Receives the URI, from where should be read
    //Returns the string read from the server
    public String readHerokuServer(String uri) {
        StrictMode.ThreadPolicy policy = new StrictMode.
        ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        StringBuilder builder = new StringBuilder();

        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("https://inspection-framework.herokuapp.com/"+uri);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(ParseJSON.class.toString(), "Download not possible!");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();

    }

    public void postToHerokuServer(String uri, String jsonObject){
        JSONObject jO;

        //Allow internet connection
        StrictMode.ThreadPolicy policy = new StrictMode.
        ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("https://inspection-framework.herokuapp.com/"+uri);

        //Create a new JSONObject from the given String
        try {
            jO = new JSONObject(jsonObject);
            //passes the results to a string builder/entity
            try {
                StringEntity se = new StringEntity(jO.toString());
                httpPost.setEntity(se);
                //sets a request header so the page receving the request
                //will know what to do with it
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                try {
                    httpClient.execute(httpPost);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void putToHerokuServer(String uri, String jsonObject){
        JSONObject jO;

        //Allow internet connection
        StrictMode.ThreadPolicy policy = new StrictMode.
        ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("https://inspection-framework.herokuapp.com/"+uri);

        //Create a new JSONObject from the given String
        try {
            jO = new JSONObject(jsonObject);
            //passes the results to a string builder/entity
            try {
                StringEntity se = new StringEntity(jO.toString());
                httpPost.setEntity(se);
                //sets a request header so the page receving the request
                //will know what to do with it
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                try {
                    httpClient.execute(httpPost);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Check whether a internet connection is available
    //Returns true, when a connection is available
    //Returns false, when no connection is available
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
