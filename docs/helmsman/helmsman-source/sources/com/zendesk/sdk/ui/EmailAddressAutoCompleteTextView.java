package com.zendesk.sdk.ui;

import android.R;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import com.zendesk.logger.Logger;
import com.zendesk.util.StringUtils;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes2.dex */
public class EmailAddressAutoCompleteTextView extends AutoCompleteTextView {
    public EmailAddressAutoCompleteTextView(Context context) {
        this(context, null);
    }

    public EmailAddressAutoCompleteTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialise();
    }

    public EmailAddressAutoCompleteTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialise();
    }

    private void initialise() {
        if (-1 == getContext().checkCallingOrSelfPermission("android.permission.GET_ACCOUNTS") || isInEditMode()) {
            Logger.w(EmailAddressAutoCompleteTextView.class.getCanonicalName(), "You must declare the GET_ACCOUNTS permission to use the auto-complete feature", new Object[0]);
        } else {
            AccountManager accountManager = AccountManager.get(getContext());
            ArrayList arrayList = new ArrayList();
            for (Account account : accountManager.getAccounts()) {
                if (isEmailValid(account.name) && !arrayList.contains(account.name)) {
                    arrayList.add(account.name);
                }
            }
            if (arrayList.size() > 0) {
                setAdapter(new ArrayAdapter(getContext(), R.layout.simple_dropdown_item_1line, arrayList));
            }
        }
        addTextChangedListener(new TextWatcherAdapter() { // from class: com.zendesk.sdk.ui.EmailAddressAutoCompleteTextView.1
            @Override // com.zendesk.sdk.ui.TextWatcherAdapter, android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence != null) {
                    if (EmailAddressAutoCompleteTextView.this.isEmailValid(charSequence)) {
                        EmailAddressAutoCompleteTextView.this.setError(null);
                    } else {
                        EmailAddressAutoCompleteTextView emailAddressAutoCompleteTextView = EmailAddressAutoCompleteTextView.this;
                        emailAddressAutoCompleteTextView.setError(emailAddressAutoCompleteTextView.getContext().getString(com.zendesk.sdk.R.string.contact_fragment_email_validation_error));
                    }
                }
            }
        });
    }

    public boolean isInputValid() {
        String string = getText().toString();
        return StringUtils.hasLength(string) && isEmailValid(string);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isEmailValid(CharSequence charSequence) {
        return Patterns.EMAIL_ADDRESS.matcher(charSequence).matches();
    }
}
