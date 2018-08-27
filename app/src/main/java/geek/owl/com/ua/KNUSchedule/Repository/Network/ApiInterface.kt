package geek.owl.com.ua.KNUSchedule.Repository.Network


import com.owl.krasn.KNUSchedule.Repository.FacultyResponse
import com.owl.krasn.KNUSchedule.Repository.GroupResponse
import com.owl.krasn.KNUSchedule.Repository.Schedule
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