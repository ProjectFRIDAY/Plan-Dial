package com.example.plandial;

import android.content.Context;

import com.opencsv.CSVReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/*
사용법
IconRecommendation IconRecommendation = new IconRecommendation(getApplicationContext());
if(IconRecommendation.isReady()) String result = IconRecommendation.getIconByName(kword);
 */

public class IconRecommendation {
    private static boolean isReady = false;
    private static final HashMap<String, ArrayList<String>> wordVectors = new HashMap<>();
    private static final ArrayList<String> iconFileNames = new ArrayList<>();
    private static final ArrayList<ArrayList<Double>> iconVectors = new ArrayList<>();

    public IconRecommendation(Context context) {
        // 최초 1회 -> 파일 준비
        if (!isReady) {
            if (getIconVectors(context) && getWordVectors(context)) {
                isReady = true;
            }
        }
    }

    private boolean getWordVectors(Context context) {
        // Word2Vec 모델 준비
        try {
            InputStream inputStream = context.getAssets().open("jsons/word_vectors.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            CSVReader read = new CSVReader(reader);

            String[] record = null;
            ArrayList<String> tmp;
            while ((record = read.readNext()) != null) {
                tmp = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(record, 1, record.length)));
                wordVectors.put(record[0], tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean getIconVectors(Context context) {
        // 아이콘 벡터값 준비
        try {
            InputStream inputStream = context.getAssets().open("jsons/icon_vectors.json");
            int fileSize = inputStream.available();

            byte[] buffer = new byte[fileSize];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject iconObject = jsonArray.getJSONObject(i);
                iconFileNames.add(iconObject.getString("icon") + ".png");

                ArrayList<Double> vectorValues = new ArrayList<>();
                JSONArray vectorArray = iconObject.getJSONArray("vector");
                for (int j = 0; j < vectorArray.length(); j++) {
                    Double value = vectorArray.getDouble(j);
                    vectorValues.add(value);
                }
                iconVectors.add(vectorValues);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private double dotOperation(final ArrayList<String> wordVector, final ArrayList<Double> iconVector) {
        // 내적
        double result = 0.0d;
        for (int i = 0; i < iconVectors.size(); i++) {
            result += Double.parseDouble(wordVector.get(i)) * iconVector.get(i);
        }
        return result;
    }

    private double normOperationString(final ArrayList<String> wordVector) {
        // 노름
        double result = 0.0d;
        for (int i = 0; i < iconVectors.size(); i++) {
            double value = Double.parseDouble(wordVector.get(i));
            result += value * value;
        }
        return result;
    }

    private double normOperation(final ArrayList<Double> iconVector) {
        // 노름
        double result = 0.0d;
        for (int i = 0; i < iconVectors.size(); i++) {
            result += iconVector.get(i) * iconVector.get(i);
        }
        return result;
    }

    public String getIconByName(String keyword) {
        try {
            ArrayList<String> wordVector = wordVectors.get(keyword);
            int targetIndex = -1;
            double targetValue = -1;

            for (int i = 0; i < iconVectors.size(); i++) {
                ArrayList<Double> iconVector = iconVectors.get(i);
                double value = dotOperation(wordVector, iconVector) / (Math.sqrt(normOperationString(wordVector)) * Math.sqrt(normOperation(iconVector)));

                if (value > targetValue) {
                    targetValue = value;
                    targetIndex = i;
                }
            }
            return iconFileNames.get(targetIndex);

        } catch (Exception e) {
            return "unknown.png";
        }
    }

    public boolean getIsReady() {
        return isReady;
    }
}
