package com.ptit.quanlysanbong.controller;

import com.ptit.quanlysanbong.entity.*;
import com.ptit.quanlysanbong.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired private KhachHangRepository khachHangRepository;
    @Autowired private SanBongRepository sanBongRepository;
    @Autowired private NhanVienRepository nhanVienRepository;
    @Autowired private DichVuRepository dichVuRepository;
    @Autowired private LuotDatSanRepository luotDatSanRepository;

    // Hỗ trợ cả hai đường dẫn / và /index
    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("danhSachSan", sanBongRepository.findAll());
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, 
                              @RequestParam String password, 
                              HttpSession session, 
                              RedirectAttributes redirectAttributes) {
        
        // TÙY CHỈNH TÀI KHOẢN VÀ MẬT KHẨU CHỦ SÂN TẠI ĐÂY:
        String adminUsername = "HPH";
        String adminPassword = "HPHdeptraiso1tg";

        if (adminUsername.equals(username) && adminPassword.equals(password)) {
            session.setAttribute("userRole", "ADMIN");
            return "redirect:/admin";
        } 

        KhachHang khach = khachHangRepository.findAll().stream()
                .filter(k -> username.equals(k.getUsername()) || username.equals(k.getSoDienThoai()))
                .filter(k -> password.equals(k.getPassword()))
                .findFirst()
                .orElse(null);

        if (khach != null) {
            session.setAttribute("userRole", "USER");
            session.setAttribute("currentKhachHang", khach);
            return "redirect:/tra-cuu-san";
        }

        redirectAttributes.addFlashAttribute("error", "Tên đăng nhập (SĐT) hoặc mật khẩu không chính xác!");
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("khachHangMoi", new KhachHang());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("khachHangMoi") KhachHang khachHang, 
                                 @RequestParam("matKhau") String matKhau, 
                                 RedirectAttributes redirectAttributes) {
        if (khachHang.getDiemUyTin() == null) {
            khachHang.setDiemUyTin(100);
        }
        
        khachHang.setUsername(khachHang.getSoDienThoai());
        khachHang.setPassword(matKhau);
        khachHangRepository.save(khachHang);

        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Hãy đăng nhập bằng SĐT: " + khachHang.getSoDienThoai());
        return "redirect:/login";
    }

    @GetMapping("/tra-cuu-san")
    public String traCuuSan(@RequestParam(required = false) String ngay, Model model) {
        LocalDate ngayDat = (ngay != null && !ngay.isEmpty()) ? LocalDate.parse(ngay) : LocalDate.now();
        model.addAttribute("ngayChon", ngayDat.toString());
        
        List<SanBong> listSan = sanBongRepository.findAll();
        List<LuotDatSan> listDat = luotDatSanRepository.findAll();

        List<String> keysDaDat = new ArrayList<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (LuotDatSan l : listDat) {
            if (l.getSanBong() != null && l.getNgayDat() != null && l.getGioBatDau() != null) {
                // Không đánh dấu đã đặt nếu lượt đó đã bị HỦY
                if (l.getNgayDat().equals(ngayDat) && !"DaHuy".equals(l.getTrangThai()) && !"Đã Hủy".equals(l.getTrangThai())) {
                    String gioBD = l.getGioBatDau().format(timeFormatter);
                    
                    if (l.getSanBong().getMaSan() != null) {
                        keysDaDat.add(l.getSanBong().getMaSan().trim() + "_" + gioBD);
                    }
                    if (l.getSanBong().getTenSan() != null) {
                        keysDaDat.add(l.getSanBong().getTenSan().trim() + "_" + gioBD);
                    }
                }
            }
        }

        model.addAttribute("danhSachSan", listSan);
        model.addAttribute("danhSachDaDatKey", keysDaDat);
        return "tra-cuu-san";
    }

    @PostMapping("/dat-san-user")
    public String handleUserDatSan(@RequestParam(required = false) String maSan,
                                   @RequestParam String ngayDat,
                                   @RequestParam String gioBatDau,
                                   @RequestParam String gioKetThuc,
                                   @RequestParam(required = false) String hoTenKhach,
                                   @RequestParam String soDienThoai,
                                   @RequestParam Double tienCoc,
                                   RedirectAttributes redirectAttributes) {
        try {
            String gBD = gioBatDau.length() == 5 ? gioBatDau + ":00" : gioBatDau;
            String gKT = gioKetThuc.length() == 5 ? gioKetThuc + ":00" : gioKetThuc;
            LocalDate ngay = LocalDate.parse(ngayDat);
            LocalTime gioStart = LocalTime.parse(gBD);

            SanBong sanBong = null;
            if (maSan != null && !maSan.isEmpty()) {
                sanBong = sanBongRepository.findById(maSan.trim()).orElse(null);
            }
            if (sanBong == null) {
                List<SanBong> ds = sanBongRepository.findAll();
                if (!ds.isEmpty()) {
                    sanBong = ds.get(0);
                } else {
                    sanBong = new SanBong();
                    sanBong.setMaSan("SAN01");
                    sanBong.setTenSan("Sân Số 1");
                    sanBong.setLoaiSan("Sân 7 người");
                    sanBongRepository.save(sanBong);
                }
            }

            // Kiểm tra trùng lịch đặt
            List<LuotDatSan> trungLich = luotDatSanRepository.findBySanBongMaSanAndNgayDatAndGioBatDau(sanBong.getMaSan(), ngay, gioStart)
                    .stream()
                    .filter(l -> !"DaHuy".equals(l.getTrangThai()) && !"Đã Hủy".equals(l.getTrangThai()))
                    .toList();

            if (!trungLich.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "⚠️ Khung giờ " + gioBatDau + " - " + gioKetThuc + " của " + sanBong.getTenSan() + " đã có người đặt!");
                return "redirect:/tra-cuu-san?ngay=" + ngayDat;
            }

            KhachHang khachHang = khachHangRepository.findAll().stream()
                    .filter(k -> soDienThoai.equals(k.getSoDienThoai()))
                    .findFirst()
                    .orElse(null);

            if (khachHang == null) {
                khachHang = new KhachHang();
                khachHang.setCccdCmt("KH_" + System.currentTimeMillis());
                khachHang.setTenDoiBong("FC " + soDienThoai);
                khachHang.setTen((hoTenKhach != null && !hoTenKhach.isEmpty()) ? hoTenKhach : "Khách " + soDienThoai);
                khachHang.setSoDienThoai(soDienThoai);
                khachHang.setUsername(soDienThoai);
                khachHang.setPassword("123456");
                khachHang.setDiemUyTin(100);
                khachHang = khachHangRepository.save(khachHang);
            }

            LuotDatSan luotDat = new LuotDatSan();
            luotDat.setSanBong(sanBong);
            luotDat.setKhachHang(khachHang);
            luotDat.setNgayDat(ngay);
            luotDat.setGioBatDau(gioStart);
            luotDat.setGioKetThuc(LocalTime.parse(gKT));
            luotDat.setTienCoc(tienCoc);
            luotDat.setTrangThai("DaCoc");

            luotDatSanRepository.save(luotDat);

            // Truyền dữ liệu để tự động mở Pop-up QR thanh toán
            redirectAttributes.addFlashAttribute("success", "Đặt sân thành công!");
            redirectAttributes.addFlashAttribute("showQR", true);
            redirectAttributes.addFlashAttribute("tienCocQR", tienCoc);
            redirectAttributes.addFlashAttribute("sdtKhach", soDienThoai);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi đặt sân: " + e.getMessage());
        }
        return "redirect:/tra-cuu-san?ngay=" + ngayDat;
    }

    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        KhachHang khach = (KhachHang) session.getAttribute("currentKhachHang");
        if (khach == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để xem thông tin cá nhân!");
            return "redirect:/login";
        }

        List<LuotDatSan> dsDat = luotDatSanRepository.findAll().stream()
                .filter(l -> l.getKhachHang() != null && khach.getSoDienThoai().equals(l.getKhachHang().getSoDienThoai()))
                .toList();

        model.addAttribute("khachHang", khach);
        model.addAttribute("danhSachDaDat", dsDat);
        return "profile";
    }

    // XỬ LÝ HỦY ĐẶT SÂN CHO KHÁCH HÀNG
    @PostMapping("/huy-dat-san")
    public String huyDatSan(@RequestParam("maLuotDat") Long maLuotDat, 
                            HttpSession session, 
                            RedirectAttributes redirectAttributes) {
        KhachHang khach = (KhachHang) session.getAttribute("currentKhachHang");
        if (khach == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập!");
            return "redirect:/login";
        }

        luotDatSanRepository.findById(maLuotDat).ifPresent(luotDat -> {
            if (luotDat.getKhachHang() != null && khach.getSoDienThoai().equals(luotDat.getKhachHang().getSoDienThoai())) {
                luotDat.setTrangThai("DaHuy");
                luotDatSanRepository.save(luotDat);
                redirectAttributes.addFlashAttribute("success", "Hủy lượt đặt sân #" + maLuotDat + " thành công!");
            }
        });

        return "redirect:/profile";
    }

    // TRANG ADMIN DASHBOARD
    @GetMapping("/admin")
