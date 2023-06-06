package t3h.android.elifeadmin.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import t3h.android.elifeadmin.api.ApiProvider;
import t3h.android.elifeadmin.api.CategoryApi;
import t3h.android.elifeadmin.callback.ResultListCallback;
import t3h.android.elifeadmin.models.Category;

public class CategoryRepository {
    private final CategoryApi categoryApi;

    public CategoryRepository(Application application) {
        ApiProvider apiProvider = new ApiProvider(application);
        categoryApi = ApiProvider.getCategoryApi();
    }

    public LiveData<List<Category>> getAllList() {
        MutableLiveData<List<Category>> result = new MutableLiveData<>(new ArrayList<>());
        categoryApi.getAllList().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                result.setValue(response.body()); // co the null
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("GET CATEGORIES LIST FAILED", t.getMessage());
            }
        });
        return result;
    }

    public LiveData<List<Category>> getActiveCategories() {
        MutableLiveData<List<Category>> result = new MutableLiveData<>(new ArrayList<>());
        categoryApi.getActiveCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                result.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("GET ACTIVE CATEGORIES LIST FAILED", t.getMessage());
            }
        });
        return result;
    }

    public void getCategoryByName(String categoryName, ResultListCallback<Category> callback) {
        categoryApi.getCategoryByName(categoryName).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categoryList = response.body();
                if (categoryList != null) {
                    callback.onResultList(categoryList);
                } else {
                    callback.onResultList(null);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("GET CATEGORY BY NAME FAILED", t.getMessage());
                callback.onResultList(null);
            }
        });
    }

    public LiveData<Category> createCategory(Category category) {
        MutableLiveData<Category> liveData = new MutableLiveData<>();

        categoryApi.createCategory(category).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                liveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Log.e("CREATE CATEGORY FAILED", t.getMessage());
            }
        });

        return liveData;
    }

    public LiveData<Category> updateCategory(Category category) {
        MutableLiveData<Category> liveData = new MutableLiveData<>();
        categoryApi.updateCategory(category).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                liveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Log.e("UPDATE CATEGORY FAILED", t.getMessage());
            }
        });
        return liveData;
    }
}
