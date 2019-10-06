package com.taximaps.server.maps;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

@Component
public class JsonReader {

    private static final double EARTH_RADIUS = 6371.; // Earth redius

    private static String readAll(final Reader rd) throws IOException {
        final StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject read(final String url) throws IOException, JSONException {
        final InputStream is = new URL(url).openStream();
        try {
            final BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            final String jsonText = readAll(rd);
            final JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }


    private static Point getPoint(final String address) throws IOException, JSONException {
        final String baseUrl = "http://maps.googleapis.com/maps/api/geocode/json";// путь к Geocoding API по HTTP
        final Map<String, String> params = Maps.newHashMap();
        params.put("sensor", "false");// указывает, исходит ли запрос на геокодирование от устройства с датчиком
        // местоположения
        params.put("address", address);// адрес, который нужно геокодировать
        final String url = baseUrl + '?' + encodeParams(params);// генерируем путь с параметрами
        System.out.println(url);// Можем проверить что вернет этот путь в браузере
        final JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ
        // как правило наиболее подходящий ответ первый и данные о координатах можно получить по пути
        // //results[0]/geometry/location/lng и //results[0]/geometry/location/lat
        JSONObject location = response.getJSONArray("results").getJSONObject(0);
        location = location.getJSONObject("geometry");
        location = location.getJSONObject("location");
        final double lng = location.getDouble("lng");// долгота
        final double lat = location.getDouble("lat");// широта
        final Point point = new Point(lng, lat);
        System.out.println(address + " " + point); // выводим адрес и точку для него
        return point;
    }

    private static double deg2rad(final double degree) {
        return degree * (Math.PI / 180);
    }

    private static class Point {
        public double lat;
        public double lng;

        public Point(final double lng, final double lat) {
            this.lng = lng;
            this.lat = lat;
        }

        @Override
        public String toString() {
            return lat + "," + lng;
        }
    }

    private static String encodeParams(final Map<String, String> params) {
        final String paramsUrl = Joiner.on('&').join(// получаем значение вида key1=value1&key2=value2...
                Iterables.transform(params.entrySet(), input -> {
                    try {
                        final StringBuffer buffer = new StringBuffer();
                        buffer.append(input.getKey());// получаем значение вида key=value
                        buffer.append('=');
                        buffer.append(URLEncoder.encode(input.getValue(), "utf-8"));// кодируем строку в соответствии со стандартом HTML 4.01
                        return buffer.toString();
                    } catch (final UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }));
        return paramsUrl;
    }

    public static String sendRequest(String origin, String destination) throws IOException {
        final String baseUrl = "https://maps.googleapis.com/maps/api/directions/json";// путь к Geocoding API по
        // HTTPS
        final Map<String, String> params = Maps.newHashMap();
        params.put("key", "AIzaSyBR4RT24iLNmr74yE168KRWz5qdjr3NhVc");
        params.put("sensor", "false");// указывает, исходит ли запрос на геокодирование от устройства с датчиком
        params.put("language", "en");// язык данные на котором мы хотим получить
        params.put("mode", "driving");// способ перемещения, может быть driving, walking, bicycling
        params.put("origin", origin/*"Ukraine, Vinnytsia, Kosmonavtiv 66"*/);// адрес или текстовое значение широты и
        // отправного пункта маршрута
        params.put("destination", destination/*"Ukraine, Vinnytsia, Yunosti Ave, 43-А"*/);// адрес или текстовое значение широты и
        // долготы
        // долготы конечного пункта маршрута
        final String url = baseUrl + '?' + encodeParams(params);// генерируем путь с параметрами
        System.out.println(url); // Можем проверить что вернет этот путь в браузере
        final JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ
        // как правило наиболее подходящий ответ первый и данные о координатах можно получить по пути
        // //results[0]/geometry/location/lng и //results[0]/geometry/location/lat
        JSONObject location = response.getJSONArray("routes").getJSONObject(0);
        location = location.getJSONArray("legs").getJSONObject(0);
        final String distance = location.getJSONObject("distance").getString("text");
        final String duration = location.getJSONObject("duration").getString("text");
        System.out.println(distance + "\n" + duration);
        return distance + ", " + duration;
    }

}
