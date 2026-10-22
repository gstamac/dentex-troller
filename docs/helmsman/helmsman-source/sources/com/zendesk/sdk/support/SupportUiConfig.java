package com.zendesk.sdk.support;

import android.os.Parcel;
import android.os.Parcelable;
import com.zendesk.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class SupportUiConfig implements Parcelable {
    public static final Parcelable.Creator<SupportUiConfig> CREATOR = new Parcelable.Creator<SupportUiConfig>() { // from class: com.zendesk.sdk.support.SupportUiConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SupportUiConfig createFromParcel(Parcel parcel) {
            return new SupportUiConfig(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SupportUiConfig[] newArray(int i) {
            return new SupportUiConfig[i];
        }
    };
    private static final String LOG_TAG = "SupportUiConfig";
    private boolean addListPaddingBottom;
    private List<Long> categoryIds;
    private boolean collapseCategories;
    private String[] labelNames;
    private List<Long> sectionIds;
    private boolean showContactUsButton;
    private boolean showConversationsMenuButton;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public List<Long> getCategoryIds() {
        return this.categoryIds;
    }

    public List<Long> getSectionIds() {
        return this.sectionIds;
    }

    public String[] getLabelNames() {
        return this.labelNames;
    }

    boolean isShowContactUsButton() {
        return this.showContactUsButton;
    }

    public boolean isAddListPaddingBottom() {
        return this.addListPaddingBottom;
    }

    public boolean isCollapseCategories() {
        return this.collapseCategories;
    }

    public boolean isShowConversationsMenuButton() {
        return this.showConversationsMenuButton;
    }

    private SupportUiConfig(Builder builder) {
        this.categoryIds = builder.categoryIds;
        this.sectionIds = builder.sectionIds;
        this.labelNames = builder.labelNames;
        this.showContactUsButton = builder.showContactUsButton;
        this.addListPaddingBottom = builder.addListPaddingBottom;
        this.collapseCategories = builder.collapseCategories;
        this.showConversationsMenuButton = builder.showConversationsMenuButton;
    }

    static class Builder {
        private boolean collapseCategories;
        private List<Long> categoryIds = Collections.emptyList();
        private List<Long> sectionIds = Collections.emptyList();
        private String[] labelNames = new String[0];
        private boolean showContactUsButton = true;
        private boolean addListPaddingBottom = true;
        private boolean showConversationsMenuButton = true;

        Builder() {
        }

        Builder withCategoryIds(List<Long> list) {
            this.categoryIds = CollectionUtils.copyOf(list);
            return this;
        }

        Builder withSectionIds(List<Long> list) {
            this.sectionIds = CollectionUtils.copyOf(list);
            return this;
        }

        Builder withLabelNames(String[] strArr) {
            if (strArr != null) {
                this.labelNames = (String[]) strArr.clone();
            }
            return this;
        }

        Builder withShowContactUsButton(boolean z) {
            this.showContactUsButton = z;
            return this;
        }

        Builder withAddListPaddingBottom(boolean z) {
            this.addListPaddingBottom = z;
            return this;
        }

        Builder withCollapseCategories(boolean z) {
            this.collapseCategories = z;
            return this;
        }

        Builder withShowConversationsMenuButton(boolean z) {
            this.showConversationsMenuButton = z;
            return this;
        }

        SupportUiConfig build() {
            return new SupportUiConfig(this);
        }
    }

    protected SupportUiConfig(Parcel parcel) {
        ArrayList arrayList = new ArrayList();
        this.categoryIds = arrayList;
        parcel.readList(arrayList, null);
        ArrayList arrayList2 = new ArrayList();
        this.sectionIds = arrayList2;
        parcel.readList(arrayList2, null);
        this.labelNames = parcel.createStringArray();
        boolean[] zArrCreateBooleanArray = parcel.createBooleanArray();
        int length = zArrCreateBooleanArray.length;
        if (length != 1) {
            if (length != 2) {
                if (length != 3) {
                    if (length != 4) {
                        return;
                    } else {
                        this.showConversationsMenuButton = zArrCreateBooleanArray[3];
                    }
                }
                this.collapseCategories = zArrCreateBooleanArray[2];
            }
            this.addListPaddingBottom = zArrCreateBooleanArray[1];
        }
        this.showContactUsButton = zArrCreateBooleanArray[0];
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(this.categoryIds);
        parcel.writeList(this.sectionIds);
        parcel.writeStringArray(this.labelNames);
        parcel.writeBooleanArray(new boolean[]{this.showContactUsButton, this.addListPaddingBottom, this.collapseCategories, this.showConversationsMenuButton});
    }
}
