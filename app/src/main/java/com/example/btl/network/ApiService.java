package com.example.btl.network;

//import com.example.btl.network.ApiResponse;
import com.example.btl.models.Student;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiService {

    // API lấy danh sách sinh viên
    @GET("api/students")
    Call<ApiResponse<List<Student>>> getAllStudents();

    // API thêm sinh viên mới
    @POST("api/students")
    Call<ApiResponse<Student>> addStudent(@Body Student student);
    // API kiểm tra đăng nhập (Trả về ApiResponse chứa thông tin Student nếu là sinh viên)
    @POST("api/auth/login")
    Call<ApiResponse<Student>> login(@Body java.util.Map<String, String> credentials);

    // API Lấy chi tiết 1 sinh viên
    @GET("api/students/{id}")
    Call<ApiResponse<Student>> getStudentById(@retrofit2.http.Path("id") String id);

    // API Cập nhật sinh viên
    @PUT("api/students/{id}")
    Call<ApiResponse<Student>> updateStudent(@retrofit2.http.Path("id") String id, @Body Student student);

    // API Xóa sinh viên
    @DELETE("api/students/{id}")
    Call<ApiResponse<Student>> deleteStudent(@retrofit2.http.Path("id") String id);

    // --- API QUẢN LÝ ĐIỂM THI ---

    // 1. API Lấy toàn bộ điểm
    @GET("api/scores")
    Call<ApiResponse<java.util.List<com.example.btl.models.Score>>> getAllScores();

    // 2. API Lấy điểm theo mã sinh viên
    @GET("api/scores/student/{studentId}")
    Call<ApiResponse<java.util.List<com.example.btl.models.Score>>> getScoresByStudent(@retrofit2.http.Path("studentId") String studentId);

    // 3. API Thêm điểm mới
    @POST("api/scores")
    Call<ApiResponse<com.example.btl.models.Score>> createScore(@Body com.example.btl.models.Score score);

    // 4. API Cập nhật điểm
    @PUT("api/scores/{id}")
    Call<ApiResponse<com.example.btl.models.Score>> updateScore(@retrofit2.http.Path("id") String id, @Body com.example.btl.models.Score score);
    // API Tìm kiếm sinh viên
    @retrofit2.http.GET("api/students/search")
    retrofit2.Call<com.example.btl.network.ApiResponse<java.util.List<com.example.btl.models.Student>>> searchStudents(@retrofit2.http.Query("q") String keyword);
}
