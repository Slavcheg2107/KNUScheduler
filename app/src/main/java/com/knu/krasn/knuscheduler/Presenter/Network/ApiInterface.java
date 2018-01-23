package com.knu.krasn.knuscheduler.Presenter.Network;


import com.knu.krasn.knuscheduler.Model.Models.Pojos.Faculties.Faculties;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.GroupModel.Groups;
import com.knu.krasn.knuscheduler.Model.Models.Pojos.Schedule.Schedules;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by krasn on 10/29/2017.
 */

public interface ApiInterface {

    @GET("getFacultiesList")
    Single<Faculties> getFaculties(@HeaderMap Map<String, String> headerMap);

    @GET("getGroupsList")
    Single<Groups> getGroups(@Query("faculty_id") String facultyID, @HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST("getSchedule")
    Single<Schedules> getSchedule(@Field("group") String group, @HeaderMap Map<String, String> headerMap);

    @GET("getAdvanceSchedule")
    Single<Schedules> getSearchingSchedule(@Query("query") String searchQuery, @HeaderMap Map<String, String> headerMap);
}
