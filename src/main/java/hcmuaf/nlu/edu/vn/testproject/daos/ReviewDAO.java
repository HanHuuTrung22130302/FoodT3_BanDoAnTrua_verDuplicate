package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.ReviewFood;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReviewDAO {

    private Map<Integer, ReviewFood> dataReviewFood = new HashMap<>();


    public ReviewDAO() {
        this.dataReviewFood = new HashMap<>();
        getAllReview();
    }

    public List<ReviewFood> getAll() {
        return new ArrayList<>(dataReviewFood.values());
    }

    // Hàm lấy tất cả các món ăn từ cơ sở dữ liệu
    public void getAllReview() {
        String query="SELECT  name,rv.idAcc,idReview, idFood,rating,createdAt from review rv join Account ac on rv.idAcc=ac.idAcc ";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Tạo kết nối cơ sở dữ liệu
            con = new DbContext().getConnection();
            // Kiểm tra kết nối
            if (con != null) {
                System.out.println("Kết nối cơ sở dữ liệu thành công!");
            } else {
                System.out.println("Kết nối cơ sở dữ liệu thất bại!");
                // Trả về danh sách rỗng nếu không kết nối được
            }

            // Chuẩn bị câu lệnh SQL
            ps = con.prepareStatement(query);
            // Thực thi câu lệnh
            rs = ps.executeQuery();

            // Duyệt qua kết quả trả về và tạo danh sách món ăn
            while (rs.next()) {
                dataReviewFood.put(rs.getInt("idReview"),new ReviewFood(
                        rs.getString("name"),
                        rs.getInt("idReview"),
                        rs.getInt("idFood"),
                        rs.getInt("rating"),
                        rs.getInt("idAcc"),
                        rs.getDate("createdAt")
                ) );
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // Đảm bảo rằng kết nối, câu lệnh và result set được đóng đúng cách
            closeResources(rs, ps, con);
        }
    }

    // Phương thức đóng các tài nguyên
    private void closeResources(ResultSet rs, PreparedStatement ps, Connection con) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
        }
    }
    public List<Integer> getTop4FoodRate(){
        Map<Integer, Long> countStarLS= dataReviewFood.values().stream().filter(rv -> rv.getRating()==5).collect(Collectors.groupingBy(ReviewFood::getIdFood,Collectors.counting()));
        List<Integer> lsIdFood= countStarLS.entrySet().stream().sorted((e1,e2)->Long.compare(e2.getValue(),e1.getValue())).limit(4).map(Map.Entry::getKey).collect(Collectors.toList());
        return lsIdFood;
    }
    public List<Integer> getTopFoodRate(){
        Map<Integer, Long> countStarLS= dataReviewFood.values().stream().filter(rv -> rv.getRating()==5).collect(Collectors.groupingBy(ReviewFood::getIdFood,Collectors.counting()));
        List<Integer> lsIdFood= countStarLS.entrySet().stream().sorted((e1,e2)->Long.compare(e2.getValue(),e1.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());
        return lsIdFood;
    }




}
