package t3h.android.elifeadmin.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import t3h.android.elifeadmin.api.ApiProvider;
import t3h.android.elifeadmin.api.TopicApi;
import t3h.android.elifeadmin.callback.ResultListCallback;
import t3h.android.elifeadmin.models.Topic;

public class TopicRepository {
    private final TopicApi topicApi;

    public TopicRepository(Application application) {
        ApiProvider apiProvider = new ApiProvider(application);
        topicApi = ApiProvider.getTopicApi();
    }

    public LiveData<List<Topic>> getAllList() {
        MutableLiveData<List<Topic>> liveData = new MutableLiveData<>();
        topicApi.getAllList().enqueue(new Callback<List<Topic>>() {
            @Override
            public void onResponse(Call<List<Topic>> call, Response<List<Topic>> response) {
                liveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Topic>> call, Throwable t) {
                Log.e("GET TOPICS LIST FAILED", t.getMessage());
            }
        });
        return liveData;
    }

    public void getTopicByName(String topicName, ResultListCallback<Topic> callback) {
        topicApi.getTopicByName(topicName).enqueue(new Callback<List<Topic>>() {
            @Override
            public void onResponse(Call<List<Topic>> call, Response<List<Topic>> response) {
                if (response.body() != null) {
                    callback.onResultList(response.body());
                } else {
                    callback.onResultList(null);
                }
            }

            @Override
            public void onFailure(Call<List<Topic>> call, Throwable t) {
                Log.e("GET TOPIC BY NAME FAILED", t.getMessage());
                callback.onResultList(null);
            }
        });
    }

    public LiveData<Topic> createTopic(Topic topic) {
        MutableLiveData<Topic> liveData = new MutableLiveData<>();
        topicApi.createTopic(topic).enqueue(new Callback<Topic>() {
            @Override
            public void onResponse(Call<Topic> call, Response<Topic> response) {
                liveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Topic> call, Throwable t) {
                Log.e("CREATE TOPIC FAILED", t.getMessage());
            }
        });
        return liveData;
    }

    public LiveData<Topic> updateTopic(Topic topic) {
        MutableLiveData<Topic> liveData = new MutableLiveData<>();
        topicApi.updateTopic(topic).enqueue(new Callback<Topic>() {
            @Override
            public void onResponse(Call<Topic> call, Response<Topic> response) {
                liveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Topic> call, Throwable t) {
                Log.e("UPDATE TOPIC FAILED", t.getMessage());
            }
        });
        return liveData;
    }
}
