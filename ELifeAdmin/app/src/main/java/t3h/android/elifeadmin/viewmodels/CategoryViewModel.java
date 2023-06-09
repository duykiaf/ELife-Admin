package t3h.android.elifeadmin.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import t3h.android.elifeadmin.models.Category;
import t3h.android.elifeadmin.repositories.CategoryRepository;

public class CategoryViewModel extends AndroidViewModel {
    private MutableLiveData<String> categoryName = new MutableLiveData<>();

    private CategoryRepository categoryRepo;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryRepo = new CategoryRepository(application);
    }

    public LiveData<String> getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName.setValue(categoryName);
    }

    public LiveData<List<Category>> getAllList() {
        return categoryRepo.getAllList();
    }

    public LiveData<List<Category>> getActiveCategories() {
        return categoryRepo.getActiveCategories();
    }

    public LiveData<Category> createCategory(Category category) {
        return categoryRepo.createCategory(category);
    }

    public LiveData<Category> updateCategory(Category category) {
        return categoryRepo.updateCategory(category);
    }
}
