package t3h.android.elifeadmin.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import t3h.android.elifeadmin.models.Audio;

public interface AudioApi {
    @GET("/audio/list")
    Call<List<Audio>> getAllList();

    @GET("/audio/get-by-title/{title}")
    Call<List<Audio>> getAudioByTitle(@Path("title") String title);

    @POST("/audio/create")
    Call<Audio> createAudio(@Body Audio audio);

    @PUT("/audio/update")
    Call<Audio> updateAudio(@Body Audio audio);
}
