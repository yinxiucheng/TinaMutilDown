package tina.com.common.http.net.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.Date;

import tina.com.common.http.model.TagModel;

/**
 * Created by Allen on 2017/11/20.
 */

public class GsonAdapter {

    public static Gson buildGson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new IntegerDefaultAdapter())
                .registerTypeAdapter(int.class, new IntegerDefaultAdapter())
                .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                .registerTypeAdapter(long.class, new LongDefault0Adapter())
                .registerTypeAdapter(TagModel.class, new ArrayObjectAdapter())
                .registerTypeAdapter(Object.class, new ArrayObjectAdapter())
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        return gson;
    }
}
