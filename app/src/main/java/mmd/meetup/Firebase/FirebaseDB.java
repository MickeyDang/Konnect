package mmd.meetup.Firebase;

/**
 * Created by mickeydang on 2018-03-30.
 */

public class FirebaseDB {

    public static class Meetings {

        public static String path = "meetings";

        public static class Entries {
            public static String id = "id";
            public static String creatorID = "creator_id";
            public static String title = "title";
            public static String description = "description";
            public static String members = "members";
        }

    }

    public static class Users {

        public static String path = "users";

        public static class Entries {
            public static String name = "name";
            public static String id = "id";
            public static String meetings = "meetings";
        }

    }

}
