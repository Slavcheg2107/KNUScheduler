package geek.owl.com.ua.KNUSchedule.Util.Network

import geek.owl.com.ua.KNUSchedule.Repository.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface WebApi {
  @GET("get_faculties_list")
  fun getFaculties(): Deferred<Response<FacultyResponse>>

  @GET("get_groups_list")
  fun getGroups(@Query("faculty_id") facultyId: String): Deferred<Response<GroupsResponse>>

  @POST("get_schedule")
  @FormUrlEncoded
  fun getSchedule(@Field("group") vararg : String): Deferred<Response<ScheduleResponse>>


  @GET("get_advanced_schedule")
  fun getAdvanceSchedule(@Query("query") query: String, @Query("limit") take: Int, @Query("offset") offset: Int): Deferred<Response<ScheduleResponse>>

  @GET("")
  fun findGroup(p0: String?): Deferred<GroupsResponse>

  @POST("get_schedule")
  fun getSchedule(@Body dayRequestBody: DayRequestBody) : Deferred<Response<ScheduleResponse>>

  @GET("get_classes_time")
  fun getClassesTime(): Deferred<Response<ClassTimeResponse>>

  @GET("get_advanced_schedule")
  fun searchSchedule(@Query("query") query: String) : Deferred<Response<List<SchedulePojo>>>

}

