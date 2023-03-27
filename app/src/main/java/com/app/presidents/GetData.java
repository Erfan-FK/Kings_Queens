package com.app.presidents;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

enum DOWNLOAD_STATUS {SUCCEED, FAILED, PROCESSING}

interface OnDataAvailable{
    void onDataAvailable(DOWNLOAD_STATUS downloadStatus, String imageURI);
}

public class GetData extends AsyncTask<String, Void, String> {

    private DOWNLOAD_STATUS downloadStatus;
    private OnDataAvailable callBack;
    private final String baseURL = "https://www.googleapis.com/customsearch/v1";
    private final String API_KEY = ApiKey.getAPI_KEY();
    private final String CX_ID = "67a579fb4ac1c4ff3";

    public GetData(OnDataAvailable callBack) {
        this.callBack = callBack;
        this.downloadStatus = DOWNLOAD_STATUS.PROCESSING;
    }

    @Override
    protected void onPostExecute(String imageURI) {
        if (this.callBack != null)
            this.callBack.onDataAvailable(DOWNLOAD_STATUS.SUCCEED, imageURI);
    }

    @Override
    protected String doInBackground(String... name) {
        String searchURL = createURI(name[0]);
        HttpsURLConnection httpsURLConnection = null;
        BufferedReader reader = null;
        String imageUrl;

        this.downloadStatus = DOWNLOAD_STATUS.PROCESSING;
        try {
            URL url = new URL(searchURL);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.connect();
            int response = httpsURLConnection.getResponseCode();

            reader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
            StringBuilder data = new StringBuilder();

            for (String line = reader.readLine(); line != null; line = reader.readLine()){
                data.append(line).append("\n");
            }

            JSONObject jsonData = new JSONObject(data.toString());
            JSONArray jsonArray = jsonData.getJSONArray("items");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            imageUrl = jsonObject.getString("link");
            return imageUrl;
        }catch (Exception e){
            System.out.println("Error " + e.getMessage());
        }finally {
            if(httpsURLConnection != null)
                httpsURLConnection.disconnect();
            if (reader != null){
                try {
                    reader.close();
                }catch (Exception e){
                    System.out.println("Error " + e.getMessage());
                }
            }


        }
        this.downloadStatus = DOWNLOAD_STATUS.FAILED;
        return null;
    }

    private String createURI(String q){
        return Uri.parse(this.baseURL).buildUpon()
                .appendQueryParameter("key", this.API_KEY)
                .appendQueryParameter("cx", this.CX_ID)
                .appendQueryParameter("q", q)
                .appendQueryParameter("searchType", "image")
                .build().toString();
    }

}
