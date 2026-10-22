package org.litepal.crud;

import android.database.sqlite.SQLiteDatabase;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.litepal.crud.model.AssociationsInfo;
import org.litepal.exceptions.DataSupportException;
import org.litepal.util.BaseUtility;
import org.litepal.util.DBUtility;

/* JADX INFO: loaded from: classes2.dex */
class DeleteHandler extends DataHandler {
    private List<String> foreignKeyTableToDelete;

    DeleteHandler(SQLiteDatabase sQLiteDatabase) {
        this.mDatabase = sQLiteDatabase;
    }

    int onDelete(DataSupport dataSupport) {
        if (!dataSupport.isSaved()) {
            return 0;
        }
        deleteGenericData(dataSupport.getClass(), getSupportedGenericFields(dataSupport.getClassName()), dataSupport.getBaseObjId());
        Collection<AssociationsInfo> collectionAnalyzeAssociations = analyzeAssociations(dataSupport);
        int iDeleteCascade = deleteCascade(dataSupport) + this.mDatabase.delete(dataSupport.getTableName(), "id = " + dataSupport.getBaseObjId(), null);
        clearAssociatedModelSaveState(dataSupport, collectionAnalyzeAssociations);
        return iDeleteCascade;
    }

    int onDelete(Class<?> cls, long j) {
        deleteGenericData(cls, getSupportedGenericFields(cls.getName()), j);
        analyzeAssociations(cls);
        int iDeleteCascade = deleteCascade(cls, j) + this.mDatabase.delete(getTableName(cls), "id = " + j, null);
        getForeignKeyTableToDelete().clear();
        return iDeleteCascade;
    }

    int onDeleteAll(String str, String... strArr) {
        BaseUtility.checkConditionsCorrect(strArr);
        if (strArr != null && strArr.length > 0) {
            strArr[0] = DBUtility.convertWhereClauseToColumnName(strArr[0]);
        }
        return this.mDatabase.delete(str, getWhereClause(strArr), getWhereArgs(strArr));
    }

    int onDeleteAll(Class<?> cls, String... strArr) {
        BaseUtility.checkConditionsCorrect(strArr);
        if (strArr != null && strArr.length > 0) {
            strArr[0] = DBUtility.convertWhereClauseToColumnName(strArr[0]);
        }
        List<Field> supportedGenericFields = getSupportedGenericFields(cls.getName());
        if (!supportedGenericFields.isEmpty()) {
            List listFind = DataSupport.select("id").where(strArr).find(cls);
            if (listFind.size() > 0) {
                int size = listFind.size();
                long[] jArr = new long[size];
                for (int i = 0; i < size; i++) {
                    jArr[i] = ((DataSupport) listFind.get(i)).getBaseObjId();
                }
                deleteGenericData(cls, supportedGenericFields, jArr);
            }
        }
        analyzeAssociations(cls);
        int iDeleteAllCascade = deleteAllCascade(cls, strArr) + this.mDatabase.delete(getTableName(cls), getWhereClause(strArr), getWhereArgs(strArr));
        getForeignKeyTableToDelete().clear();
        return iDeleteAllCascade;
    }

    private void analyzeAssociations(Class<?> cls) {
        for (AssociationsInfo associationsInfo : getAssociationInfo(cls.getName())) {
            String tableNameByClassName = DBUtility.getTableNameByClassName(associationsInfo.getAssociatedClassName());
            if (associationsInfo.getAssociationType() == 2 || associationsInfo.getAssociationType() == 1) {
                if (!cls.getName().equals(associationsInfo.getClassHoldsForeignKey())) {
                    getForeignKeyTableToDelete().add(tableNameByClassName);
                }
            } else if (associationsInfo.getAssociationType() == 3) {
                getForeignKeyTableToDelete().add(BaseUtility.changeCase(DBUtility.getIntermediateTableName(getTableName(cls), tableNameByClassName)));
            }
        }
    }

    private int deleteCascade(Class<?> cls, long j) {
        Iterator<String> it = getForeignKeyTableToDelete().iterator();
        int iDelete = 0;
        while (it.hasNext()) {
            iDelete += this.mDatabase.delete(it.next(), getForeignKeyColumnName(getTableName(cls)) + " = " + j, null);
        }
        return iDelete;
    }

