package ru.saydumarov;

import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

class Translator {
    private static final String API_KEY = "trnsl.1.1.20170822T143935Z.93aec9c80a4d874e.76f6cab7ca8ebb1aaaf148491f8a9f3fbd1daecd";
    private static final String SERVER_REQUEST = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
    private static final String ENCODING = "UTF-8";
    private static final String PARAM_KEY = "key=";
    private static final String PARAM_LANG = "&lang=";
    private static final String PARAM_TEXT = "&text=";


    static String translate(String original) throws Exception {
        String params = SERVER_REQUEST + PARAM_KEY
                + URLEncoder.encode(API_KEY, ENCODING)
                + PARAM_LANG + URLEncoder.encode("ru-en", ENCODING)
                + PARAM_TEXT + URLEncoder.encode(original, ENCODING);
        URL url = new URL(params);
        return retrieveTranslate(url).trim();
    }

    private static String retrieveTranslate(URL url) throws Exception {
        String json = retrieveResponse(url);

        JSONParser parser = new JSONParser();
        org.json.simple.JSONObject jsonObj = (org.json.simple.JSONObject) parser.parse(json);
        org.json.simple.JSONArray translationArr = (org.json.simple.JSONArray) jsonObj.get("text");

        String translation = (String) translationArr.get(0);
        return translation.trim();
    }


    private static String retrieveResponse(URL url) throws Exception {
        HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();
        try {
            int responseCode = uc.getResponseCode();
            String result = streamToString(uc.getInputStream());
            if(responseCode!=200) {
                throw new Exception("Ошибка API: " + result);
            }
            return result;
        } finally {
            uc.disconnect();
        }
    }

    private static String streamToString(InputStream inputStream) throws Exception {
        StringBuilder outputBuilder = new StringBuilder();

        try {
            String string;
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
                while (null != (string = reader.readLine())) {
                    outputBuilder.append(string.replaceAll("\uFEFF", ""));
                }
            }
        } catch (Exception e) {
            throw new Exception("Не удалось прочитать поток", e);
        }
        return outputBuilder.toString();
    }
}
