package mobi.btbase.btbasesdk.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BTGameWallConfig {
    public int configVersion;
    public BTGameWallItem[] items;

    public static BTGameWallConfig parse(JSONObject jsonObject) throws JSONException {
        BTGameWallConfig result = new BTGameWallConfig();
        result.configVersion = jsonObject.getInt("configVersion");
        JSONArray itemArr = jsonObject.getJSONArray("items");
        result.items = new BTGameWallItem[itemArr.length()];
        for (int i = 0; i < itemArr.length(); i++) {
            result.items[i] = BTGameWallItem.parse(itemArr.getJSONObject(i));
        }
        return result;
    }
}
