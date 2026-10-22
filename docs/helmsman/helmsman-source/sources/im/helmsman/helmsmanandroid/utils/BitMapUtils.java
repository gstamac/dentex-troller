package im.helmsman.helmsmanandroid.utils;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/* JADX INFO: loaded from: classes2.dex */
public class BitMapUtils {

    public interface PhotoCallBack {
        void onFailure();

        void onSuccess(String str);
    }

    public static Bitmap zoomImage(Bitmap bitmap, double d, double d2) {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) d) / width, ((float) d2) / height);
        return Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
    }

    public static Bitmap zoomImage(Context context, int i, double d, double d2) {
        Bitmap bitmapDecodeResource = BitmapFactory.decodeResource(context.getResources(), i);
        float width = bitmapDecodeResource.getWidth();
        float height = bitmapDecodeResource.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) d) / width, ((float) d2) / height);
        return Bitmap.createBitmap(bitmapDecodeResource, 0, 0, (int) width, (int) height, matrix, true);
    }

    public static String photoUriToFilePath(Context context, Uri uri) {
        if (new File(uri.getPath()).exists()) {
            return uri.getPath();
        }
        Cursor cursorLoadInBackground = new CursorLoader(context, uri, new String[]{"_data"}, null, null, null).loadInBackground();
        int columnIndexOrThrow = cursorLoadInBackground.getColumnIndexOrThrow("_data");
        cursorLoadInBackground.moveToFirst();
        return cursorLoadInBackground.getString(columnIndexOrThrow);
    }

    public static void getPhotoURLByAlbum(Context context, Intent intent, PhotoCallBack photoCallBack) {
        if (intent == null) {
            photoCallBack.onFailure();
            return;
        }
        Uri data = intent.getData();
        if (data == null) {
            photoCallBack.onFailure();
            return;
        }
        if (new File(data.getPath()).exists()) {
            photoCallBack.onSuccess(data.getPath());
            return;
        }
        if (DocumentsContract.isDocumentUri(context, data)) {
            String[] strArrSplit = DocumentsContract.getDocumentId(data).split(":");
            if (strArrSplit.length > 1) {
                String[] strArr = {"_data"};
                Cursor cursorQuery = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, "_id=?", new String[]{strArrSplit[1]}, null);
                if (cursorQuery.moveToNext()) {
                    photoCallBack.onSuccess(cursorQuery.getString(cursorQuery.getColumnIndex(strArr[0])));
                    cursorQuery.close();
                    return;
                }
                Cursor cursorQuery2 = context.getContentResolver().query(data, new String[]{"_data"}, null, null, null);
                if (cursorQuery2.moveToNext()) {
                    photoCallBack.onSuccess(cursorQuery2.getString(cursorQuery2.getColumnIndex("_data")));
                    return;
                } else {
                    cursorQuery.close();
                    return;
                }
            }
            photoCallBack.onFailure();
            return;
        }
        photoCallBack.onSuccess(photoUriToFilePath(context, data));
    }

    public static byte[] bitmapToArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        bitmap.recycle();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    public static File scal(Uri uri) throws Throwable {
        String path = uri.getPath();
        File file = new File(path);
        long length = file.length();
        if (length < 204800) {
            return file;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int i = options.outHeight;
        int i2 = options.outWidth;
        double dSqrt = Math.sqrt(length / 204800.0f);
        options.outHeight = (int) (((double) i) / dSqrt);
        options.outWidth = (int) (((double) i2) / dSqrt);
        options.inSampleSize = (int) (dSqrt + 0.5d);
        options.inJustDecodeBounds = false;
        Bitmap bitmapDecodeFile = BitmapFactory.decodeFile(path, options);
        File file2 = new File(createImageFile().getPath());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            bitmapDecodeFile.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!bitmapDecodeFile.isRecycled()) {
            bitmapDecodeFile.recycle();
            return file2;
        }
        File file3 = new File(createImageFile().getPath());
        copyFileUsingFileChannels(file2, file3);
        return file3;
    }

    private static Uri createImageFile() {
        File fileCreateTempFile;
        String str = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            fileCreateTempFile = File.createTempFile(str, ".png");
        } catch (IOException e) {
            e.printStackTrace();
            fileCreateTempFile = null;
        }
        return Uri.fromFile(fileCreateTempFile);
    }

    private static void copyFileUsingFileChannels(File file, File file2) throws Throwable {
        FileChannel fileChannel;
        FileChannel channel;
        FileChannel channel2 = null;
        try {
            channel = new FileInputStream(file).getChannel();
            try {
                channel2 = new FileOutputStream(file2).getChannel();
                channel2.transferFrom(channel, 0L, channel.size());
            } catch (IOException e) {
                e = e;
                FileChannel fileChannel2 = channel2;
                channel2 = channel;
                fileChannel = fileChannel2;
                try {
                    e.printStackTrace();
                    FileChannel fileChannel3 = channel2;
                    channel2 = fileChannel;
                    channel = fileChannel3;
                } catch (Throwable th) {
                    th = th;
                    try {
                        channel2.close();
                        fileChannel.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                FileChannel fileChannel4 = channel2;
                channel2 = channel;
                fileChannel = fileChannel4;
                channel2.close();
                fileChannel.close();
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            fileChannel = null;
        } catch (Throwable th3) {
            th = th3;
            fileChannel = null;
        }
        try {
            channel.close();
            channel2.close();
        } catch (IOException e4) {
            e4.printStackTrace();
        }
    }
}
