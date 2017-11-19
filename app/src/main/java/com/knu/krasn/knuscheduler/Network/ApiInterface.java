package com.knu.krasn.knuscheduler.Network;


import com.knu.krasn.knuscheduler.Models.GroupModel.Groups;
import com.knu.krasn.knuscheduler.Models.Schedule.Schedules;
import com.knu.krasn.knuscheduler.Models.ScheduleTime.Streams;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

/**
 * Created by krasn on 10/29/2017.
 */

public interface ApiInterface {

    @GET("getGroupsList")
    Single<Groups> getGroups(@HeaderMap Map<String, String> headerMap);

    @GET("getClassesTime")
    Single<Streams> getTime(@HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST("getSchedule")
    Single<Schedules> getSchedule(@Field("group") String group, @HeaderMap Map<String, String> headerMap);
}
