package t3h.android.elifeadmin.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import t3h.android.elifeadmin.callback.ResultListCallback;
import t3h.android.elifeadmin.models.Audio;
import t3h.android.elifeadmin.repositories.AudioRepository;

public class AudioViewModel extends AndroidViewModel {
    private AudioRepository audioRepo;
    private MutableLiveData<String> audioTitle = new MutableLiveData<>();
    private MutableLiveData<String> audioLyrics = new MutableLiveData<>();

    public AudioViewModel(@NonNull Application application) {
        super(application);
        audioRepo = new AudioRepository(application);
    }

    public void setAudioTitle(String audioTitle) {
        this.audioTitle.setValue(audioTitle);
    }

    public LiveData<String> getAudioTitle() {
        return audioTitle;
    }

    public void setAudioLyrics(String audioLyrics) {
        this.audioLyrics.setValue(audioLyrics);
    }

    public LiveData<String> getAudioLyrics() {
        return audioLyrics;
    }

    public LiveData<List<Audio>> getAllList() {
        return audioRepo.getAllList();
    }

    public void getAudioByTitle(String title, ResultListCallback<Audio> callback) {
        audioRepo.getAudioByTitle(title, callback);
    }

    public LiveData<Audio> createAudio(Audio audio) {
        return audioRepo.createAudio(audio);
    }

    public LiveData<Audio> updateAudio(Audio audio) {
        return audioRepo.updateAudio(audio);
    }
}
