package mmd.meetup.Firebase;

/**
 * Created by mickeydang on 2018-03-30.
 */

public class FirebaseDB {

    //property of time option and location option
    public static String VOTE_COUNT = "vote_count";

    public static class FinalizedMeetings {

        public static String path = "finalized_meetings";

        public static class Entries {
            public static String id = "id";
            public static String creatorID = "creator_id";
            public static String title = "title";
            public static String description = "description";
            public static String members = "members";
            //only one entry
            public static String time = "time";
            public static String location = "location";
        }

    }

    public static class PendingMeetings {
        public static String path = "pending_meetings";

        public static class Entries {
            public static String id = "id";
            public static String inviteID = "inviteID";
            public static String organizerID = "organizerID";
            public static String title = "title";
            public static String description = "description";
            public static String members = "members";

            //multiple entries (options)
            public static String timeOptions = "time_options";
            public static String locationOptions = "location_options";
        }
    }

    public static class Users {

        public static String path = "users";

        public static class Entries {
            public static String name = "name";
            public static String id = "id";
            public static String pendingMeetings = "pending_meetings";
            public static String finalizedMeetings = "finalized_meetings";
        }

    }

}
