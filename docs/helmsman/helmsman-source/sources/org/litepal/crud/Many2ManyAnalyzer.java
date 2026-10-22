package org.litepal.crud;

import android.database.Cursor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import org.litepal.crud.model.AssociationsInfo;
import org.litepal.tablemanager.Connector;
import org.litepal.util.BaseUtility;
import org.litepal.util.DBUtility;

/* JADX INFO: loaded from: classes2.dex */
public class Many2ManyAnalyzer extends AssociationsAnalyzer {
    void analyze(DataSupport dataSupport, AssociationsInfo associationsInfo) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Collection<DataSupport> associatedModels = getAssociatedModels(dataSupport, associationsInfo);
        declareAssociations(dataSupport, associationsInfo);
        if (associatedModels != null) {
            for (DataSupport dataSupport2 : associatedModels) {
                Collection<DataSupport> collectionCheckAssociatedModelCollection = checkAssociatedModelCollection(getReverseAssociatedModels(dataSupport2, associationsInfo), associationsInfo.getAssociateSelfFromOtherModel());
                addNewModelForAssociatedModel(collectionCheckAssociatedModelCollection, dataSupport);
                setReverseAssociatedModels(dataSupport2, associationsInfo, collectionCheckAssociatedModelCollection);
                dealAssociatedModel(dataSupport, dataSupport2);
            }
        }
    }

    private void declareAssociations(DataSupport dataSupport, AssociationsInfo associationsInfo) {
        dataSupport.addEmptyModelForJoinTable(getAssociatedTableName(associationsInfo));
    }

    private void addNewModelForAssociatedModel(Collection<DataSupport> collection, DataSupport dataSupport) {
        if (collection.contains(dataSupport)) {
            return;
        }
        collection.add(dataSupport);
    }

    private void dealAssociatedModel(DataSupport dataSupport, DataSupport dataSupport2) {
        if (dataSupport2.isSaved()) {
            dataSupport.addAssociatedModelForJoinTable(dataSupport2.getTableName(), dataSupport2.getBaseObjId());
        }
    }

    private String getAssociatedTableName(AssociationsInfo associationsInfo) {
        return BaseUtility.changeCase(DBUtility.getTableNameByClassName(associationsInfo.getAssociatedClassName()));
    }

    @Deprecated
    private boolean isDataExists(DataSupport dataSupport, DataSupport dataSupport2) {
        Cursor cursorQuery = null;
        try {
            cursorQuery = Connector.getDatabase().query(getJoinTableName(dataSupport, dataSupport2), null, getSelection(dataSupport, dataSupport2), getSelectionArgs(dataSupport, dataSupport2), null, null, null);
            return cursorQuery.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        } finally {
            cursorQuery.close();
        }
    }

    private String getSelection(DataSupport dataSupport, DataSupport dataSupport2) {
        return getForeignKeyColumnName(dataSupport.getTableName()) + " = ? and " + getForeignKeyColumnName(dataSupport2.getTableName()) + " = ?";
    }

    private String[] getSelectionArgs(DataSupport dataSupport, DataSupport dataSupport2) {
        return new String[]{String.valueOf(dataSupport.getBaseObjId()), String.valueOf(dataSupport2.getBaseObjId())};
    }

    private String getJoinTableName(DataSupport dataSupport, DataSupport dataSupport2) {
        return getIntermediateTableName(dataSupport, dataSupport2.getTableName());
    }
}
