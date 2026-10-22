package org.litepal.tablemanager.typechange;

import androidx.constraintlayout.core.motion.utils.TypedValues;

/* JADX INFO: loaded from: classes2.dex */
public class BooleanOrm extends OrmChange {
    @Override // org.litepal.tablemanager.typechange.OrmChange
    public String object2Relation(String str) {
        if (str == null) {
            return null;
        }
        if (str.equals(TypedValues.Custom.S_BOOLEAN) || str.equals("java.lang.Boolean")) {
            return TypedValues.Custom.S_INT;
        }
        return null;
    }
}
