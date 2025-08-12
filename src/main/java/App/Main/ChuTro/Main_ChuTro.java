/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package App.Main.ChuTro;


// ở đầu file Main_ChuTro.java
import App.DAO.PhongDAO;
import App.Impl.PhongDAOImpl;
import App.Entity.Phong;
import App.Main.TrangChuChung;

import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;



//implements MainChuTroController
/**
 *
 * @author WINDOWS
 */
public class Main_ChuTro extends javax.swing.JFrame implements MainChuTroController{
// trong class Main_ChuTro
private final PhongDAO phongDAO = new PhongDAOImpl();
private DefaultTableModel phongModel;

    /**
     * Creates new form TrangChu
     */
    
    
    public Main_ChuTro() throws SQLException {
        initComponents();
        setLocationRelativeTo(null);
//        setupActions();
//        buildTable();
//        loadDashboard();
    buildPhongTable();
    loadAllPhong();   // <== nạp tất cả phòng, không lọc trạng thái

    }

private void buildPhongTable() {
    phongModel = (DefaultTableModel) tblphongtro.getModel();
    phongModel.setColumnIdentifiers(new Object[]{
        "Mã phòng", "Diện tích (m²)", "Giá phòng", "Trạng thái", "Địa chỉ", "Mô tả", "Liên hệ"
    });
    tblphongtro.setRowHeight(26);
}

private void loadAllPhong() {
    phongModel.setRowCount(0);
    try {
        List<Phong> list = phongDAO.findAll();    // KHÔNG lọc, lấy hết
        for (Phong p : list) {
            phongModel.addRow(new Object[]{
                nvl(p.getMaPhong()),
                fmtQty(p.getDienTich()),
                fmtMoney(p.getGiaTien()),
                nvl(p.getTrangThai()),            // ví dụ: "Trống" / "Đang thuê" / "0" / "1"
                nvl(p.getDiaChi()),
                nvl(p.getMoTa()),
                nvl(p.getLienHe())
            });
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Lỗi tải danh sách phòng: " + ex.getMessage());
    }
}
private static String nvl(Object o) { return o == null ? "" : String.valueOf(o); }

private static String fmtQty(BigDecimal v) {
    if (v == null) return "";
    NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi","VN"));
    nf.setMaximumFractionDigits(2);  // diện tích có thể lẻ .5
    nf.setMinimumFractionDigits(0);
    return nf.format(v);
}

private static String fmtMoney(BigDecimal v) {
    if (v == null) return "";
    NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi","VN"));
    nf.setMaximumFractionDigits(0);  // VND không lấy lẻ
    return nf.format(v);
}

    

//    // ===== DAOs =====
////    private final HoaDonDAO hoaDonDAO = new HoaDonDAOImpl();
////    private final HopDongDAO hopDongDAO = new HopDongDAOImpl();
//
//    // ===== Wiring menu & buttons =====
//    private void setupActions() {
//        // mở dialogs
//        jMenuPhongTro.addMenuListener(new javax.swing.event.MenuListener() {
//            @Override public void menuSelected(javax.swing.event.MenuEvent e) { openPhong(); }
//            @Override public void menuDeselected(javax.swing.event.MenuEvent e) {}
//            @Override public void menuCanceled(javax.swing.event.MenuEvent e) {}
//        });
//        jMenuHopDong.addMenuListener(new javax.swing.event.MenuListener() {
//            @Override public void menuSelected(javax.swing.event.MenuEvent e) { openHopDong(); }
//            @Override public void menuDeselected(javax.swing.event.MenuEvent e) {}
//            @Override public void menuCanceled(javax.swing.event.MenuEvent e) {}
//        });
//        jMenuHoaDon.addMenuListener(new javax.swing.event.MenuListener() {
//            @Override public void menuSelected(javax.swing.event.MenuEvent e) { openHoaDon(); }
//            @Override public void menuDeselected(javax.swing.event.MenuEvent e) {}
//            @Override public void menuCanceled(javax.swing.event.MenuEvent e) {}
//        });
//        jMtaikhoan.addMenuListener(new javax.swing.event.MenuListener() {
//            @Override public void menuSelected(javax.swing.event.MenuEvent e) { openTaiKhoan(); }
//            @Override public void menuDeselected(javax.swing.event.MenuEvent e) {}
//            @Override public void menuCanceled(javax.swing.event.MenuEvent e) {}
//        });
//
//        // nút hệ thống
//        btnThoat.addActionListener(evt -> {
//            int c = JOptionPane.showConfirmDialog(this, "Bạn muốn đóng ứng dụng?", "Thoát?", JOptionPane.YES_NO_OPTION);
//            if (c == JOptionPane.YES_OPTION) System.exit(0);
//        });
//        btnDangXuat.addActionListener(evt -> {
//            // nếu có màn đăng nhập, bạn mở lại tại đây
//            JOptionPane.showMessageDialog(this, "Đăng xuất (demo).");
//        });
//    }
//
//    // ===== Dashboard =====
//    @Override
//    public void loadDashboard() {
////        try {
////            // Doanh thu tháng hiện tại
////            LocalDate now = LocalDate.now();
////            LocalDate start = now.withDayOfMonth(1);
////            LocalDate end   = now.withDayOfMonth(now.lengthOfMonth());
////            Date from = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
////            Date to   = Date.from(end.atTime(23,59,59).atZone(ZoneId.systemDefault()).toInstant());
////
////            long doanhThuThang = hoaDonDAO.sumDoanhThuDaThanhToan(from, to);
////            lblDoanhSo.setText(formatMoney(doanhThuThang) + " VND");
////            lblThangDoanhSo.setText(String.format("%02d", now.getMonthValue()));
////
////            // Số khách trọ đang thuê = số hợp đồng trạng thái "Đang thuê"
////            int soDangThue = countDangThue(hopDongDAO.findAll());
////            lblSoKhach.setText(pad2(soDangThue));
////
////            // Hợp đồng sắp hết hạn (7 ngày tới)
////            int sapHetHan = countSapHetHan(hopDongDAO.findAll(), 7);
////            lblHopDong.setText(pad2(sapHetHan));
////
////            // Hóa đơn chưa thanh toán
////            int chuaTT = hoaDonDAO.findByTrangThai("Chưa thanh toán").size();
////            lblHoaDon.setText(pad2(chuaTT));
////
////            // Tải bảng: 10 hóa đơn gần nhất
////            fillRecentInvoices();
////
////        } catch (Exception ex) {
////            Logger.getLogger(Main_ChuTro.class.getName()).log(Level.SEVERE, null, ex);
////            JOptionPane.showMessageDialog(this, "Lỗi tải dashboard: " + ex.getMessage());
////        }
//    }
//
//    private void fillRecentInvoices() {
//        DefaultTableModel m = (DefaultTableModel) tblTrangChu.getModel();
//        m.setRowCount(0);
//        List<HoaDon> all = hoaDonDAO.findAll();
//        // sắp xếp theo ngày tạo mới nhất (nếu null thì về cuối)
//        all.sort((a,b) -> {
//            Date da = a.getNgayTaoHoaDon();
//            Date db = b.getNgayTaoHoaDon();
//            if (da == null && db == null) return 0;
//            if (da == null) return 1;
//            if (db == null) return -1;
//            return db.compareTo(da);
//        });
//        int limit = Math.min(10, all.size());
//        for (int i = 0; i < limit; i++) {
//            HoaDon h = all.get(i);
//            long tong = Math.round(nz(h.getTienPhong()) + nz(h.getTienDien()) + nz(h.getTienNuoc()));
//            m.addRow(new Object[]{
//                h.getMaHoaDon(),
//                h.getMaHopDong(),
//                h.getNgayTaoHoaDon(),
//                h.getTrangThai(),
//                formatMoney(tong)
//            });
//        }
//    }
//
//// ===== Helpers ngày =====
//private static java.time.LocalDate toLocal(java.util.Date d) {
//    return d.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
//}
//
//// ===== Đếm hợp đồng ĐANG THUÊ (không cần cột trangThai) =====
//private int countDangThue(java.util.List<main.entity.HopDong> list) {
//    int c = 0;
//    java.time.LocalDate today = java.time.LocalDate.now();
//    for (main.entity.HopDong hd : list) {
//        java.util.Date bd = hd.getNgayBatDau();
//        java.util.Date kt = hd.getNgayKetThuc();
//
//        if (bd == null) continue; // thiếu ngày bắt đầu thì bỏ qua
//
//        java.time.LocalDate start = toLocal(bd);
//        java.time.LocalDate end   = (kt == null) ? null : toLocal(kt);
//
//        boolean started = !today.isBefore(start);                  // today >= start
//        boolean notEnded = (end == null) || !today.isAfter(end);   // end == null || today <= end
//        if (started && notEnded) c++;
//    }
//    return c;
//}
//
//// ===== Đếm hợp đồng SẮP HẾT HẠN trong N ngày tới =====
//private int countSapHetHan(java.util.List<main.entity.HopDong> list, int daysAhead) {
//    int c = 0;
//    java.time.LocalDate today = java.time.LocalDate.now();
//    java.time.LocalDate limit = today.plusDays(daysAhead);
//
//    for (main.entity.HopDong hd : list) {
//        java.util.Date kt = hd.getNgayKetThuc();
//        if (kt == null) continue;                    // không có ngày kết thúc thì bỏ qua
//        java.time.LocalDate end = toLocal(kt);
//
//        // end ∈ [today, limit]
//        if ((!end.isBefore(today)) && (!end.isAfter(limit))) c++;
//    }
//    return c;
//}
//
//
//    private String formatMoney(long v) {
//        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi","VN"));
//        nf.setMaximumFractionDigits(0);
//        return nf.format(v);
//    }
//    private double nz(Double d){ return d==null?0d:d; }
//    private String pad2(int n){ return String.format("%02d", n); }
//
//    private void buildTable() {
//        DefaultTableModel m = new DefaultTableModel(new Object[]{
//            "Mã HĐơn","Mã HĐồng","Ngày tạo","Trạng thái","Tổng tiền"
//        }, 0) {
//            @Override public boolean isCellEditable(int r,int c){ return false; }
//        };
//        tblTrangChu.setModel(m);
//        tblTrangChu.setRowHeight(26);
//    }
//
//    // ===== open dialogs =====
//    @Override public void openPhong()   { tryOpenDialog(PhongJDialog.class); }
//    @Override public void openHopDong() { tryOpenDialog(HopDongJDialog.class); }
//    @Override public void openHoaDon()  { tryOpenDialog(HoaDonJDialog.class); }
//    @Override public void openTaiKhoan(){ tryOpenDialog(TaiKhoanJDialog.class); }
//
//    private void tryOpenDialog(Class<? extends javax.swing.JDialog> clazz) {
//        try {
//            java.lang.reflect.Constructor<? extends javax.swing.JDialog> c =
//                    clazz.getConstructor(java.awt.Frame.class, boolean.class);
//            javax.swing.JDialog dlg = c.newInstance(this, true);
//            dlg.setVisible(true);
//            // sau khi đóng dialog, refresh dashboard
//            loadDashboard();
//        } catch (NoSuchMethodException e) {
//            JOptionPane.showMessageDialog(this, "Dialog " + clazz.getSimpleName() + " thiếu constructor (Frame, boolean).");
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Không mở được " + clazz.getSimpleName() + ": " + e.getMessage());
//        }
//    }

 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpnView = new javax.swing.JPanel();
        lblNull = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblThangDoanhSo = new javax.swing.JLabel();
        lblDoanhSo = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        lblHopDong = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lblSoKhach = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        lblHoaDon = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblphongtro = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuTrangChu = new javax.swing.JMenu();
        jMenuPhongTro = new javax.swing.JMenu();
        jMenuHopDong = new javax.swing.JMenu();
        jMenuHoaDon = new javax.swing.JMenu();
        jMtaikhoan = new javax.swing.JMenu();
        jMenuAbout = new javax.swing.JMenu();
        jMenuNguoiDung = new javax.swing.JMenu();
        jMenuThongTin = new javax.swing.JMenu();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuLichSuHD = new javax.swing.JMenu();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        btnThoat = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        btnDangXuat = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jpnView.setBackground(new java.awt.Color(40, 46, 62));
        jpnView.setPreferredSize(new java.awt.Dimension(1100, 630));
        jpnView.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jpnView.add(lblNull, new org.netbeans.lib.awtextra.AbsoluteConstraints(119, 161, 140, 81));

        jPanel1.setBackground(new java.awt.Color(46, 56, 86));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 205, 31), 2));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("TỔNG DOANH THU");

        lblThangDoanhSo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblThangDoanhSo.setForeground(new java.awt.Color(255, 205, 31));
        lblThangDoanhSo.setText("01");

        lblDoanhSo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblDoanhSo.setForeground(new java.awt.Color(255, 205, 31));
        lblDoanhSo.setText("100,000,000 VND");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblThangDoanhSo))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(lblDoanhSo)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblThangDoanhSo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDoanhSo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpnView.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 34, -1, -1));

        jPanel5.setBackground(new java.awt.Color(46, 56, 86));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 205, 31), 2));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("SỐ HỢP ĐỒNG SẮP HẾT HẠN");

        lblHopDong.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblHopDong.setForeground(new java.awt.Color(255, 205, 31));
        lblHopDong.setText("XX");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(22, 22, 22))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(lblHopDong)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHopDong)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpnView.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(528, 34, -1, 109));

        jPanel4.setBackground(new java.awt.Color(46, 56, 86));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 205, 31), 2));
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("SỐ KHÁCH TRỌ ĐANG THUÊ");

        lblSoKhach.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblSoKhach.setForeground(new java.awt.Color(255, 205, 31));
        lblSoKhach.setText("XX");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(lblSoKhach)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(15, 15, 15))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSoKhach)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpnView.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 34, -1, 109));

        jPanel6.setBackground(new java.awt.Color(46, 56, 86));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 205, 31), 2));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("HÓA ĐƠN CHƯA THANH TOÁN");
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
        });

        lblHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblHoaDon.setForeground(new java.awt.Color(255, 205, 31));
        lblHoaDon.setText("XX");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel14))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addComponent(lblHoaDon)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHoaDon)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpnView.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(788, 34, -1, 109));

        tblphongtro.setBackground(new java.awt.Color(207, 243, 243));
        tblphongtro.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tblphongtro.setForeground(new java.awt.Color(40, 46, 62));
        tblphongtro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblphongtro.setGridColor(new java.awt.Color(255, 255, 255));
        tblphongtro.setSelectionBackground(new java.awt.Color(46, 56, 86));
        tblphongtro.setSelectionForeground(new java.awt.Color(255, 205, 31));
        jScrollPane1.setViewportView(tblphongtro);

        jpnView.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 166, 1012, 310));

        jMenuBar1.setBackground(new java.awt.Color(46, 56, 86));
        jMenuBar1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 205, 31), 2));
        jMenuBar1.setForeground(new java.awt.Color(40, 46, 62));
        jMenuBar1.setPreferredSize(new java.awt.Dimension(615, 70));

        jMenuTrangChu.setForeground(new java.awt.Color(255, 255, 255));
        jMenuTrangChu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/App/Icon/home (1).png"))); // NOI18N
        jMenuTrangChu.setText("Trang chủ");
        jMenuTrangChu.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jMenuBar1.add(jMenuTrangChu);

        jMenuPhongTro.setForeground(new java.awt.Color(255, 255, 255));
        jMenuPhongTro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/App/Icon/home-page.png"))); // NOI18N
        jMenuPhongTro.setText("Phòng trọ");
        jMenuPhongTro.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jMenuPhongTro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuPhongTroMouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenuPhongTro);

        jMenuHopDong.setForeground(new java.awt.Color(255, 255, 255));
        jMenuHopDong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/App/Icon/rent (1).png"))); // NOI18N
        jMenuHopDong.setText("Hợp đồng");
        jMenuHopDong.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jMenuHopDong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuHopDongMouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenuHopDong);

        jMenuHoaDon.setForeground(new java.awt.Color(255, 255, 255));
        jMenuHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/App/Icon/bill (1).png"))); // NOI18N
        jMenuHoaDon.setText("Hóa đơn");
        jMenuHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jMenuHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuHoaDonMouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenuHoaDon);

        jMtaikhoan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/App/Icon/Clien list.png"))); // NOI18N
        jMtaikhoan.setText("Tài khoản");
        jMtaikhoan.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jMtaikhoan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMtaikhoanMouseClicked(evt);
            }
        });
        jMenuBar1.add(jMtaikhoan);

        jMenuAbout.setForeground(new java.awt.Color(255, 255, 255));
        jMenuAbout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/App/Icon/Gear.png"))); // NOI18N
        jMenuAbout.setText("About");
        jMenuAbout.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N

        jMenuNguoiDung.setForeground(new java.awt.Color(40, 46, 62));
        jMenuNguoiDung.setText("Người dùng");
        jMenuNguoiDung.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuThongTin.setForeground(new java.awt.Color(40, 46, 62));
        jMenuThongTin.setText("Thông tin cá nhân");
        jMenuThongTin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuThongTin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuThongTinMouseClicked(evt);
            }
        });
        jMenuNguoiDung.add(jMenuThongTin);
        jMenuNguoiDung.add(jSeparator5);

        jMenuLichSuHD.setForeground(new java.awt.Color(40, 46, 62));
        jMenuLichSuHD.setText("Lịch sử hoạt động");
        jMenuLichSuHD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuLichSuHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuLichSuHDMouseClicked(evt);
            }
        });
        jMenuNguoiDung.add(jMenuLichSuHD);

        jMenuAbout.add(jMenuNguoiDung);
        jMenuAbout.add(jSeparator4);

        btnThoat.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        btnThoat.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnThoat.setForeground(new java.awt.Color(40, 46, 62));
        btnThoat.setText("Thoát");
        btnThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoatActionPerformed(evt);
            }
        });
        jMenuAbout.add(btnThoat);
        jMenuAbout.add(jSeparator6);

        btnDangXuat.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        btnDangXuat.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnDangXuat.setForeground(new java.awt.Color(40, 46, 62));
        btnDangXuat.setText("Đăng xuất");
        btnDangXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangXuatActionPerformed(evt);
            }
        });
        jMenuAbout.add(btnDangXuat);

        jMenuBar1.add(jMenuAbout);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpnView, javax.swing.GroupLayout.PREFERRED_SIZE, 1068, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpnView, javax.swing.GroupLayout.PREFERRED_SIZE, 563, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatActionPerformed
        // TODO add your handling code here:
    if (JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc muốn thoát?",
            "Xác nhận thoát",
            JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION) {
        System.exit(0);
    }
    }//GEN-LAST:event_btnThoatActionPerformed

    private void btnDangXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangXuatActionPerformed
        // TODO add your handling code here:
    // Đóng form hiện tại
    this.dispose();

    // Mở lại form TrangChuChung
    try {
        // Ép lại Look & Feel để giữ màu/kiểu như ban đầu (tránh thanh progress đổi màu)
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ignored) {}

    TrangChuChung home = new TrangChuChung();
    SwingUtilities.updateComponentTreeUI(home); // áp LAF cho form mới
    home.setLocationRelativeTo(null);
    home.setVisible(true);
    }//GEN-LAST:event_btnDangXuatActionPerformed

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
//        NguoiThue1 tr = new NguoiThue1();
//        tr.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel4MouseClicked

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
//        try {
//            HoaDonCtt ctt = new HoaDonCtt();
//            ctt.setVisible(true);
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel14MouseClicked

    private void jMenuPhongTroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuPhongTroMouseClicked
        // TODO add your handling code here:
        this.showPhongJDialog(this); 
    }//GEN-LAST:event_jMenuPhongTroMouseClicked

    private void jMenuHopDongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuHopDongMouseClicked
        // TODO add your handling code here:
                this.showHopDongJDialog(this); 
    }//GEN-LAST:event_jMenuHopDongMouseClicked

    private void jMenuHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuHoaDonMouseClicked
        // TODO add your handling code here:
                this.showHoaDonJDialog(this); 
    }//GEN-LAST:event_jMenuHoaDonMouseClicked

    private void jMtaikhoanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMtaikhoanMouseClicked
        // TODO add your handling code here:
                this.showTaiKhoanJDialog(this); 
    }//GEN-LAST:event_jMtaikhoanMouseClicked

    private void jMenuThongTinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuThongTinMouseClicked
        // TODO add your handling code here:
                this.showThongTinNguoiDungJDialog(this); 
    }//GEN-LAST:event_jMenuThongTinMouseClicked

    private void jMenuLichSuHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuLichSuHDMouseClicked
        // TODO add your handling code here:
                this.showLichSuNguoiDungJDialog(this); 
    }//GEN-LAST:event_jMenuLichSuHDMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("FlatLaf Light".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main_ChuTro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main_ChuTro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main_ChuTro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main_ChuTro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Main_ChuTro().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Main_ChuTro.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem btnDangXuat;
    private javax.swing.JMenuItem btnThoat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JMenu jMenuAbout;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuHoaDon;
    private javax.swing.JMenu jMenuHopDong;
    private javax.swing.JMenu jMenuLichSuHD;
    private javax.swing.JMenu jMenuNguoiDung;
    private javax.swing.JMenu jMenuPhongTro;
    private javax.swing.JMenu jMenuThongTin;
    private javax.swing.JMenu jMenuTrangChu;
    private javax.swing.JMenu jMtaikhoan;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPanel jpnView;
    private javax.swing.JLabel lblDoanhSo;
    private javax.swing.JLabel lblHoaDon;
    private javax.swing.JLabel lblHopDong;
    private javax.swing.JLabel lblNull;
    private javax.swing.JLabel lblSoKhach;
    private javax.swing.JLabel lblThangDoanhSo;
    private javax.swing.JTable tblphongtro;
    // End of variables declaration//GEN-END:variables
}
