package geek.owl.com.ua.KNUSchedule.Repository.Network




import geek.owl.com.ua.KNUSchedule.Repository.FacultyResponse
import geek.owl.com.ua.KNUSchedule.Repository.GroupsResponse
import geek.owl.com.ua.KNUSchedule.Repository.ScheduleResponse

import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

    @GET("getFacultiesList")
    fun getFaculties():Deferred<FacultyResponse>
    @GET("getGroupsList")
fun getGroups():Deferred<GroupsResponse>
    @FormUrlEncoded
    @POST("getSchedule")
fun getSchedule():Deferred<ScheduleResponse>

    @GET("getAdvanceSchedule")
fun getAdvanceSchedule():Deferred<ScheduleResponse>
}