public String adminDashboard(@RequestParam(name = "cccdSelect", required = false) String cccdSelect, Model model) {
    model.addAttribute("danhSachKhachHang", khachHangRepository.findAll());
    model.addAttribute("danhSachSan", sanBongRepository.findAll());
    model.addAttribute("danhSachNhanVien", nhanVienRepository.findAll());
    model.addAttribute("danhSachDichVu", dichVuRepository.findAll());
    
    // ĐÃ SỬA: Sắp xếp ngày & giờ đá từ sớm nhất đến muộn nhất (lên đầu)
    model.addAttribute("danhSachDatSan", luotDatSanRepository.findAllByOrderByNgayDatAscGioBatDauAsc());

    model.addAttribute("khachHangMoi", new KhachHang());
    model.addAttribute("luotDatMoi", new LuotDatSan());

    if (cccdSelect != null && !cccdSelect.isEmpty()) {
        khachHangRepository.findById(cccdSelect).ifPresent(kh -> {
            model.addAttribute("khachHangChon", kh);
            model.addAttribute("lichSuDatCuaKhach", luotDatSanRepository.findByKhachHang_CccdCmtOrderByNgayDatDesc(cccdSelect));
        });
    }

    return "admin-dashboard";
}

    // XÁC NHẬN THANH TOÁN CHO CHỦ SÂN
    @PostMapping("/admin/xac-nhan")
    public String xacNhanLuotDat(@RequestParam("maLuotDat") Long maLuotDat, RedirectAttributes redirectAttributes) {
        luotDatSanRepository.findById(maLuotDat).ifPresent(luotDat -> {
            luotDat.setTrangThai("DaThanhToan");
            luotDatSanRepository.save(luotDat);
        });
        redirectAttributes.addFlashAttribute("success", "Ghi nhận thanh toán thành công!");
        return "redirect:/admin";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}