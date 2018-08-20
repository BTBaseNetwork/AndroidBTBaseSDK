package mobi.btbase.btbasesdk.models;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Hashtable;
import java.util.Locale;

public class BTGameWallItem {

    public static final int LABEL_NONE = 0;
    public static final int LABEL_NEW = 1;
    public static final int LABEL_HOT = 2;

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

    public Hashtable<String,String> loc;

    public String getLocalizedString(Context context, String key, String defaultValue) {
        Locale locale = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
           locale = context.getResources().getConfiguration().getLocales().get(0);
        }else {
            locale = context.getResources().getConfiguration().locale;
        }

        String locKey = String.format("%s_%s",locale.getCountry(),locale.getDisplayLanguage());

        if (loc != null && loc.containsKey(locKey))
        {
            return loc.get(locKey);
        }
        return defaultValue;
    }

    public boolean hasHotLabel() {
        return (labels & LABEL_HOT) != 0;
    }

    public boolean hasNewLabel() {
        return (labels & LABEL_NEW )!= 0;
    }

    public static BTGameWallItem parse(JSONObject jsonObject) throws JSONException {
        BTGameWallItem res = new BTGameWallItem();
        res.itemId = jsonObject.getString("itemId");
        res.gameName = jsonObject.getString("gameName");
        res.videoUrl = jsonObject.getString("videoUrl");
        res.videoLoop = jsonObject.getBoolean("videoLoop");
        res.closeVideo = jsonObject.getBoolean("closeVideo");
        res.videoType = jsonObject.getInt("videoType");
        res.coverUrl = jsonObject.getString("coverUrl");
        res.labels = jsonObject.getInt("labels");
        res.priority = jsonObject.getInt("priority");
        res.stars = (float) jsonObject.getDouble("stars");
        res.appLink = BTAppLink.parse(jsonObject.getJSONObject("appLink"));
        JSONObject locDictJObj = jsonObject.getJSONObject("loc");
        res.loc = new Hashtable<>();
        return res;
    }

    public boolean hasVideo() {
        return videoUrl != null && videoUrl.trim().length() > 0;
    }
}
