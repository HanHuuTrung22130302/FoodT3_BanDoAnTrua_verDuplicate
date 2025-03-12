package hcmuaf.nlu.edu.vn.testproject.services;

public class Iconstant {
    // Google
    public static final String GOOGLE_CLIENT_ID = "165264526065-c32ercvpjs2kccueb3mjj7l3nd2ksqk7.apps.googleusercontent.com";
    public static final String GOOGLE_CLIENT_SECRET = "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";
    public static final String GOOGLE_REDIRECT_URI = "http://localhost:8080/testProject/loginGoogle";
    public static final String GOOGLE_GRANT_TYPE = "authorization_code";
    public static final String GOOGLE_LINK_GET_TOKEN = "https://accounts.google.com/o/oauth2/token";
    public static final String GOOGLE_LINK_GET_USER_INFO = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";

    // Facebook
    public static final String FACEBOOK_CLIENT_ID = "624822650411926";
    public static final String FACEBOOK_CLIENT_SECRET = "49afe428df8031404776e6ead7cba62a";
    public static final String FACEBOOK_REDIRECT_URI = "http://localhost:8080/testProject/loginFacebook";
    public static final String FACEBOOK_LINK_GET_TOKEN = "https://graph.facebook.com/v19.0/oauth/access_token";
    public static final String FACEBOOK_LINK_GET_USER_INFO = "https://graph.facebook.com/me?fields=id,name,email,picture&access_token=";
}
