package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.model.request.UserFieldRequest;
import com.zendesk.sdk.model.request.UserFieldResponse;
import com.zendesk.sdk.model.request.UserResponse;
import com.zendesk.sdk.model.request.UserTagRequest;
import com.zendesk.sdk.network.UserService;
import com.zendesk.service.RetrofitZendeskCallbackAdapter;
import com.zendesk.service.ZendeskCallback;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskUserService {
    private final UserService userService;

    ZendeskUserService(UserService userService) {
        this.userService = userService;
    }

    void addTags(String str, UserTagRequest userTagRequest, ZendeskCallback<UserResponse> zendeskCallback) {
        this.userService.addTags(str, userTagRequest).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }

    void deleteTags(String str, String str2, ZendeskCallback<UserResponse> zendeskCallback) {
        this.userService.deleteTags(str, str2).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }

    void getUserFields(String str, ZendeskCallback<UserFieldResponse> zendeskCallback) {
        this.userService.getUserFields(str).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }

    void setUserFields(String str, UserFieldRequest userFieldRequest, ZendeskCallback<UserResponse> zendeskCallback) {
        this.userService.setUserFields(str, userFieldRequest).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }

    void getUser(String str, ZendeskCallback<UserResponse> zendeskCallback) {
        this.userService.getUser(str).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }
}
