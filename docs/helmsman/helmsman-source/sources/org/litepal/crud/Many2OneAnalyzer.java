package org.litepal.crud;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import org.litepal.crud.model.AssociationsInfo;
import org.litepal.util.DBUtility;

/* JADX INFO: loaded from: classes2.dex */
class Many2OneAnalyzer extends AssociationsAnalyzer {
    Many2OneAnalyzer() {
    }

    void analyze(DataSupport dataSupport, AssociationsInfo associationsInfo) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (dataSupport.getClassName().equals(associationsInfo.getClassHoldsForeignKey())) {
            analyzeManySide(dataSupport, associationsInfo);
        } else {
            analyzeOneSide(dataSupport, associationsInfo);
        }
    }

    private void analyzeManySide(DataSupport dataSupport, AssociationsInfo associationsInfo) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        DataSupport associatedModel = getAssociatedModel(dataSupport, associationsInfo);
        if (associatedModel != null) {
            Collection<DataSupport> collectionCheckAssociatedModelCollection = checkAssociatedModelCollection(getReverseAssociatedModels(associatedModel, associationsInfo), associationsInfo.getAssociateSelfFromOtherModel());
            setReverseAssociatedModels(associatedModel, associationsInfo, collectionCheckAssociatedModelCollection);
            dealAssociatedModelOnManySide(collectionCheckAssociatedModelCollection, dataSupport, associatedModel);
            return;
        }
        mightClearFKValue(dataSupport, associationsInfo);
    }

    private void analyzeOneSide(DataSupport dataSupport, AssociationsInfo associationsInfo) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Collection<DataSupport> associatedModels = getAssociatedModels(dataSupport, associationsInfo);
        if (associatedModels == null || associatedModels.isEmpty()) {
            dataSupport.addAssociatedTableNameToClearFK(DBUtility.getTableNameByClassName(associationsInfo.getAssociatedClassName()));
            return;
        }
        for (DataSupport dataSupport2 : associatedModels) {
            buildBidirectionalAssociations(dataSupport, dataSupport2, associationsInfo);
            dealAssociatedModelOnOneSide(dataSupport, dataSupport2);
        }
    }

    private void dealAssociatedModelOnManySide(Collection<DataSupport> collection, DataSupport dataSupport, DataSupport dataSupport2) {
        if (!collection.contains(dataSupport)) {
            collection.add(dataSupport);
        }
        if (dataSupport2.isSaved()) {
            dataSupport.addAssociatedModelWithoutFK(dataSupport2.getTableName(), dataSupport2.getBaseObjId());
        }
    }

    private void dealAssociatedModelOnOneSide(DataSupport dataSupport, DataSupport dataSupport2) {
        dealsAssociationsOnTheSideWithoutFK(dataSupport, dataSupport2);
    }
}
