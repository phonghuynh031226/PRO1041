/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package App.Main.NguoiDung;

/**
 *
 * @author PHONG
 */
public interface MainNguoiDungController {
    /** Nạp danh sách phòng trống vào bảng */
    void loadPhongTrong();

    /** Hiển thị thông tin đầu trang: phòng đang thuê, tên người thuê, hóa đơn mới nhất */
    void showHeaderInfo();

    /** Khi chọn 1 dòng trong bảng */
    void tableRowClick(int row);

    /** Đăng ký phòng theo ID đang nhập/chọn */
    void dangKyPhong();

    /** Xóa form nhỏ (id phòng nhập tay) + bỏ chọn bảng */
    void resetMiniForm();
}
