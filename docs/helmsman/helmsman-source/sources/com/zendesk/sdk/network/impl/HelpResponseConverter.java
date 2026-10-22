package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.model.helpcenter.help.ArticleItem;
import com.zendesk.sdk.model.helpcenter.help.CategoryItem;
import com.zendesk.sdk.model.helpcenter.help.HelpItem;
import com.zendesk.sdk.model.helpcenter.help.HelpResponse;
import com.zendesk.sdk.model.helpcenter.help.SectionItem;
import com.zendesk.sdk.model.helpcenter.help.SeeAllArticlesItem;
import com.zendesk.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
class HelpResponseConverter {
    private HelpResponse helpResponse;

    public HelpResponseConverter(HelpResponse helpResponse) {
        this.helpResponse = helpResponse;
    }

    List<HelpItem> convert() {
        HelpResponse helpResponse = this.helpResponse;
        if (helpResponse == null) {
            return Collections.emptyList();
        }
        Map<Long, List<HelpItem>> sectionArticlesMap = getSectionArticlesMap(helpResponse);
        Map<Long, List<SectionItem>> categorySectionsMap = getCategorySectionsMap(this.helpResponse);
        if (CollectionUtils.isNotEmpty(this.helpResponse.getCategories()) && CollectionUtils.isNotEmpty(this.helpResponse.getSections())) {
            return convertByCategories(categorySectionsMap, sectionArticlesMap);
        }
        if (CollectionUtils.isNotEmpty(this.helpResponse.getSections())) {
            return convertBySections(sectionArticlesMap);
        }
        return convertByArticles(this.helpResponse.getArticles());
    }

    private List<HelpItem> convertByCategories(Map<Long, List<SectionItem>> map, Map<Long, List<HelpItem>> map2) {
        List<CategoryItem> listEnsureEmpty = CollectionUtils.ensureEmpty(this.helpResponse.getCategories());
        ArrayList arrayList = new ArrayList(map2.size() + map.size() + listEnsureEmpty.size());
        for (CategoryItem categoryItem : listEnsureEmpty) {
            List<SectionItem> listEnsureEmpty2 = CollectionUtils.ensureEmpty(map.get(categoryItem.getId()));
            categoryItem.setSections(listEnsureEmpty2);
            arrayList.add(categoryItem);
            for (SectionItem sectionItem : listEnsureEmpty2) {
                arrayList.addAll(convertSection(sectionItem, map2.get(sectionItem.getId())));
            }
        }
        return arrayList;
    }

    private List<HelpItem> convertBySections(Map<Long, List<HelpItem>> map) {
        List<SectionItem> listEnsureEmpty = CollectionUtils.ensureEmpty(this.helpResponse.getSections());
        ArrayList arrayList = new ArrayList(map.size() + listEnsureEmpty.size());
        for (SectionItem sectionItem : listEnsureEmpty) {
            arrayList.addAll(convertSection(sectionItem, map.get(sectionItem.getId())));
        }
        return arrayList;
    }

    private List<HelpItem> convertSection(SectionItem sectionItem, List<HelpItem> list) {
        List<HelpItem> listCopyOf = CollectionUtils.copyOf(list);
        if (sectionItem.getTotalArticlesCount() > listCopyOf.size() && listCopyOf.size() > 0) {
            listCopyOf.add(new SeeAllArticlesItem(sectionItem));
        }
        sectionItem.addChildren(listCopyOf);
        List<HelpItem> listCopyOf2 = CollectionUtils.copyOf(listCopyOf);
        listCopyOf2.add(0, sectionItem);
        return listCopyOf2;
    }

    private List<HelpItem> convertByArticles(List<ArticleItem> list) {
        if (list != null) {
            return new ArrayList(list);
        }
        return Collections.emptyList();
    }

    Map<Long, List<HelpItem>> getSectionArticlesMap(HelpResponse helpResponse) {
        if (CollectionUtils.isEmpty(helpResponse.getSections()) || CollectionUtils.isEmpty(helpResponse.getArticles())) {
            return Collections.emptyMap();
        }
        HashMap map = new HashMap(helpResponse.getSections().size());
        for (ArticleItem articleItem : helpResponse.getArticles()) {
            Long parentId = articleItem.getParentId();
            List arrayList = map.containsKey(parentId) ? (List) map.get(parentId) : new ArrayList();
            if (!map.containsKey(parentId)) {
                map.put(parentId, arrayList);
            }
            arrayList.add(articleItem);
        }
        return map;
    }

    Map<Long, List<SectionItem>> getCategorySectionsMap(HelpResponse helpResponse) {
        if (CollectionUtils.isEmpty(helpResponse.getCategories()) || CollectionUtils.isEmpty(helpResponse.getSections())) {
            return Collections.emptyMap();
        }
        HashMap map = new HashMap(helpResponse.getCategories().size());
        for (SectionItem sectionItem : helpResponse.getSections()) {
            Long parentId = sectionItem.getParentId();
            List arrayList = map.containsKey(parentId) ? (List) map.get(parentId) : new ArrayList();
            if (!map.containsKey(parentId)) {
                map.put(parentId, arrayList);
            }
            arrayList.add(sectionItem);
        }
        return map;
    }
}
