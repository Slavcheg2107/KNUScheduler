package geek.owl.com.ua.KNUSchedule.Repository.Network



import geek.owl.com.ua.KNUSchedule.Repository.FacultyResponse
import geek.owl.com.ua.KNUSchedule.Repository.GroupResponse
import geek.owl.com.ua.KNUSchedule.Repository.Schedule
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

    @GET("getFacultiesList")
    fun getFaculties():Deferred<FacultyResponse>
    @GET("getGroupsList")
fun getGroups():Deferred<GroupResponse>
    @FormUrlEncoded
    @POST("getSchedule")
fun getSchedule():Deferred<Schedule>

    @GET("getAdvanceSchedule")
fun getAdvanceSchedule():Deferred<Schedule>
}