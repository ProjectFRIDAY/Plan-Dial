package com.friday.plandial;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
IconRecommendation iconRecommendation = new IconRecommendation();
if(iconRecommendation.getIsReady()) int result = iconRecommendation.getIconByName(context, kword);
 */

// 추후 싱글톤으로 변경 가능성 있음
public class IconRecommendation {
    private static boolean isReady = false;
    private static final HashMap<String, ArrayList<String>> wordVectors = new HashMap<>();
    private static final ArrayList<String> iconFileNames = new ArrayList<>();
    private static final ArrayList<ArrayList<Double>> iconVectors = new ArrayList<>();
    private static final HashMap<String, String> taggedWords = new HashMap<>();

    private static final String WORD_VECTOR_FILE = "datas/word_vectors.csv";
    private static final String ICON_VECTOR_FILE = "datas/icon_vectors.json";
    private static final String WORD_TAGGING_FILE = "datas/tag.json";
    public static final int UNKNOWN_IMAGE = R.drawable.baseline_question_mark_black;
    private static final String IMAGE_EXTENSION = ""; // 예비용

    private static final String JSON_ENTIRE_KEY = "data";
    private static final String JSON_ICON_KEY = "icon";
    private static final String JSON_VECTOR_KEY = "vector";
    private static final String JSON_TAG_KEY = "tag";

    public void roadIconData(Context context) {
        RoadDataTask roadDataTask = new RoadDataTask(context);
        roadDataTask.execute();
    }

    private boolean getWordVectors(Context context) {
        // Word2Vec 모델 준비
        try {
            InputStream inputStream = context.getAssets().open(WORD_VECTOR_FILE);
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
            InputStream inputStream = context.getAssets().open(ICON_VECTOR_FILE);
            int fileSize = inputStream.available();

            byte[] buffer = new byte[fileSize];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(JSON_ENTIRE_KEY);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject iconObject = jsonArray.getJSONObject(i);
                iconFileNames.add(iconObject.getString(JSON_ICON_KEY) + IMAGE_EXTENSION);

                ArrayList<Double> vectorValues = new ArrayList<>();
                JSONArray vectorArray = iconObject.getJSONArray(JSON_VECTOR_KEY);
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

    private boolean getTaggedWords(Context context) {
        // 사전 태깅된 단어는 바로 사용해서 추천
        try {
            InputStream inputStream = context.getAssets().open(WORD_TAGGING_FILE);
            int fileSize = inputStream.available();

            byte[] buffer = new byte[fileSize];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(JSON_ENTIRE_KEY);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tagObject = jsonArray.getJSONObject(i);
                String fileName = tagObject.getString(JSON_ICON_KEY) + IMAGE_EXTENSION;

                for (String tag : tagObject.getString(JSON_TAG_KEY).split("/")) {
                    if (!"".equals(tag) && !taggedWords.containsKey(tag)) {
                        taggedWords.put(tag, fileName);
                    }
                }
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

    public int getIconByName(Context context, String keyword) {
        if (!isReady) {
            return UNKNOWN_IMAGE;
        }
        if (taggedWords.containsKey(keyword)) {
            return getResource(context, taggedWords.get(keyword));
        }

        int targetIndex = -1;
        double targetValue = -1;

        for (String word : keyword.split(" ")) {
            if (taggedWords.containsKey(word)) {
                return getResource(context, taggedWords.get(word));
            }

            try {
                ArrayList<String> wordVector = wordVectors.get(word);
                for (int i = 0; i < iconVectors.size(); i++) {
                    ArrayList<Double> iconVector = iconVectors.get(i);
                    double value = dotOperation(wordVector, iconVector) / (Math.sqrt(normOperationString(wordVector)) * Math.sqrt(normOperation(iconVector)));

                    if (value > targetValue) {
                        targetValue = value;
                        targetIndex = i;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (targetIndex == -1) {
            return UNKNOWN_IMAGE;
        }

        return getResource(context, iconFileNames.get(targetIndex));
    }

    private int getResource(Context context, String iconName) {
        Log.i("IconRecommendation", iconName);
        return context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
    }

    public boolean getIsReady() {
        return isReady;
    }

    public int getUnknownImage() {
        return UNKNOWN_IMAGE;
    }

    private class RoadDataTask extends AsyncTask<Boolean, Boolean, Boolean> {
        private final Context context;

        public RoadDataTask(Context context) {
            super();
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Boolean... booleans) {
            boolean completed = getIconVectors(context) && getWordVectors(context) && getTaggedWords(context);
            return isReady = completed;
        }
    }
}
