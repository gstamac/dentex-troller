package com.zendesk.sdk.network.impl;

/* JADX INFO: loaded from: classes2.dex */
class ConfigurationRuntimeException extends RuntimeException {
    private static final String MESSAGE = "Zendesk configuration error occurred";

    ConfigurationRuntimeException() {
        super(MESSAGE);
    }

    ConfigurationRuntimeException(String str) {
        super(str);
    }
}
