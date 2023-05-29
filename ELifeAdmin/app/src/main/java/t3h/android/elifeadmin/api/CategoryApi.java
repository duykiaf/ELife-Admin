package t3h.android.elifeadmin.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import t3h.android.elifeadmin.models.Category;

public interface CategoryApi {
    @GET("/category/get-by-name/{name}")
    Call<List<Category>> getCategoryByName(@Path("name") String categoryName);

    @POST("/category/create")
    Call<Category> createCategory(@Body Category category);
}
