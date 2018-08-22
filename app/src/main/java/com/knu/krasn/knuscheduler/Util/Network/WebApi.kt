package com.knu.krasn.knuscheduler.Util.Network

import kotlinx.coroutines.experimental.Deferred
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

interface WebApi {
    @GET("getFacultiesList")
    fun getFaculties(): Deferred<Response<FacultyResponse>>

    @GET("getGroupsList")
    fun getGroups(@Query("faculty_id") facultyId: String): Deferred<Response<GroupsResponse>>

    @GET("getSchedule")
    @FormUrlEncoded
    fun getSchedule(@Field("group") group: String): Deferred<Response<ScheduleResponse>>


    @GET("getAdvanceSchedule")
    fun getAdvanceSchedule(@Query("query") query: String, @Query("limit") take: Int, @Query("offset") offset: Int): Deferred<Response<ScheduleResponse>>

    @GET("")
    fun findGroup(p0: String?): Deferred<GroupsResponse>

}

