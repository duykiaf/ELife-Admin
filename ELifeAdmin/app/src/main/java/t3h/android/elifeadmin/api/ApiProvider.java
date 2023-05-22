package t3h.android.elifeadmin.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiProvider {
    public static final String BASE_URL = "http://localhost:3000";
    private static TokenApi tokenApi;
    private static CategoryApi categoryApi;
    private static TopicApi topicApi;
    private static AudioApi audioApi;
    private static Retrofit retrofit;

    public static TokenApi getTokenApi() {
        if (tokenApi == null) {
            tokenApi = getRetrofit().create(TokenApi.class);
        }
        return tokenApi;
    }

    public static CategoryApi getCategoryApi() {
        if (categoryApi == null) {
            categoryApi = getRetrofit().create(CategoryApi.class);
        }
        return categoryApi;
    }

    public static TopicApi getTopicApi() {
        if (topicApi == null) {
            topicApi = getRetrofit().create(TopicApi.class);
        }
        return topicApi;
    }

    public static AudioApi getAudioApi() {
        if (audioApi == null) {
            audioApi = getRetrofit().create(AudioApi.class);
        }
        return audioApi;
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
