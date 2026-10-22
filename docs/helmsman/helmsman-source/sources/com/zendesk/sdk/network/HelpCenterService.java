package com.zendesk.sdk.network;

import com.facebook.places.model.PlaceFields;
import com.google.android.gms.actions.SearchIntents;
import com.zendesk.sdk.model.helpcenter.ArticleResponse;
import com.zendesk.sdk.model.helpcenter.ArticleVoteResponse;
import com.zendesk.sdk.model.helpcenter.ArticlesListResponse;
import com.zendesk.sdk.model.helpcenter.ArticlesSearchResponse;
import com.zendesk.sdk.model.helpcenter.AttachmentResponse;
import com.zendesk.sdk.model.helpcenter.CategoriesResponse;
import com.zendesk.sdk.model.helpcenter.CategoryResponse;
import com.zendesk.sdk.model.helpcenter.RecordArticleViewRequest;
import com.zendesk.sdk.model.helpcenter.SectionResponse;
import com.zendesk.sdk.model.helpcenter.SectionsResponse;
import com.zendesk.sdk.model.helpcenter.SuggestedArticleResponse;
import com.zendesk.sdk.model.helpcenter.help.HelpResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/* JADX INFO: loaded from: classes2.dex */
public interface HelpCenterService {
    @DELETE("/api/v2/help_center/votes/{vote_id}.json")
    Call<Void> deleteVote(@Header("Authorization") String str, @Path("vote_id") Long l);

    @POST("/api/v2/help_center/articles/{article_id}/down.json")
    Call<ArticleVoteResponse> downvoteArticle(@Header("Authorization") String str, @Path("article_id") Long l, @Body String str2);

    @GET("/api/v2/help_center/{locale}/articles/{article_id}.json?respect_sanitization_settings=true")
    Call<ArticleResponse> getArticle(@Header("Authorization") String str, @Path("locale") String str2, @Path("article_id") Long l, @Query("include") String str3);

    @GET("/api/v2/help_center/{locale}/sections/{id}/articles.json?respect_sanitization_settings=true")
    Call<ArticlesListResponse> getArticles(@Header("Authorization") String str, @Path("locale") String str2, @Path("id") Long l, @Query("include") String str3, @Query("per_page") int i);

    @GET("/api/v2/help_center/{locale}/articles/{article_id}/attachments/{attachment_type}.json")
    Call<AttachmentResponse> getAttachments(@Header("Authorization") String str, @Path("locale") String str2, @Path("article_id") Long l, @Path("attachment_type") String str3);

    @GET("/api/v2/help_center/{locale}/categories.json?per_page=1000")
    Call<CategoriesResponse> getCategories(@Header("Authorization") String str, @Path("locale") String str2);

    @GET("/api/v2/help_center/{locale}/categories/{category_id}.json")
    Call<CategoryResponse> getCategoryById(@Header("Authorization") String str, @Path("locale") String str2, @Path("category_id") Long l);

    @GET("/hc/api/mobile/{locale}/articles.json")
    Call<HelpResponse> getHelp(@Header("Authorization") String str, @Path("locale") String str2, @Query("category_ids") String str3, @Query("section_ids") String str4, @Query("include") String str5, @Query("limit") int i, @Query("label_names") String str6, @Query("per_page") int i2, @Query("sort_by") String str7, @Query("sort_order") String str8);

    @GET("/api/v2/help_center/{locale}/sections/{section_id}.json")
    Call<SectionResponse> getSectionById(@Header("Authorization") String str, @Path("locale") String str2, @Path("section_id") Long l);

    @GET("/api/v2/help_center/{locale}/categories/{id}/sections.json")
    Call<SectionsResponse> getSections(@Header("Authorization") String str, @Path("locale") String str2, @Path("id") Long l, @Query("per_page") int i);

    @GET("/api/mobile/help_center/search/deflect.json?respect_sanitization_settings=true")
    Call<SuggestedArticleResponse> getSuggestedArticles(@Header("Authorization") String str, @Query(SearchIntents.EXTRA_QUERY) String str2, @Query("locale") String str3, @Query("label_names") String str4, @Query("category") Long l, @Query("section") Long l2);

    @GET("/api/v2/help_center/{locale}/articles.json?respect_sanitization_settings=true")
    Call<ArticlesListResponse> listArticles(@Header("Authorization") String str, @Path("locale") String str2, @Query("label_names") String str3, @Query("include") String str4, @Query("sort_by") String str5, @Query("sort_order") String str6, @Query(PlaceFields.PAGE) Integer num, @Query("per_page") Integer num2);

    @GET("/api/v2/help_center/articles/search.json?respect_sanitization_settings=true&origin=mobile_sdk")
    Call<ArticlesSearchResponse> searchArticles(@Header("Authorization") String str, @Query(SearchIntents.EXTRA_QUERY) String str2, @Query("locale") String str3, @Query("include") String str4, @Query("label_names") String str5, @Query("category") String str6, @Query("section") String str7, @Query(PlaceFields.PAGE) Integer num, @Query("per_page") Integer num2);

    @POST("/api/v2/help_center/{locale}/articles/{article_id}/stats/view.json")
    Call<Void> submitRecordArticleView(@Header("Authorization") String str, @Path("article_id") Long l, @Path("locale") String str2, @Body RecordArticleViewRequest recordArticleViewRequest);

    @POST("/api/v2/help_center/articles/{article_id}/up.json")
    Call<ArticleVoteResponse> upvoteArticle(@Header("Authorization") String str, @Path("article_id") Long l, @Body String str2);
}
