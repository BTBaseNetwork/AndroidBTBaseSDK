package mobi.btbase.btbasesdk;

import android.content.Context;
import android.media.Image;
import android.os.Debug;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import mobi.btbase.btbasesdk.common.GlideRoundTransform;
import mobi.btbase.btbasesdk.models.GameWallItem;

public class GameWallActivity extends AppCompatActivity {

    class GameWallListAdapter extends BaseAdapter {

        class BannerItemViewHolder implements View.OnClickListener {
            ImageView mIconView;
            TextView mTitleText;
            ImageView[] mStarViews;
            ImageView mPlayVideoView;
            ImageView mHotLabelView;
            ImageView mNewLabelView;
            private GameWallItem gameWallItem;

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

            public void setGameWallItem(GameWallItem gameWallItem) {
                this.gameWallItem = gameWallItem;
                updateViews();
            }

            private void updateViews() {
                GameWallItem item = getGameWallItem();
                mTitleText.setText(item.getLocalizedString("gameName", item.gameName));
                mNewLabelView.setVisibility(item.hasNewLabel() ? View.VISIBLE : View.INVISIBLE);
                mHotLabelView.setVisibility(item.hasHotLabel() ? View.VISIBLE : View.INVISIBLE);
                for (int j = 0; j < mStarViews.length; j++) {
                    mStarViews[j].setVisibility(j < item.stars + 0.5f ? View.VISIBLE : View.INVISIBLE);
                    if (mStarViews[j].getVisibility() == View.VISIBLE) {
                        mStarViews[j].setImageAlpha(j < item.stars ? 255 : 127);
                    }
                }
                RequestOptions myOptions = new RequestOptions()
                        .transform(new GlideRoundTransform(mIconView.getContext(), 6))
                        .placeholder(R.drawable.ikons_grid_2);
                Glide.with(mContext).load(item.iconUrl).apply(myOptions).into(mIconView);
            }

            public GameWallItem getGameWallItem() {
                return gameWallItem;
            }

            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.play_video_button) {
                    Log.i("GameWallActivity", "On Click Play Video Button");
                }
            }
        }

        private Context mContext;
        private LayoutInflater mInflater;

        private List<GameWallItem> mGamewallItems;

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
            }
            GameWallItem item = (GameWallItem) getItem(i);
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
    }
}