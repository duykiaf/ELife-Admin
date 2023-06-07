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
import t3h.android.elifeadmin.api.AudioApi;
import t3h.android.elifeadmin.callback.ResultListCallback;
import t3h.android.elifeadmin.models.Audio;

public class AudioRepository {
    private final AudioApi audioApi;

    public AudioRepository(Application application) {
        ApiProvider apiProvider = new ApiProvider(application);
        audioApi = ApiProvider.getAudioApi();
    }

    public LiveData<List<Audio>> getAllList() {
        MutableLiveData<List<Audio>> liveData = new MutableLiveData<>();
        audioApi.getAllList().enqueue(new Callback<List<Audio>>() {
            @Override
            public void onResponse(Call<List<Audio>> call, Response<List<Audio>> response) {
                liveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Audio>> call, Throwable t) {
                Log.e("GET AUDIOS LIST FAILED", t.getMessage());
            }
        });
        return liveData;
    }

    public void getAudioByTitle(String title, ResultListCallback<Audio> callback) {
        audioApi.getAudioByTitle(title).enqueue(new Callback<List<Audio>>() {
            @Override
            public void onResponse(Call<List<Audio>> call, Response<List<Audio>> response) {
                if (response.body() != null) {
                    callback.onResultList(response.body());
                } else {
                    callback.onResultList(null);
                }
            }

            @Override
            public void onFailure(Call<List<Audio>> call, Throwable t) {
                Log.e("GET AUDIO BY TITLE FAILED", t.getMessage());
                callback.onResultList(null);
            }
        });
    }

    public LiveData<Audio> createAudio(Audio audio) {
        MutableLiveData<Audio> liveData = new MutableLiveData<>();
        audioApi.createAudio(audio).enqueue(new Callback<Audio>() {
            @Override
            public void onResponse(Call<Audio> call, Response<Audio> response) {
                liveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Audio> call, Throwable t) {
                Log.e("CREATE AUDIO FAILED", t.getMessage());
            }
        });
        return liveData;
    }

    public LiveData<Audio> updateAudio(Audio audio) {
        MutableLiveData<Audio> liveData = new MutableLiveData<>();
        audioApi.updateAudio(audio).enqueue(new Callback<Audio>() {
            @Override
            public void onResponse(Call<Audio> call, Response<Audio> response) {
                liveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Audio> call, Throwable t) {
                Log.e("UPDATE AUDIO FAILED", t.getMessage());
            }
        });
        return liveData;
    }
}
