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

        for (T item : list) {
            wrappers.add(new DataWrapper<>(item));
        }

        return wrappers;
    }
}
