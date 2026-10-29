DROP DATABASE IF EXISTS quan_ly_san_bong;
CREATE DATABASE quan_ly_san_bong DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE quan_ly_san_bong;

-- ========================================================
-- 1. BẢNG KHÁCH HÀNG
-- ========================================================
CREATE TABLE khach_hang (
    cccd_cmt VARCHAR(20) PRIMARY KEY,
    ten_doi_bong VARCHAR(100),
    ho_tendem VARCHAR(50),
    ten VARCHAR(50),
    ngay_sinh DATE,
    dia_chi VARCHAR(255),
    so_dien_thoai VARCHAR(15),
    diem_uy_tin INT DEFAULT 100,
    username VARCHAR(50),
    password VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================================
-- 2. BẢNG SÂN BÓNG
-- ========================================================
CREATE TABLE san_bong (
    ma_san VARCHAR(20) PRIMARY KEY,
    ten_san VARCHAR(50) NOT NULL,
    loai_san VARCHAR(20),
    don_gia_gio_thuong DECIMAL(12,2),
    don_gia_gio_cao_diem DECIMAL(12,2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================================
-- 3. BẢNG NHÂN VIÊN
-- ========================================================
CREATE TABLE nhan_vien (
    ma_nv VARCHAR(20) PRIMARY KEY,
    cccd_cmt VARCHAR(20),
    ho_tendem VARCHAR(50),
    ten VARCHAR(50),
    ngay_sinh DATE,
    dia_chi VARCHAR(255),
    so_dien_thoai VARCHAR(15),
    chuc_vu VARCHAR(50),
    luong_cung_gio DECIMAL(12,2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================================
-- 4. BẢNG DỊCH VỤ
-- ========================================================
CREATE TABLE dich_vu (
    ma_dv VARCHAR(20) PRIMARY KEY,
    ten_dv VARCHAR(100) NOT NULL,
    don_gia_dv DECIMAL(12,2),
    so_luong_ton INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================================
-- 5. BẢNG LƯỢT ĐẶT SÂN (Đồng bộ chuẩn khóa ngoại)
-- ========================================================
CREATE TABLE luot_dat_san (
    ma_luot_dat BIGINT AUTO_INCREMENT PRIMARY KEY,
    ngay_dat DATE NOT NULL,
    gio_bat_dau TIME NOT NULL,
    gio_ket_thuc TIME NOT NULL,
    tien_coc DECIMAL(12,2) DEFAULT 0,
    trang_thai VARCHAR(30) DEFAULT 'DaCoc',
    CCCD_KhachHang VARCHAR(20),
    ma_san VARCHAR(20),
    ma_nv_xac_nhan VARCHAR(20),
    CONSTRAINT fk_luotdat_khachhang FOREIGN KEY (CCCD_KhachHang) REFERENCES khach_hang(cccd_cmt) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_luotdat_sanbong FOREIGN KEY (ma_san) REFERENCES san_bong(ma_san) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_luotdat_nhanvien FOREIGN KEY (ma_nv_xac_nhan) REFERENCES nhan_vien(ma_nv) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================================
-- 6. BẢNG CHI TIẾT SỬ DỤNG DỊCH VỤ
-- ========================================================
CREATE TABLE chi_tiet_su_dung_dv (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_luot_dat BIGINT NOT NULL,
    ma_dv VARCHAR(20) NOT NULL,
    so_luong_don INT DEFAULT 1,
    don_gia_dv DECIMAL(12,2),
    CONSTRAINT fk_chitiet_luotdat FOREIGN KEY (ma_luot_dat) REFERENCES luot_dat_san(ma_luot_dat) ON DELETE CASCADE,
    CONSTRAINT fk_chitiet_dichvu FOREIGN KEY (ma_dv) REFERENCES dich_vu(ma_dv) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ========================================================
-- DỮ LIỆU MẪU BAN ĐẦU
-- ========================================================
INSERT INTO san_bong (ma_san, ten_san, loai_san, don_gia_gio_thuong, don_gia_gio_cao_diem) VALUES
('SAN01', 'Sân Cỏ Nhân Tạo A1', 'Sân 5 người', 150000, 250000),
('SAN02', 'Sân Cỏ Nhân Tạo A2', 'Sân 7 người', 200000, 300000),
('SAN03', 'Sân VIP 1', 'Sân 11 người', 400000, 600000);


-- ========================================================
-- STORED PROCEDURES KHÔNG LỖI
-- ========================================================
DELIMITER //

-- 1. Thêm Khách Hàng
CREATE PROCEDURE sp_ThemKhachHang(
    IN p_CCCD VARCHAR(20),
    IN p_TenDoiBong VARCHAR(100),
    IN p_HoTendem VARCHAR(50),
    IN p_Ten VARCHAR(50),
    IN p_NgaySinh DATE,
    IN p_DiaChi VARCHAR(255),
    IN p_Sdt VARCHAR(15),
    IN p_DiemUyTin INT
)
BEGIN
    INSERT INTO khach_hang (cccd_cmt, ten_doi_bong, ho_tendem, ten, ngay_sinh, dia_chi, so_dien_thoai, diem_uy_tin, username, password)
    VALUES (p_CCCD, p_TenDoiBong, p_HoTendem, p_Ten, p_NgaySinh, p_DiaChi, p_Sdt, IFNULL(p_DiemUyTin, 100), p_Sdt, '123456');
END //

-- 2. Thêm Sân Bóng
CREATE PROCEDURE sp_ThemSanBong(
    IN p_MaSan VARCHAR(20),
    IN p_TenSan VARCHAR(50),
    IN p_LoaiSan VARCHAR(20),
    IN p_GiaThuong DECIMAL(12,2),
    IN p_GiaCaoDiem DECIMAL(12,2)
)
BEGIN
    INSERT INTO san_bong (ma_san, ten_san, loai_san, don_gia_gio_thuong, don_gia_gio_cao_diem)
    VALUES (p_MaSan, p_TenSan, p_LoaiSan, p_GiaThuong, p_GiaCaoDiem);
END //

-- 3. Thêm Nhân Viên
CREATE PROCEDURE sp_ThemNhanVien(
    IN p_MaNV VARCHAR(20),
    IN p_CCCD VARCHAR(20),
    IN p_HoTendem VARCHAR(50),
    IN p_Ten VARCHAR(50),
    IN p_NgaySinh DATE,
    IN p_DiaChi VARCHAR(255),
    IN p_Sdt VARCHAR(15),
    IN p_ChucVu VARCHAR(50),
    IN p_LuongGio DECIMAL(12,2)
)
BEGIN
    INSERT INTO nhan_vien (ma_nv, cccd_cmt, ho_tendem, ten, ngay_sinh, dia_chi, so_dien_thoai, chuc_vu, luong_cung_gio)
    VALUES (p_MaNV, p_CCCD, p_HoTendem, p_Ten, p_NgaySinh, p_DiaChi, p_Sdt, p_ChucVu, p_LuongGio);
END //

-- 4. Thêm Dịch Vụ
CREATE PROCEDURE sp_ThemDichVu(
    IN p_MaDV VARCHAR(20),
    IN p_TenDV VARCHAR(100),
    IN p_DonGia DECIMAL(12,2),
    IN p_SoLuongTon INT
)
BEGIN
    INSERT INTO dich_vu (ma_dv, ten_dv, don_gia_dv, so_luong_ton)
    VALUES (p_MaDV, p_TenDV, p_DonGia, p_SoLuongTon);
END //

-- 5. Tạo Lượt Đặt Sân
CREATE PROCEDURE sp_TaoLuotDatSan(
    IN p_NgayDat DATE,
    IN p_GioBatDau TIME,
    IN p_GioKetThuc TIME,
    IN p_TienCoc DECIMAL(12,2),
    IN p_TrangThai VARCHAR(30),
    IN p_CCCDKhach VARCHAR(20),
    IN p_MaSan VARCHAR(20),
    IN p_MaNVXacNhan VARCHAR(20)
)
BEGIN
    INSERT INTO luot_dat_san (ngay_dat, gio_bat_dau, gio_ket_thuc, tien_coc, trang_thai, CCCD_KhachHang, ma_san, ma_nv_xac_nhan)
    VALUES (p_NgayDat, p_GioBatDau, p_GioKetThuc, IFNULL(p_TienCoc, 0), IFNULL(p_TrangThai, 'DaCoc'), p_CCCDKhach, p_MaSan, p_MaNVXacNhan);
END //

-- 6. Sử Dụng Dịch Vụ
CREATE PROCEDURE sp_SuDungDichVu(
    IN p_MaLuotDat BIGINT,
    IN p_MaDV VARCHAR(20),
    IN p_SoLuong INT
)
BEGIN
    DECLARE v_DonGia DECIMAL(12,2);
    SELECT don_gia_dv INTO v_DonGia FROM dich_vu WHERE ma_dv = p_MaDV;
    
    INSERT INTO chi_tiet_su_dung_dv (ma_luot_dat, ma_dv, so_luong_don, don_gia_dv)
    VALUES (p_MaLuotDat, p_MaDV, p_SoLuong, v_DonGia);
END //

DELIMITER ;