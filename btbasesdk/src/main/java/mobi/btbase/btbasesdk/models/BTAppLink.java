package mobi.btbase.btbasesdk.models;

import org.json.JSONException;
import org.json.JSONObject;

public class BTAppLink {
    public String url;
    public String androidPackageId;
    public String iOSUrlScheme;
    public String iOSAppId;
    public String iOSPackageId;

    public static BTAppLink parse(JSONObject jsonObject) throws JSONException {
        BTAppLink res = new BTAppLink();
        res.url = jsonObject.getString("url");
        res.androidPackageId = jsonObject.getString("androidPackageId");
        res.iOSUrlScheme = jsonObject.getString("iOSUrlScheme");
        res.iOSAppId = jsonObject.getString("iOSAppId");
        res.iOSPackageId = jsonObject.getString("iOSPackageId");
        return res;
    }
}
