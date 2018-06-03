package mmd.konnect.Models;

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
        if (list == null) return null;
        List<DataWrapper> wrappers = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            wrappers.add(new DataWrapper<>(list.get(i)));
        }

        return wrappers;
    }
}
