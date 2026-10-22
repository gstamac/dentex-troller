package com.zendesk.sdk.deeplinking;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import com.zendesk.sdk.deeplinking.actions.Action;
import com.zendesk.sdk.deeplinking.actions.ActionData;
import com.zendesk.sdk.deeplinking.actions.ActionHandler;
import com.zendesk.sdk.deeplinking.actions.ActionRefreshComments;
import com.zendesk.sdk.deeplinking.actions.ActionType;
import com.zendesk.sdk.deeplinking.targets.ArticleConfiguration;
import com.zendesk.sdk.deeplinking.targets.DeepLinkingTargetArticle;
import com.zendesk.sdk.deeplinking.targets.DeepLinkingTargetRequest;
import com.zendesk.sdk.deeplinking.targets.RequestConfiguration;
import com.zendesk.sdk.model.helpcenter.Article;
import com.zendesk.sdk.model.helpcenter.SimpleArticle;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes2.dex */
public enum ZendeskDeepLinking {
    INSTANCE;

    private WeakHashMap<ActionHandler, Action[]> mActiveHandlers = new WeakHashMap<>();

    ZendeskDeepLinking() {
    }

    public void registerAction(ActionHandler actionHandler, Action... actionArr) {
        this.mActiveHandlers.put(actionHandler, actionArr);
    }

    public void unregisterAction(ActionHandler actionHandler) {
        this.mActiveHandlers.remove(actionHandler);
    }

    public boolean refreshComments(String str) {
        ActionRefreshComments.ActionRefreshCommentsData actionRefreshCommentsData = new ActionRefreshComments.ActionRefreshCommentsData(str);
        boolean zCanHandle = canHandle(ActionType.RELOAD_COMMENT_STREAM, actionRefreshCommentsData);
        if (zCanHandle) {
            execute(ActionType.RELOAD_COMMENT_STREAM, actionRefreshCommentsData);
        }
        return zCanHandle;
    }

    public Intent getRequestIntent(Context context, String str, String str2, ArrayList<Intent> arrayList, Intent intent) {
        return new DeepLinkingTargetRequest().getIntent(context, new RequestConfiguration(str, str2, arrayList, intent));
    }

    public Intent getRequestIntent(Context context, String str, String str2, ArrayList<Intent> arrayList, Intent intent, String str3, String str4, String str5) {
        return new DeepLinkingTargetRequest().getIntent(context, new RequestConfiguration(str, str2, arrayList, intent), str3, str4, str5);
    }

    public Intent getArticleIntent(Context context, Article article, ArrayList<Intent> arrayList, Intent intent) {
        return new DeepLinkingTargetArticle().getIntent(context, new ArticleConfiguration(article, arrayList, intent));
    }

    public Intent getArticleIntent(Context context, Article article, ArrayList<Intent> arrayList, Intent intent, String str, String str2, String str3) {
        return new DeepLinkingTargetArticle().getIntent(context, new ArticleConfiguration(article, arrayList, intent), str, str2, str3);
    }

    public Intent getArticleIntent(Context context, SimpleArticle simpleArticle, ArrayList<Intent> arrayList, Intent intent) {
        return new DeepLinkingTargetArticle().getIntent(context, new ArticleConfiguration(simpleArticle, arrayList, intent));
    }

    public Intent getArticleIntent(Context context, SimpleArticle simpleArticle, ArrayList<Intent> arrayList, Intent intent, String str, String str2, String str3) {
        return new DeepLinkingTargetArticle().getIntent(context, new ArticleConfiguration(simpleArticle, arrayList, intent), str, str2, str3);
    }

    private void execute(ActionType actionType, final ActionData actionData) {
        for (final Map.Entry<ActionHandler, Action[]> entry : getCompatibleEntries(actionType, actionData)) {
            for (final Action action : entry.getValue()) {
                new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.zendesk.sdk.deeplinking.ZendeskDeepLinking.1
                    @Override // java.lang.Runnable
                    public void run() {
                        action.execute((ActionHandler) entry.getKey(), actionData);
                    }
                });
            }
        }
    }

    private List<Map.Entry<ActionHandler, Action[]>> getCompatibleEntries(ActionType actionType, ActionData actionData) {
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<ActionHandler, Action[]> entry : this.mActiveHandlers.entrySet()) {
            ArrayList arrayList2 = new ArrayList();
            for (Action action : entry.getValue()) {
                if (action.getActionType() == actionType && action.canHandleData(actionData)) {
                    arrayList2.add(action);
                }
            }
            if (arrayList2.size() > 0) {
                arrayList.add(new AbstractMap.SimpleEntry(entry.getKey(), arrayList2.toArray(new Action[arrayList2.size()])));
            }
        }
        return arrayList;
    }

    private boolean canHandle(ActionType actionType, ActionData actionData) {
        return getCompatibleEntries(actionType, actionData).size() > 0;
    }
}
