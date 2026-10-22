package com.zendesk.belvedere;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;
import androidx.core.content.FileProvider;
import java.util.Arrays;

/* JADX INFO: loaded from: classes2.dex */
public class BelvedereFileProvider extends FileProvider {
    private static final String LOG_TAG = "BelvedereFileProvider";

    @Override // androidx.core.content.FileProvider, android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        Cursor cursorQuery = super.query(uri, strArr, str, strArr2, str2);
        if (cursorQuery == null) {
            Log.w(LOG_TAG, "Not able to apply workaround, super.query(...) returned null");
            return null;
        }
        String[] columnNames = cursorQuery.getColumnNames();
        MatrixCursor matrixCursor = new MatrixCursor(columnNamesWithData(columnNames), cursorQuery.getCount());
        cursorQuery.moveToPosition(-1);
        while (cursorQuery.moveToNext()) {
            MatrixCursor.RowBuilder rowBuilderNewRow = matrixCursor.newRow();
            for (int i = 0; i < columnNames.length; i++) {
                rowBuilderNewRow.add(cursorQuery.getString(i));
            }
        }
        cursorQuery.close();
        return matrixCursor;
    }

    private String[] columnNamesWithData(String[] strArr) {
        for (String str : strArr) {
            if ("_data".equals(str)) {
                return strArr;
            }
        }
        String[] strArr2 = (String[]) Arrays.copyOf(strArr, strArr.length + 1);
        strArr2[strArr.length] = "_data";
        return strArr2;
    }
}
