package com.google.zxing.pdf417.decoder;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;

/* JADX INFO: loaded from: classes2.dex */
final class BoundingBox {
    private ResultPoint bottomLeft;
    private ResultPoint bottomRight;
    private BitMatrix image;
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;
    private ResultPoint topLeft;
    private ResultPoint topRight;

    BoundingBox(BitMatrix bitMatrix, ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3, ResultPoint resultPoint4) throws NotFoundException {
        if ((resultPoint == null && resultPoint3 == null) || ((resultPoint2 == null && resultPoint4 == null) || ((resultPoint != null && resultPoint2 == null) || (resultPoint3 != null && resultPoint4 == null)))) {
            throw NotFoundException.getNotFoundInstance();
        }
        init(bitMatrix, resultPoint, resultPoint2, resultPoint3, resultPoint4);
    }

    BoundingBox(BoundingBox boundingBox) {
        init(boundingBox.image, boundingBox.topLeft, boundingBox.bottomLeft, boundingBox.topRight, boundingBox.bottomRight);
    }

    private void init(BitMatrix bitMatrix, ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3, ResultPoint resultPoint4) {
        this.image = bitMatrix;
        this.topLeft = resultPoint;
        this.bottomLeft = resultPoint2;
        this.topRight = resultPoint3;
        this.bottomRight = resultPoint4;
        calculateMinMaxValues();
    }

    static BoundingBox merge(BoundingBox boundingBox, BoundingBox boundingBox2) throws NotFoundException {
        if (boundingBox == null) {
            return boundingBox2;
        }
        return boundingBox2 == null ? boundingBox : new BoundingBox(boundingBox.image, boundingBox.topLeft, boundingBox.bottomLeft, boundingBox2.topRight, boundingBox2.bottomRight);
    }

    /* JADX WARN: Code duplicated, block: B:17:0x002d A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:18:0x002f  */
    /* JADX WARN: Code duplicated, block: B:19:0x0032  */
    /* JADX WARN: Code duplicated, block: B:22:0x0042  */
    /* JADX WARN: Code duplicated, block: B:25:0x0056  */
    /* JADX WARN: Code duplicated, block: B:26:0x0058  */
    /* JADX WARN: Code duplicated, block: B:27:0x005b  */
    BoundingBox addMissingRows(int i, int i2, boolean z) throws NotFoundException {
        ResultPoint resultPoint;
        ResultPoint resultPoint2;
        ResultPoint resultPoint3;
        ResultPoint resultPoint4;
        ResultPoint resultPoint5;
        int y;
        ResultPoint resultPoint6;
        ResultPoint resultPoint7 = this.topLeft;
        ResultPoint resultPoint8 = this.bottomLeft;
        ResultPoint resultPoint9 = this.topRight;
        ResultPoint resultPoint10 = this.bottomRight;
        if (i > 0) {
            ResultPoint resultPoint11 = z ? resultPoint7 : resultPoint9;
            int y2 = ((int) resultPoint11.getY()) - i;
            if (y2 < 0) {
                y2 = 0;
            }
            ResultPoint resultPoint12 = new ResultPoint(resultPoint11.getX(), y2);
            if (z) {
                resultPoint = resultPoint12;
            } else {
                resultPoint2 = resultPoint12;
                resultPoint = resultPoint7;
            }
            if (i2 > 0) {
                if (z) {
                    resultPoint5 = this.bottomLeft;
                } else {
                    resultPoint5 = this.bottomRight;
                }
                y = ((int) resultPoint5.getY()) + i2;
                if (y >= this.image.getHeight()) {
                    y = this.image.getHeight() - 1;
                }
                resultPoint6 = new ResultPoint(resultPoint5.getX(), y);
                if (z) {
                    resultPoint3 = resultPoint6;
                } else {
                    resultPoint4 = resultPoint6;
                    resultPoint3 = resultPoint8;
                }
                calculateMinMaxValues();
                return new BoundingBox(this.image, resultPoint, resultPoint3, resultPoint2, resultPoint4);
            }
            resultPoint3 = resultPoint8;
            resultPoint4 = resultPoint10;
            calculateMinMaxValues();
            return new BoundingBox(this.image, resultPoint, resultPoint3, resultPoint2, resultPoint4);
        }
        resultPoint = resultPoint7;
        resultPoint2 = resultPoint9;
        if (i2 > 0) {
            if (z) {
                resultPoint5 = this.bottomLeft;
            } else {
                resultPoint5 = this.bottomRight;
            }
            y = ((int) resultPoint5.getY()) + i2;
            if (y >= this.image.getHeight()) {
                y = this.image.getHeight() - 1;
            }
            resultPoint6 = new ResultPoint(resultPoint5.getX(), y);
            if (z) {
                resultPoint3 = resultPoint6;
            } else {
                resultPoint4 = resultPoint6;
                resultPoint3 = resultPoint8;
            }
            calculateMinMaxValues();
            return new BoundingBox(this.image, resultPoint, resultPoint3, resultPoint2, resultPoint4);
        }
        resultPoint3 = resultPoint8;
        resultPoint4 = resultPoint10;
        calculateMinMaxValues();
        return new BoundingBox(this.image, resultPoint, resultPoint3, resultPoint2, resultPoint4);
    }

    private void calculateMinMaxValues() {
        if (this.topLeft == null) {
            this.topLeft = new ResultPoint(0.0f, this.topRight.getY());
            this.bottomLeft = new ResultPoint(0.0f, this.bottomRight.getY());
        } else if (this.topRight == null) {
            this.topRight = new ResultPoint(this.image.getWidth() - 1, this.topLeft.getY());
            this.bottomRight = new ResultPoint(this.image.getWidth() - 1, this.bottomLeft.getY());
        }
        this.minX = (int) Math.min(this.topLeft.getX(), this.bottomLeft.getX());
        this.maxX = (int) Math.max(this.topRight.getX(), this.bottomRight.getX());
        this.minY = (int) Math.min(this.topLeft.getY(), this.topRight.getY());
        this.maxY = (int) Math.max(this.bottomLeft.getY(), this.bottomRight.getY());
    }

    int getMinX() {
        return this.minX;
    }

    int getMaxX() {
        return this.maxX;
    }

    int getMinY() {
        return this.minY;
    }

    int getMaxY() {
        return this.maxY;
    }

    ResultPoint getTopLeft() {
        return this.topLeft;
    }

    ResultPoint getTopRight() {
        return this.topRight;
    }

    ResultPoint getBottomLeft() {
        return this.bottomLeft;
    }

    ResultPoint getBottomRight() {
        return this.bottomRight;
    }
}
