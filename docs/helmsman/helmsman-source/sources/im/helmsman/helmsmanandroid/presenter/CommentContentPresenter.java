package im.helmsman.helmsmanandroid.presenter;

import android.widget.ImageView;
import im.helmsman.helmsmanandroid.dao.MLatLng;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.DowloadCompleteMessage;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.CommentContentResultModel;
import im.helmsman.helmsmanandroid.inet.model.CommentListModel;
import im.helmsman.helmsmanandroid.inet.model.DeleteCommentResultModel;
import im.helmsman.helmsmanandroid.inet.model.DownloadRouteResultModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.view.CommentContentView;
import im.helmsman.helmsmanandroid.utils.DaoUtil;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class CommentContentPresenter extends BasePresenter<CommentContentView> {
    private int messageid;
    private int nextPage = 0;
    private int page = 1;
    private int sinceId;

    public void getMessageById(int i) {
        RequestInetUtils.instance().getMessageById(i, new Callback<CommentContentResultModel>() { // from class: im.helmsman.helmsmanandroid.presenter.CommentContentPresenter.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<CommentContentResultModel> response) {
                if (CommentContentPresenter.this.view != null) {
                    ((CommentContentView) CommentContentPresenter.this.view).getMessageSuccess(response.body());
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                if (CommentContentPresenter.this.view != null) {
                    ((CommentContentView) CommentContentPresenter.this.view).showGetDatasFaileMessage(errorCode.getMessage());
                }
            }
        });
    }

    public void deleteComment(int i) {
        RequestInetUtils.instance().deleteCommentById(i, new Callback<DeleteCommentResultModel>() { // from class: im.helmsman.helmsmanandroid.presenter.CommentContentPresenter.2
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<DeleteCommentResultModel> response) {
                if (CommentContentPresenter.this.view != null) {
                    ((CommentContentView) CommentContentPresenter.this.view).refreshListView();
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                if (CommentContentPresenter.this.view != null) {
                    ((CommentContentView) CommentContentPresenter.this.view).showDeleteCommentFailed(errorCode.getMessage());
                }
            }
        });
    }

    public void getCommentLists(int i, int i2, final int i3) {
        this.messageid = i;
        this.sinceId = i2;
        this.page = i3;
        RequestInetUtils.instance().getAllCommentByMessageId(i, i2, i3, new Callback<CommentListModel>() { // from class: im.helmsman.helmsmanandroid.presenter.CommentContentPresenter.3
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<CommentListModel> response) {
                CommentContentPresenter.this.nextPage = response.body().getNext_page();
                if (CommentContentPresenter.this.view != null) {
                    if (CommentContentPresenter.this.nextPage > i3) {
                        ((CommentContentView) CommentContentPresenter.this.view).enableLoadMore();
                    } else {
                        ((CommentContentView) CommentContentPresenter.this.view).diableLoadMore();
                    }
                    ((CommentContentView) CommentContentPresenter.this.view).initListView(response.body());
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                if (CommentContentPresenter.this.view != null) {
                    ((CommentContentView) CommentContentPresenter.this.view).showGetDatasFaileMessage(errorCode.getMessage());
                }
            }
        });
    }

    public void loadMore() {
        int i = this.page;
        if (i < this.nextPage) {
            int i2 = i + 1;
            this.page = i2;
            getCommentLists(this.messageid, this.sinceId, i2);
        }
    }

    public void downLoadWayPointToPhone(final ImageView imageView, int i) {
        RequestInetUtils.instance().downLoadRouteById(i, -1, new Callback<DownloadRouteResultModel>() { // from class: im.helmsman.helmsmanandroid.presenter.CommentContentPresenter.4
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<DownloadRouteResultModel> response) {
                DownloadRouteResultModel downloadRouteResultModelBody = response.body();
                String route_name = downloadRouteResultModelBody.getRoute_name();
                List<DownloadRouteResultModel.WaypointsBean> waypoints = downloadRouteResultModelBody.getWaypoints();
                ArrayList arrayList = new ArrayList();
                for (DownloadRouteResultModel.WaypointsBean waypointsBean : waypoints) {
                    arrayList.add(new MLatLng(waypointsBean.getLatitude(), waypointsBean.getLongitude()));
                }
                DaoUtil.getInstance().saveWayPointListByImageView(route_name, arrayList, imageView, null);
                if (CommentContentPresenter.this.view != null) {
                    ((CommentContentView) CommentContentPresenter.this.view).showDownLoadRouteSuccessMessage();
                }
                EventBus.getDefault().post(new DowloadCompleteMessage());
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                if (CommentContentPresenter.this.view != null) {
                    ((CommentContentView) CommentContentPresenter.this.view).showDownLoadRouteFailedMessage(errorCode.getMessage());
                }
            }
        });
    }
}
