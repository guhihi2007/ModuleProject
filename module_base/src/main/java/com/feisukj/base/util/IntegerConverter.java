package com.feisukj.base.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class IntegerConverter implements PropertyConverter<List<Integer>, String> {

    private final Gson gson;

    public IntegerConverter() {
        gson = new Gson();
    }

    @Override
    public List<Integer> convertToEntityProperty(String databaseValue) {
        Type type = new TypeToken<ArrayList<Integer>>() {

        }.getType();

        ArrayList<Integer> list = gson.fromJson(databaseValue, type);
        return list;
    }

    @Override
    public String convertToDatabaseValue(List<Integer> entityProperty) {

        return gson.toJson(entityProperty);
    }
}
