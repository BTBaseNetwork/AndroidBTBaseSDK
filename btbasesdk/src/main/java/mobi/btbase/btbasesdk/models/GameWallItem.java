package mobi.btbase.btbasesdk.models;

import java.util.Dictionary;

public class GameWallItem {
    public static final int LABEL_NONE = 0;
    public static final int LABEL_NEW = 0;
    public static final int LABEL_HOT = 0;

    public static final int VIDEO_TYPE_UNSPECIFIC = 0;
    public static final int VIDEO_TYPE_VERTICAL = 1;
    public static final int VIDEO_TYPE_HORIZONTAL = 2;

    public String itemId;
    public String gameName;
    public String iconUrl;

    public String videoUrl;
    public boolean videoLoop;
    public boolean closeVideo = true ;//Effect Only the video loop is false
    public int videoType = 0;

    public String coverUrl;
    public int labels = 0;
    public int priority = 0;
    public float stars = 0f;
    public BTAppLink appLink;

    public Dictionary<String,String> loc;

    public String getLocalizedString(String key, String defaultValue) {
        return defaultValue;
    }

    public boolean hasHotLabel() {
        return (labels & LABEL_HOT) != 0;
    }

    public boolean hasNewLabel() {
        return (labels & LABEL_NEW )!= 0;
    }
}
