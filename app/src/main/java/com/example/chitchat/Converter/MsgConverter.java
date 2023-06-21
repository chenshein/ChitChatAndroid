package com.example.chitchat.Converter;
import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.chitchat.data.Msg.MsgEntity;
import java.lang.reflect.Type;
import java.util.List;

public class MsgConverter {
        @TypeConverter
        public static List<MsgEntity> fromString(String value) {
            Type listType = new TypeToken<List<MsgEntity>>() {}.getType();
            return new Gson().fromJson(value, listType);
        }

        @TypeConverter
        public static String fromList(List<MsgEntity> list) {
            Gson gson = new Gson();
            return gson.toJson(list);
        }
    }


