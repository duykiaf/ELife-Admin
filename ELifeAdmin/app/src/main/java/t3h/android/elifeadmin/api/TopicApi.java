package t3h.android.elifeadmin.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import t3h.android.elifeadmin.models.Topic;

public interface TopicApi {
    @GET("/topic/list")
    Call<List<Topic>> getAllList();

    @GET("/topic/get-by-name/{name}")
    Call<List<Topic>> getTopicByName(@Path("name") String topicName);

    @POST("/topic/create")
    Call<Topic> createTopic(@Body Topic topic);

    @PUT("/topic/update")
    Call<Topic> updateTopic(@Body Topic topic);
}
