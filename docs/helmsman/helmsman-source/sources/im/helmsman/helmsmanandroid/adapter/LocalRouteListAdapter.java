package im.helmsman.helmsmanandroid.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.MAVLink.common.msg_set_actuator_control_target;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.dao.RouteModel;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.GetRouteScreenShostResultModel;
import im.helmsman.helmsmanandroid.inet.model.UserMe;
import im.helmsman.helmsmanandroid.ui.activity.SendMessageActivity;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.ui.fragment.MainDrawerMenuFragment;
import im.helmsman.helmsmanandroid.utils.AutoLayoutExtendsUtils;
import im.helmsman.helmsmanandroid.utils.CoordinateParserUtil;
import im.helmsman.helmsmanandroid.utils.DaoUtil;
import im.helmsman.helmsmanandroid.utils.TimeUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.io.File;
import java.util.List;
import okhttp3.Call;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class LocalRouteListAdapter extends BaseAdapter implements View.OnClickListener, AbsListView.OnScrollListener {
    private static LocalRouteListAdapter adapter = null;
    public static int visiblity = 8;
    private List<RouteModel> datas;
    private boolean isScrolling;
    private OnListButtonClickListener listener;
    private Context mContext;
    private int animationPosition = 0;
    private RequestOptions noneCacheOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);

    public interface OnListButtonClickListener {
        void onButtonClickListener(int i, View view, RouteModel routeModel);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
    }

    @Override // android.widget.AbsListView.OnScrollListener
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
    }

    public LocalRouteListAdapter(Context context, List<RouteModel> list, ListView listView) {
        this.datas = list;
        this.mContext = context;
        adapter = this;
        listView.setOnScrollListener(this);
    }

    public static LocalRouteListAdapter getInstance() {
        return adapter;
    }

    public static void onDestroy() {
        adapter = null;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.datas.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.datas.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Holder holder;
        final RouteModel routeModel = (RouteModel) getItem(i);
        if (view == null) {
            view = View.inflate(this.mContext, R.layout.item_refresh_listview, null);
            holder = new Holder();
            holder.ivImageView = (ImageView) view.findViewById(R.id.iv_waypoint_image);
            holder.tvName = (TextView) view.findViewById(R.id.tv_way_point_name);
            holder.btnShare = (TextView) view.findViewById(R.id.tv_localroute_share);
            holder.tvNavi = (TextView) view.findViewById(R.id.tv_localroute_navi);
            holder.cbCheck = (CheckBox) view.findViewById(R.id.cb_localroute_check_route);
            holder.tvTime = (TextView) view.findViewById(R.id.tv_item_refresh_time);
            holder.tvDistance = (TextView) view.findViewById(R.id.tv_localroute_dis);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.tvNavi.setOnClickListener(new MyOnclickListener(i));
        holder.btnShare.setOnTouchListener(new ShareButtonOnTouchListener(i));
        String timeElapse = TimeUtils.getTimeElapse(routeModel.getDate());
        String strComputerAllPolineDis = CoordinateParserUtil.computerAllPolineDis(routeModel.getWaypoint());
        final String encodePolyline = routeModel.getEncodePolyline();
        boolean zIsCheck = routeModel.isCheck();
        holder.tvDistance.setText(strComputerAllPolineDis);
        String name = routeModel.getName();
        holder.cbCheck.setVisibility(visiblity);
        holder.cbCheck.setChecked(zIsCheck);
        if (8 == visiblity) {
            holder.cbCheck.setChecked(false);
        }
        holder.tvTime.setText(timeElapse);
        if (name == null) {
            name = this.mContext.getString(R.string.edit_no_name);
        }
        if (routeModel.getIdentifier() == null) {
            holder.btnShare.setVisibility(0);
        } else {
            holder.btnShare.setVisibility(8);
        }
        holder.tvName.setText(name);
        File file = new File(DaoUtil.imagePath + "/" + routeModel.getPictureName());
        initDefaultImage(holder.ivImageView);
        if (file.exists()) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
            holder.ivImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.ivImageView.setLayoutParams(layoutParams);
            Glide.with(this.mContext).load(file.getPath()).apply((BaseRequestOptions<?>) this.noneCacheOptions).into(holder.ivImageView);
        } else if (UserMe.isLogin() && !this.isScrolling) {
            RequestInetUtils.instance().getLocalRouteScreenhost(routeModel, new Callback<GetRouteScreenShostResultModel>() { // from class: im.helmsman.helmsmanandroid.adapter.LocalRouteListAdapter.1
                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onResponse(Response<GetRouteScreenShostResultModel> response) {
                    if (routeModel.getEncodePolyline().equals(response.body().getEncoded_polyline())) {
                        String str = encodePolyline + ".png";
                        routeModel.setPictureName(str);
                        routeModel.save();
                        OkHttpUtils.get().url(response.body().getScreenshot_url()).build().execute(new FileCallBack(DaoUtil.imagePath, str) { // from class: im.helmsman.helmsmanandroid.adapter.LocalRouteListAdapter.1.1
                            @Override // com.zhy.http.okhttp.callback.Callback
                            public void onError(Call call, Exception exc, int i2) {
                                LocalRouteListAdapter.this.initDefaultImage(holder.ivImageView);
                            }

                            @Override // com.zhy.http.okhttp.callback.Callback
                            public void onResponse(File file2, int i2) {
                                FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-1, -1);
                                holder.ivImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                holder.ivImageView.setLayoutParams(layoutParams2);
                                if ((LocalRouteListAdapter.this.mContext instanceof Activity) && ((Activity) LocalRouteListAdapter.this.mContext).isDestroyed()) {
                                    return;
                                }
                                Glide.with(LocalRouteListAdapter.this.mContext).load(file2).apply((BaseRequestOptions<?>) LocalRouteListAdapter.this.noneCacheOptions).into(holder.ivImageView);
                            }
                        });
                    }
                }

                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onFailure(ErrorCode errorCode) {
                    LocalRouteListAdapter.this.initDefaultImage(holder.ivImageView);
                }
            });
        }
        if (i == this.animationPosition) {
            view.startAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.list_anim));
            this.animationPosition = -1;
        }
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initDefaultImage(ImageView imageView) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(AutoLayoutExtendsUtils.widthConvert(960), AutoLayoutExtendsUtils.heightConvert(560));
        layoutParams.setMargins(0, AutoLayoutExtendsUtils.heightConvert(msg_set_actuator_control_target.MAVLINK_MSG_ID_SET_ACTUATOR_CONTROL_TARGET), 0, 0);
        layoutParams.gravity = 1;
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(R.drawable.icon_way_point_no_map_cicle);
    }

    public void setCheckBoxVisiblity(int i) {
        visiblity = i;
        notifyDataSetChanged();
    }

    @Override // android.widget.AbsListView.OnScrollListener
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i == 0) {
            this.isScrolling = false;
            notifyDataSetChanged();
        } else {
            this.isScrolling = true;
        }
    }

    private class Holder {
        TextView btnShare;
        CheckBox cbCheck;
        ImageView ivImageView;
        TextView tvDistance;
        TextView tvName;
        TextView tvNavi;
        TextView tvTime;

        private Holder() {
        }
    }

    public void setOnListButtonClickListener(OnListButtonClickListener onListButtonClickListener) {
        this.listener = onListButtonClickListener;
    }

    class MyOnclickListener implements View.OnClickListener {
        private int position;

        public MyOnclickListener(int i) {
            this.position = i;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (LocalRouteListAdapter.this.listener != null) {
                OnListButtonClickListener onListButtonClickListener = LocalRouteListAdapter.this.listener;
                int i = this.position;
                onListButtonClickListener.onButtonClickListener(i, view, (RouteModel) LocalRouteListAdapter.this.getItem(i));
            }
        }
    }

    class ShareButtonOnTouchListener implements View.OnTouchListener {
        private int position;

        public ShareButtonOnTouchListener(int i) {
            this.position = i;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Log.e("getAction", motionEvent.getAction() + "");
            if (motionEvent.getAction() == 0) {
                return true;
            }
            if (1 != motionEvent.getAction()) {
                return false;
            }
            if (UserMe.isLogin()) {
                RouteModel routeModel = (RouteModel) LocalRouteListAdapter.this.getItem(this.position);
                Intent intent = new Intent(LocalRouteListAdapter.this.mContext, (Class<?>) SendMessageActivity.class);
                intent.putExtra("routeId", routeModel.getId());
                intent.putExtra("model", (short) 2);
                String name = routeModel.getName();
                if (TextUtils.isEmpty(name)) {
                    name = MyApplication.getInstance().getString(R.string.edit_no_name);
                }
                intent.putExtra("routename", name);
                LocalRouteListAdapter.this.mContext.startActivity(intent);
            } else {
                ViewUtils.ShowToast(R.string.pulltorefreshlistadapter_pleaselogin);
                Base2Activity.getTopActivity().finish();
                MainDrawerMenuFragment mainDrawerMenuFragment = MainDrawerMenuFragment.getInstance();
                if (mainDrawerMenuFragment != null) {
                    mainDrawerMenuFragment.showLogin();
                }
            }
            return true;
        }
    }
}
