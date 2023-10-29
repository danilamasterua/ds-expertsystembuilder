package ds.esb.expertsystembuilder.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ds.esb.expertsystembuilder.classes.Model;
import ds.esb.expertsystembuilder.classes.RulesContainer;
import ds.esb.expertsystembuilder.classes.Variables;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class JsonWorks {
    public static Model loadProject(String path) throws RuntimeException{
        Variables variables = new Variables();
        RulesContainer rules = new RulesContainer();
        int result = variables.loadVariablesFromJson(path);
        if (result!=200){
            throw new RuntimeException("File with variables was not found, or has an incompatible format");
        } else {
            result = rules.loadExpertRules(path);
            if (result!=200){
                throw new RuntimeException("File with rules was not found, or has an incompatible format");
            }
        }
        return new Model(variables, rules);
    }

    public static HashMap<String, String> getLocalizations() throws RuntimeException{
        try (InputStream s = JsonWorks.class.getResourceAsStream("/localizations/localizations.json")){
            assert s != null;
            InputStreamReader reader = new InputStreamReader(s);
            BufferedReader bReader = new BufferedReader(reader);
            String json = bReader.lines().collect(Collectors.joining());
            HashMap<String, String> retMap = new HashMap<>();
            JsonArray array = JsonParser.parseString(json).getAsJsonArray();
            for (var el:array){
                JsonObject jo = el.getAsJsonObject();
                retMap.put(jo.get("locale").getAsString(), jo.get("file").getAsString());
            }
            System.out.println(retMap.keySet());
            return retMap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String, String> getLocalization(String key) throws RuntimeException{
        try (InputStream inputStream = JsonWorks.class.getResourceAsStream("/localizations/"+key)){
            HashMap<String, String> ret = new HashMap<>();
            if(inputStream != null){
                Scanner scanner = new Scanner(inputStream);
                String json = scanner.useDelimiter("\\A").next();
                JsonObject object = JsonParser.parseString(json).getAsJsonObject();
                Set<String> jKeys = object.keySet();
                for (var s:jKeys){
                    ret.put(s, object.get(s).getAsString());
                }
                return ret;
            } else {
                throw new RuntimeException("Localization " + key + " file was not found");
            }
        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