    private int deleteAllCascade(Class<?> cls, String... strArr) {
        int iDelete = 0;
        for (String str : getForeignKeyTableToDelete()) {
            String tableName = getTableName(cls);
            String foreignKeyColumnName = getForeignKeyColumnName(tableName);
            StringBuilder sb = new StringBuilder();
            sb.append(foreignKeyColumnName).append(" in (select id from ");
            sb.append(tableName);
            if (strArr != null && strArr.length > 0) {
                sb.append(" where ").append(buildConditionString(strArr));
            }
            sb.append(")");
            iDelete += this.mDatabase.delete(str, BaseUtility.changeCase(sb.toString()), null);
        }
        return iDelete;
    }

    private String buildConditionString(String... strArr) {
        int length = strArr.length - 1;
        int i = 0;
        String strReplaceFirst = strArr[0];
        while (i < length) {
            i++;
            strReplaceFirst = strReplaceFirst.replaceFirst("\\?", "'" + strArr[i] + "'");
        }
        return strReplaceFirst;
    }

    private Collection<AssociationsInfo> analyzeAssociations(DataSupport dataSupport) {
        try {
            Collection<AssociationsInfo> associationInfo = getAssociationInfo(dataSupport.getClassName());
            analyzeAssociatedModels(dataSupport, associationInfo);
            return associationInfo;
        } catch (Exception e) {
            throw new DataSupportException(e.getMessage(), e);
        }
    }

    private void clearAssociatedModelSaveState(DataSupport dataSupport, Collection<AssociationsInfo> collection) {
        DataSupport associatedModel;
        try {
            for (AssociationsInfo associationsInfo : collection) {
                if (associationsInfo.getAssociationType() == 2 && !dataSupport.getClassName().equals(associationsInfo.getClassHoldsForeignKey())) {
                    Collection<DataSupport> associatedModels = getAssociatedModels(dataSupport, associationsInfo);
                    if (associatedModels != null && !associatedModels.isEmpty()) {
                        for (DataSupport dataSupport2 : associatedModels) {
                            if (dataSupport2 != null) {
                                dataSupport2.clearSavedState();
                            }
                        }
                    }
                } else if (associationsInfo.getAssociationType() == 1 && (associatedModel = getAssociatedModel(dataSupport, associationsInfo)) != null) {
                    associatedModel.clearSavedState();
                }
            }
        } catch (Exception e) {
            throw new DataSupportException(e.getMessage(), e);
        }
    }

    private int deleteCascade(DataSupport dataSupport) {
        return deleteAssociatedForeignKeyRows(dataSupport) + deleteAssociatedJoinTableRows(dataSupport);
    }

    private int deleteAssociatedForeignKeyRows(DataSupport dataSupport) {
        Iterator<String> it = dataSupport.getAssociatedModelsMapWithFK().keySet().iterator();
        int iDelete = 0;
        while (it.hasNext()) {
            iDelete += this.mDatabase.delete(it.next(), getForeignKeyColumnName(dataSupport.getTableName()) + " = " + dataSupport.getBaseObjId(), null);
        }
        return iDelete;
    }

    private int deleteAssociatedJoinTableRows(DataSupport dataSupport) {
        Iterator<String> it = dataSupport.getAssociatedModelsMapForJoinTable().keySet().iterator();
        int iDelete = 0;
        while (it.hasNext()) {
            iDelete += this.mDatabase.delete(DBUtility.getIntermediateTableName(dataSupport.getTableName(), it.next()), getForeignKeyColumnName(dataSupport.getTableName()) + " = " + dataSupport.getBaseObjId(), null);
        }
        return iDelete;
    }

    private List<String> getForeignKeyTableToDelete() {
        if (this.foreignKeyTableToDelete == null) {
            this.foreignKeyTableToDelete = new ArrayList();
        }
        return this.foreignKeyTableToDelete;
    }

    private void deleteGenericData(Class<?> cls, List<Field> list, long... jArr) {
        Iterator<Field> it = list.iterator();
        while (it.hasNext()) {
            String genericTableName = DBUtility.getGenericTableName(cls.getName(), it.next().getName());
            String genericValueIdColumnName = DBUtility.getGenericValueIdColumnName(cls.getName());
            StringBuilder sb = new StringBuilder();
            int length = jArr.length;
            int i = 0;
            boolean z = false;
            while (i < length) {
                long j = jArr[i];
                if (z) {
                    sb.append(" or ");
                }
                sb.append(genericValueIdColumnName).append(" = ").append(j);
                i++;
                z = true;
            }
            this.mDatabase.delete(genericTableName, sb.toString(), null);
        }
    }
}
