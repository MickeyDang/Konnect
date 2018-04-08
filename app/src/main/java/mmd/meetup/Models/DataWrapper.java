package mmd.meetup.Models;

import java.util.ArrayList;
import java.util.List;

public class DataWrapper<T> {
    T t;

    public DataWrapper(T t) {
        this.t = t;
    }

    public T getValue() {
        return t;
    }

    public static <T> List<DataWrapper> wrapObjects(List<T> list) {
        List<DataWrapper> wrappers = new ArrayList<>();

        //cannot be for each in case of null list
        for (int i = 0; i < list.size(); i++) {
            wrappers.add(new DataWrapper<T>(list.get(i)));
        }

        return wrappers;
    }
}
