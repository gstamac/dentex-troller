package com.zendesk.sdk.deeplinking.actions;

import com.zendesk.sdk.deeplinking.actions.ActionData;
import com.zendesk.sdk.deeplinking.actions.ActionHandler;

/* JADX INFO: loaded from: classes2.dex */
public abstract class Action<Handler extends ActionHandler, Data extends ActionData> {
    private Data mActionData;
    private ActionType mActionType;

    public abstract boolean canHandleData(Data data);

    public abstract void execute(Handler handler, Data data);

    public Action(ActionType actionType, Data data) {
        this.mActionType = actionType;
        this.mActionData = data;
    }

    public ActionType getActionType() {
        return this.mActionType;
    }

    public Data getActionData() {
        return this.mActionData;
    }
}
