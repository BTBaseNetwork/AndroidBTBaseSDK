package mobi.btbase.btbasesdk;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mobi.btbase.btbasesdk.common.GlideRoundTransform;
import mobi.btbase.btbasesdk.models.BTGameWallConfig;
import mobi.btbase.btbasesdk.models.BTGameWallItem;

public class GameWallActivity extends AppCompatActivity {

    class GameWallListAdapter extends BaseAdapter {

        class BannerItemViewHolder implements View.OnClickListener {
            ImageView mIconView;
            TextView mTitleText;
            ImageView[] mStarViews;
            ImageView mPlayVideoView;
            ImageView mHotLabelView;
            ImageView mNewLabelView;
            private BTGameWallItem gameWallItem;

            RequestListener<Drawable> updateIconReqListener = new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    mIconView.clearColorFilter();
                    return false;
                }
            };

            public void bindView(View view) {

                mIconView = view.findViewById(R.id.app_icon);
                mTitleText = view.findViewById(R.id.app_title);
                mStarViews = new ImageView[5];
                mStarViews[0] = view.findViewById(R.id.app_star_0);
                mStarViews[1] = view.findViewById(R.id.app_star_1);
                mStarViews[2] = view.findViewById(R.id.app_star_2);
                mStarViews[3] = view.findViewById(R.id.app_star_3);
                mStarViews[4] = view.findViewById(R.id.app_star_4);
                mPlayVideoView = view.findViewById(R.id.play_video_button);
                mHotLabelView = view.findViewById(R.id.hot_label);
                mNewLabelView = view.findViewById(R.id.new_label);

                mPlayVideoView.setOnClickListener(this);
            }

            public void setGameWallItem(BTGameWallItem gameWallItem) {
                this.gameWallItem = gameWallItem;
                updateViews();
            }

            private void updateViews() {
                BTGameWallItem item = getGameWallItem();
                mTitleText.setText(item.getLocalizedString(mContext,"gameName", item.gameName));
                mNewLabelView.setVisibility(item.hasNewLabel() ? View.VISIBLE : View.INVISIBLE);
                mHotLabelView.setVisibility(item.hasHotLabel() ? View.VISIBLE : View.INVISIBLE);
                mPlayVideoView.setVisibility(item.hasVideo() ? View.VISIBLE : View.INVISIBLE);
                for (int j = 0; j < mStarViews.length; j++) {
                    mStarViews[j].setVisibility(j < item.stars + 0.5f ? View.VISIBLE : View.INVISIBLE);
                    if (mStarViews[j].getVisibility() == View.VISIBLE) {
                        mStarViews[j].setImageAlpha(j < item.stars ? 255 : 127);
                    }
                }

                String locIconURL = item.getLocalizedString(mContext,"iconUrl",item.iconUrl);
                RequestOptions myOptions = new RequestOptions()
                        .transform(new GlideRoundTransform(mIconView.getContext(), 6))
                        .placeholder(R.drawable.ikons_grid_2);
                Glide.with(mContext)
                        .load(locIconURL)
                        .listener(updateIconReqListener)
                        .apply(myOptions)
                        .into(mIconView);
            }

            public BTGameWallItem getGameWallItem() {
                return gameWallItem;
            }

            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.play_video_button) {
                    Log.i("GameWallActivity", "On Click Play Video Button");
                    try {
                        String videoUrl = BTBaseSDK.getHttpProxyCacheServer().getProxyUrl(gameWallItem.getLocalizedString(mContext, "videoUrl", gameWallItem.videoUrl));
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(videoUrl), "video/mp4");
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private Context mContext;
        private LayoutInflater mInflater;

        private List<BTGameWallItem> mGamewallItems;

        public void refreshGamewallItems() {
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            String url = "https://raw.githubusercontent.com/BTBaseNetwork/Resources/master/config/gamewall_config_01.json";
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    BTGameWallConfig btGameWallConfig = new Gson().fromJson(response.toString(),BTGameWallConfig.class);

                    if (btGameWallConfig.configVersion > 0 && btGameWallConfig.items != null) {
                        mGamewallItems = Arrays.asList(btGameWallConfig.items);
                        try {
                            GameWallListAdapter.this.notifyDataSetChanged();
                        }catch (Exception e){
                            Log.e("GameWallListAdapter",e.toString());
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("GameWallListAdapter",error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        }

        public GameWallListAdapter(Context context) {
            mGamewallItems = new ArrayList<>();
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mGamewallItems.size();
        }

        @Override
        public Object getItem(int i) {
            return mGamewallItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            BannerItemViewHolder holder = null;
            if (view == null || view.getTag() == null) {
                holder = new BannerItemViewHolder();
                view = mInflater.inflate(R.layout.gamewall_banner_item, null);
                holder.bindView(view);
            }else {
                holder = (BannerItemViewHolder) view.getTag();
            }
            BTGameWallItem item = (BTGameWallItem) getItem(i);
            holder.setGameWallItem(item);
            view.setTag(holder);
            return view;
        }
    }

    private ListView mGamewallListView;
    private GameWallListAdapter mGameWallListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_wall);
        mGameWallListAdapter = new GameWallListAdapter(getApplicationContext());
        mGamewallListView = findViewById(R.id.gamewall_list);
        mGamewallListView.setAdapter(mGameWallListAdapter);
        mGameWallListAdapter.refreshGamewallItems();
    }
}