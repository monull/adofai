package io.github.monull.adofai;

import com.google.common.io.Files;
import org.apache.commons.compress.utils.Charsets;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.List;

public class ADOFAIMap {

    private List<String> pathDataList;
    private ADOFAIMapSettings settings;

    @SuppressWarnings("UnstableApiUsage")
    public void load(File file) {
        try {
            BufferedReader reader = Files.newReader(file, Charsets.UTF_8);

            StringBuilder sb = new StringBuilder();
            String buf;

            while ((buf = reader.readLine()) != null) {
                sb.append(buf);
            }

            reader.close();

            String mapData = sb.toString();

            if (mapData.charAt(0) != '{') {
                mapData = mapData.substring(1);
            }

            JSONObject json = (JSONObject) new JSONParser().parse(mapData);
            String pathData = (String) json.get("pathData");
            JSONObject settings = (JSONObject) json.get("settings");

            this.settings = new ADOFAIMapSettings(settings);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
