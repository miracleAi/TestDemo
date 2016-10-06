package io.egg.jiantu;

/**
 * Created by apolloyujl on 14-6-20.
 */
public class Constants {
    public static final String GA_ID = "UA-39803356-6";

    public static final String APP_ID = "wx12f3f5b32bf3a532";//wxd930ea5d5a258f4f

    public static final String QQ_ID ="1101558257";
    public static final String QQ_ID_TEST ="222222";
    public static final String QQ_KEY = "vVnE3qZjRhJB0IeO";

    public static final String MI_PUSH_APP_ID_ALPHA = "2882303761517237153";
    public static final String MI_PUSH_APP_KEY_ALPHA = "5461723763153";

    public static final String MI_PUSH_APP_ID = "2882303761517225651";
    public static final String MI_PUSH_APP_KEY = "5971722543651";

    public static final String CAMERA_TMP_FILE_NAME = "camera";

//    public static final String APP_ID = "wxd930ea5d5a258f4f";

    public final static int SHARE_TO_SESSION = 1;

    public final static int SHARE_TO_TIMELINE = 2;

    public static String qqAppId(){
        if (BuildConfig.DEBUG){
            return QQ_ID_TEST;
        }else {
            return QQ_ID;
        }
    }

    public static class ShowMsgActivity {
        public static final String STitle = "showmsg_title";
        public static final String SMessage = "showmsg_message";
        public static final String BAThumbData = "showmsg_thumb_data";
    }
}
