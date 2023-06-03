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
import t3h.android.elifeadmin.callback.CategoryResultCallback;
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
                if (response.isSuccessful()) {
                    result.setValue(response.body()); // co the null
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("GET CATEGORIES LIST FAILED", t.getMessage());
            }
        });
        return result;
    }

    public void getCategoryByName(String categoryName, CategoryResultCallback callback) {
        categoryApi.getCategoryByName(categoryName).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> categoryList = response.body();
                    if (categoryList.size() != 0) {
                        callback.onCategoryResult(categoryList);
                    } else {
                        callback.onCategoryResult(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("GET CATEGORY BY NAME FAILED", t.getMessage());
                callback.onCategoryResult(null);
            }
        });
    }

    public LiveData<Category> createCategory(Category category) {
        MutableLiveData<Category> liveData = new MutableLiveData<>();

        categoryApi.createCategory(category).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                }
                Log.d("RESPONSE_CODE", String.valueOf(response.code()));
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
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Log.e("UPDATE CATEGORY FAILED", t.getMessage());
            }
        });
        return liveData;
    }
}
