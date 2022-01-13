package com.example.plandial;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class IconRecommendation {
    private static boolean sIsReady = false;
    private static JSONObject sWordVectors = new JSONObject();
    private static final ArrayList<String> sIconFileNames = new ArrayList<>();
    private static final ArrayList<ArrayList<Double>> sIconVectors = new ArrayList<>();
    private Context mContext;

    public IconRecommendation(Context context) {
        // 최초 1회 -> 파일 준비
        if (!sIsReady) {
            this.mContext = context;
            if(getIconVectors() && getWordVectors()){
                sIsReady = true;
            }
        }
    }

    private boolean getWordVectors(){
        // Word2Vec 모델 준비
        try{
            InputStream inputStream = mContext.getAssets().open("jsons/word_vectors.json");
            int fileSize = inputStream.available();

            byte[] buffer = new byte[fileSize];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);
            sWordVectors = jsonObject.getJSONObject("data");
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean getIconVectors(){
        // 아이콘 벡터값 준비
        try{
            InputStream inputStream = mContext.getAssets().open("jsons/icon_vectors.json");
            int fileSize = inputStream.available();

            byte[] buffer = new byte[fileSize];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject iconObject = jsonArray.getJSONObject(i);
                sIconFileNames.add(iconObject.getString("icon") + ".png");

                ArrayList<Double> vectorValues = new ArrayList<>();
                JSONArray vectorArray = iconObject.getJSONArray("vector");
                for(int j = 0; j < vectorArray.length(); j++)
                {
                    Double value = vectorArray.getDouble(j);
                    vectorValues.add(value);
                }
                sIconVectors.add(vectorValues);
            }

        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private double dotOperation(final JSONArray wordVector, final ArrayList<Double> iconVector) throws JSONException {
        // 내적
        double result = 0.0d;
        for(int i = 0; i < sIconVectors.size(); i++) {
            result += wordVector.getDouble(i) * iconVector.get(i);
        }
        return result;
    }

    private double normOperation(final JSONArray wordVector) throws JSONException {
        // 노름
        double result = 0.0d;
        for(int i = 0; i < sIconVectors.size(); i++) {
            result += wordVector.getDouble(i) * wordVector.getDouble(i);
        }
        return result;
    }

    private double normOperation(final ArrayList<Double> iconVector){
        // 노름
        double result = 0.0d;
        for(int i = 0; i < sIconVectors.size(); i++) {
            result += iconVector.get(i) * iconVector.get(i);
        }
        return result;
    }

    public String getIconByName(String keyword){
        // 아이콘 추천 (코사인 유사도 이용)
        try{
            JSONArray wordVector = sWordVectors.getJSONArray(keyword);
            int targetIndex = -1;
            double targetValue = -1;

            for(int i = 0; i < sIconVectors.size(); i++)
            {
                ArrayList<Double> iconVector = sIconVectors.get(i);
                double value = dotOperation(wordVector, iconVector) / (Math.sqrt(normOperation(wordVector)) * Math.sqrt(normOperation(iconVector)));

                if(value > targetValue){
                    targetValue = value;
                    targetIndex = i;
                }
            }

            return sIconFileNames.get(targetIndex);
        } catch(Exception e){
            return "unknown.png";
        }

    }

    public boolean getIsReady(){
        return sIsReady;
    }
}
