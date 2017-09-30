package com.example.krasn.knuscheduler.Network;


import com.example.krasn.knuscheduler.Models.GroupModel.Groups;
import com.example.krasn.knuscheduler.Models.Schedule.Schedules;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by krasn on 9/3/2017.
 */

public interface ApiNetwork {
    @GET("getGroupsList")
    Call<Groups> getGroups ();

    @FormUrlEncoded
    @POST("getSchedule")
    Call<Schedules> getSchedule (@Field("group") String group);
}
