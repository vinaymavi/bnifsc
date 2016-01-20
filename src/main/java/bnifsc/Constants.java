package bnifsc;

/**
 * Contains the client IDs and scopes for allowed clients consuming your API.
 */
public class Constants {
    /*TODO add ids in database and read from there.*/
    public static final String WEB_CLIENT_ID = "1056077165370-lpjivah5okp45lcelbrgeeg7d4hc16rv.apps.googleusercontent" +
            ".com";
    public static final String ANDROID_CLIENT_ID = "replace this with your Android client ID";
    public static final String IOS_CLIENT_ID = "replace this with your iOS client ID";
    public static final String ANDROID_AUDIENCE = WEB_CLIENT_ID;
    public static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
}
