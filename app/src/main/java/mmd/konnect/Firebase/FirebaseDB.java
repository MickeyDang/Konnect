package mmd.konnect.Firebase;

/**
 * Created by mickeydang on 2018-03-30.
 */

public class FirebaseDB {

    //property of time option and location option
    final public static String VOTE_COUNT = "vote_count";

    public static class FinalizedMeetings {

        final public static String path = "finalized_meetings";

        public static class Entries {
            final public static String id = "id";
            final public static String creatorID = "creator_id";
            final public static String title = "title";
            final public static String description = "description";
            final public static String members = "members";
            //only one entry
            final public static String time = "time";
            final public static String location = "location";
            final public static String invitedUsers = "invitedUsers";
        }

    }

    public static class PendingMeetings {
        final public static String path = "pending_meetings";

        public static class Entries {
            final public static String id = "id";
            final public static String inviteID = "inviteID";
            final public static String organizerID = "organizerID";
            final public static String title = "title";
            final public static String description = "description";
            final public static String members = "members";

            //multiple entries (options)
            final public static String timeOptions = "timeOptions";
            final public static String locationOptions = "meetingPlaces";
            final public static String invitedUsers = "invitedUsers";
        }
    }

    public static class Users {

        final public static String path = "users";

        public static class Entries {
            final public static String email = "email";
            final public static String name = "name";
            final public static String id = "id";
            final public static String pendingMeetings = "pending_meetings";
            final public static String finalizedMeetings = "finalized_meetings";
        }

    }

}
