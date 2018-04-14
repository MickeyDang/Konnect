package mmd.meetup.Adapters;

import java.util.List;

/**
 * Created by mickeydang on 2018-03-31.
 */

public interface FirebaseAdapter<T> {

    void onInsert(T t);
    void onUpdate(T t);
    void onFilter(T t);
    void onDelete(T t);
    List<T> getFullList();
    boolean containsItem(String s);

}
