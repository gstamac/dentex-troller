package com.zendesk.sdk.deeplinking.actions;

/* JADX INFO: loaded from: classes2.dex */
public class ActionRefreshComments extends Action<ActionRefreshCommentsListener, ActionRefreshCommentsData> {

    public interface ActionRefreshCommentsListener extends ActionHandler {
        void refreshComments(ActionRefreshCommentsData actionRefreshCommentsData);
    }

    public ActionRefreshComments(String str) {
        super(ActionType.RELOAD_COMMENT_STREAM, new ActionRefreshCommentsData(str));
    }

    @Override // com.zendesk.sdk.deeplinking.actions.Action
    public void execute(ActionRefreshCommentsListener actionRefreshCommentsListener, ActionRefreshCommentsData actionRefreshCommentsData) {
        actionRefreshCommentsListener.refreshComments(actionRefreshCommentsData);
    }

    @Override // com.zendesk.sdk.deeplinking.actions.Action
    public boolean canHandleData(ActionRefreshCommentsData actionRefreshCommentsData) {
        return getActionData().getRequestId().equals(actionRefreshCommentsData.getRequestId());
    }

    public static class ActionRefreshCommentsData implements ActionData {
        private final String requestId;

        public ActionRefreshCommentsData(String str) {
            this.requestId = str;
        }

        public String getRequestId() {
            return this.requestId;
        }
    }
}
