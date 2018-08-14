package tina.com.common.http.net.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

import tina.com.common.http.model.TagModel;


public class ArrayObjectAdapter implements JsonDeserializer<TagModel> {
    @Override
    public TagModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (json == null || !json.isJsonObject()) return null;
        String jsonString = json.toString();
        if (jsonString.equals("") || jsonString.equals("null") ||
                jsonString.equals("[]") || jsonString.equals("[]")) {
            return null;
        }
        try {
            return GsonUtil.getObjectFromJson(jsonString, TagModel.class);
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }

}