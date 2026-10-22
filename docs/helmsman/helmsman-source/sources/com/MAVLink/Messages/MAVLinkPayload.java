package com.MAVLink.Messages;

import android.util.Log;
import java.nio.ByteBuffer;
import kotlin.UByte;

/* JADX INFO: loaded from: classes.dex */
public class MAVLinkPayload {
    public static final int MAX_PAYLOAD_SIZE = 255;
    private static final short UNSIGNED_BYTE_MAX_VALUE = 255;
    private static final byte UNSIGNED_BYTE_MIN_VALUE = 0;
    private static final long UNSIGNED_INT_MAX_VALUE = 4294967295L;
    private static final int UNSIGNED_INT_MIN_VALUE = 0;
    private static final long UNSIGNED_LONG_MIN_VALUE = 0;
    private static final int UNSIGNED_SHORT_MAX_VALUE = 65535;
    private static final short UNSIGNED_SHORT_MIN_VALUE = 0;
    public int index;
    public final ByteBuffer payload;

    public MAVLinkPayload(int i) {
        if (i > 255) {
            this.payload = ByteBuffer.allocate(255);
        } else {
            this.payload = ByteBuffer.allocate(i);
        }
    }

    public ByteBuffer getData() {
        return this.payload;
    }

    public int size() {
        return this.payload.position();
    }

    public void add(byte b) {
        this.payload.put(b);
    }

    public void resetIndex() {
        this.index = 0;
    }

    public byte getByte() {
        byte b = 0;
        try {
            b = (byte) (0 | (this.payload.get(this.index + 0) & UByte.MAX_VALUE));
            this.index++;
            return b;
        } catch (IndexOutOfBoundsException unused) {
            Log.e("粗错了", getClass().getName() + ".getByte()", new Throwable("getClass().getName()+.getByte()"));
            this.payload.clear();
            return b;
        }
    }

    public short getUnsignedByte() {
        short s = (short) ((this.payload.get(this.index + 0) & UByte.MAX_VALUE) | 0);
        this.index++;
        return s;
    }

    public short getShort() {
        short s = (short) (((short) (((this.payload.get(this.index + 1) & UByte.MAX_VALUE) << 8) | 0)) | (this.payload.get(this.index + 0) & UByte.MAX_VALUE));
        this.index += 2;
        return s;
    }

    public int getUnsignedShort() {
        int i = ((this.payload.get(this.index + 1) & UByte.MAX_VALUE) << 8) | 0 | (this.payload.get(this.index + 0) & UByte.MAX_VALUE);
        this.index += 2;
        return i;
    }

    public int getInt() {
        int i = ((this.payload.get(this.index + 3) & UByte.MAX_VALUE) << 24) | 0 | ((this.payload.get(this.index + 2) & UByte.MAX_VALUE) << 16) | ((this.payload.get(this.index + 1) & UByte.MAX_VALUE) << 8) | (this.payload.get(this.index + 0) & UByte.MAX_VALUE);
        this.index += 4;
        return i;
    }

    public long getUnsignedInt() {
        long j = ((((long) this.payload.get(this.index + 3)) & 255) << 24) | 0 | ((((long) this.payload.get(this.index + 2)) & 255) << 16) | ((((long) this.payload.get(this.index + 1)) & 255) << 8) | (255 & ((long) this.payload.get(this.index + 0)));
        this.index += 4;
        return j;
    }

    public long getLong() {
        long j = ((((long) this.payload.get(this.index + 7)) & 255) << 56) | 0 | ((((long) this.payload.get(this.index + 6)) & 255) << 48) | ((((long) this.payload.get(this.index + 5)) & 255) << 40) | ((((long) this.payload.get(this.index + 4)) & 255) << 32) | ((((long) this.payload.get(this.index + 3)) & 255) << 24) | ((((long) this.payload.get(this.index + 2)) & 255) << 16) | ((((long) this.payload.get(this.index + 1)) & 255) << 8) | (255 & ((long) this.payload.get(this.index + 0)));
        this.index += 8;
        return j;
    }

    public long getUnsignedLong() {
        return getLong();
    }

    public long getLongReverse() {
        long j = ((((long) this.payload.get(this.index + 0)) & 255) << 56) | 0 | ((((long) this.payload.get(this.index + 1)) & 255) << 48) | ((((long) this.payload.get(this.index + 2)) & 255) << 40) | ((((long) this.payload.get(this.index + 3)) & 255) << 32) | ((((long) this.payload.get(this.index + 4)) & 255) << 24) | ((((long) this.payload.get(this.index + 5)) & 255) << 16) | ((((long) this.payload.get(this.index + 6)) & 255) << 8) | (255 & ((long) this.payload.get(this.index + 7)));
        this.index += 8;
        return j;
    }

    public float getFloat() {
        return Float.intBitsToFloat(getInt());
    }

    public void putByte(byte b) {
        add(b);
    }

    public void putUnsignedByte(short s) {
        if (s < 0 || s > 255) {
            throw new IllegalArgumentException("Value is outside of the range of an unsigned byte: " + ((int) s));
        }
        putByte((byte) s);
    }

    public void putShort(short s) {
        add((byte) (s >> 0));
        add((byte) (s >> 8));
    }

    public void putUnsignedShort(int i) {
        if (i < 0 || i > 65535) {
            throw new IllegalArgumentException("Value is outside of the range of an unsigned short: " + i);
        }
        putShort((short) i);
    }

    public void putInt(int i) {
        add((byte) (i >> 0));
        add((byte) (i >> 8));
        add((byte) (i >> 16));
        add((byte) (i >> 24));
    }

    public void putUnsignedInt(long j) {
        if (j < 0 || j > UNSIGNED_INT_MAX_VALUE) {
            throw new IllegalArgumentException("Value is outside of the range of an unsigned int: " + j);
        }
        putInt((int) j);
    }

    public void putLong(long j) {
        add((byte) (j >> 0));
        add((byte) (j >> 8));
        add((byte) (j >> 16));
        add((byte) (j >> 24));
        add((byte) (j >> 32));
        add((byte) (j >> 40));
        add((byte) (j >> 48));
        add((byte) (j >> 56));
    }

    public void putUnsignedLong(long j) {
        if (j < 0) {
            throw new IllegalArgumentException("Value is outside of the range of an unsigned long: " + j);
        }
        putLong(j);
    }

    public void putFloat(float f) {
        putInt(Float.floatToIntBits(f));
    }
}
