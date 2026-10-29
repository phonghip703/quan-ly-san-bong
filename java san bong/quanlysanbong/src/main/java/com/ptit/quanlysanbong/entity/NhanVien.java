package com.ptit.quanlysanbong.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "NHAN_VIEN")
@Data
public class NhanVien {
    @Id
    @Column(name = "Ma_NV", length = 20)
    private String maNV;

    @Column(name = "CCCD_CMT", nullable = false, unique = true, length = 20)
    private String cccdCmt;

    @Column(name = "Ho_Tendem", length = 50)
    private String hoTendem;

    @Column(name = "Ten", nullable = false, length = 50)
    private String ten;

    @Column(name = "Ngay_Sinh")
    private LocalDate ngaySinh;

    @Column(name = "Dia_chi", length = 255)
    private String diaChi;

    @Column(name = "So_dien_thoai", length = 15)
    private String soDienThoai;

    @Column(name = "Chuc_vu", nullable = false, length = 50)
    private String chucVu; // 'QuanLy' hoặc 'NhanVienTrucCa'

    @Column(name = "Luong_cung_gio", nullable = false)
    private Double luongCungGio;
}