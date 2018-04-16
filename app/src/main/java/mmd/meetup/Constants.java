package mmd.meetup;

/**
 * Created by mickeydang on 2018-03-30.
 */

public class Constants {

    final public static int RC_VOTE = 22;
    final public static int resultSuccess = 11;

    public static class MeetingNavigation{
        public static String INVITEE_KEY = "invitees_key";
        public static String MEETING_OBJ_KEY = "meeting_key";
        public static String TIME_OPTION_KEY = "timeoption_key";
        public static String PLACE_OPTION_KEY = "placeoption_key";

        public static String STEP_KEY = "step_key";
        public static String stepDescription = "step_description";
        public static String stepTime = "step_time";
        public static String stepLocation = "step_location";
        public static String stepInvite = "step_invite";

        public static int resultSuccess = 11;
        public static int resultFailure = 12;

        final public static int RC_DESCRIPTION = 1;
        final public static int RC_TIME = 2;
        final public static int RC_LOCATION = 3;
        final public static int RC_INVITE = 4;

    }

    public static class KEYS {
        public static String SIGN_OUT_KEY = "isSignOut";
        public static String PENDING_MEETING_ID = "p_m_i";
    }

}
