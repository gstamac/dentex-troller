package org.litepal.tablemanager.typechange;

import androidx.constraintlayout.core.motion.utils.TypedValues;

/* JADX INFO: loaded from: classes2.dex */
public class DecimalOrm extends OrmChange {
    @Override // org.litepal.tablemanager.typechange.OrmChange
    public String object2Relation(String str) {
        if (str == null) {
            return null;
        }
        if (str.equals(TypedValues.Custom.S_FLOAT) || str.equals("java.lang.Float") || str.equals("double") || str.equals("java.lang.Double")) {
            return "real";
        }
        return null;
    }
}
