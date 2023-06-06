package t3h.android.elifeadmin.api;

import android.content.Context;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import t3h.android.elifeadmin.helper.SharedPreferencesHelper;

public class ApiProvider {
    public static final String BASE_URL = "http://192.168.1.9:3000";
    private static TokenApi tokenApi;
    private static CategoryApi categoryApi;
    private static TopicApi topicApi;
    private static AudioApi audioApi;
    private static Retrofit retrofit;
    private static Context context;
    private static OkHttpClient okHttpClient;

    public ApiProvider(Context context) {
        this.context = context;
    }

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
        if (context != null) {
            Interceptor interceptor = chain -> {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();
                builder.addHeader("x-access-token", SharedPreferencesHelper.getAccessToken(context));
                builder.addHeader("x-refresh-token", SharedPreferencesHelper.getRefreshToken(context));
                return chain.proceed(builder.build());
            };
            okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            retrofit = retrofitBuilder();
        } else {
            okHttpClient = new OkHttpClient.Builder().build();
            if (retrofit == null) {
                retrofit = retrofitBuilder();
            }
        }
        return retrofit;
    }

    private static Retrofit retrofitBuilder() {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
