package t3h.android.elifeadmin.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import t3h.android.elifeadmin.models.Topic;
import t3h.android.elifeadmin.repositories.TopicRepository;

public class TopicViewModel extends AndroidViewModel {
    private TopicRepository topicRepo;
    private MutableLiveData<String> topicName = new MutableLiveData<>();

    public TopicViewModel(@NonNull Application application) {
        super(application);
        topicRepo = new TopicRepository(application);
    }

    public void setTopicName(String topicName) {
        this.topicName.setValue(topicName);
    }

    public LiveData<String> getTopicName() {
        return topicName;
    }

    public LiveData<List<Topic>> getAllList() {
        return topicRepo.getAllList();
    }

    public LiveData<List<Topic>> getActiveTopicsList() {
        return topicRepo.getActiveTopicsList();
    }

    public LiveData<Topic> createTopic(Topic topic) {
        return topicRepo.createTopic(topic);
    }

    public LiveData<Topic> updateTopic(Topic topic) {
        return topicRepo.updateTopic(topic);
    }
}
