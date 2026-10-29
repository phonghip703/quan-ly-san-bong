package com.ptit.quanlysanbong.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "luot_dat_san")
public class LuotDatSan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_luot_dat")
    private Long maLuotDat;

    // QUAN TRỌNG: Chỉ định tên cột chính xác trùng 100% với MySQL (CCCD_KhachHang)
    @ManyToOne
    @JoinColumn(name = "CCCD_KhachHang", referencedColumnName = "cccd_cmt")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "ma_san", referencedColumnName = "ma_san")
    private SanBong sanBong;

    @ManyToOne
    @JoinColumn(name = "ma_nv_xac_nhan", referencedColumnName = "ma_nv", nullable = true)
    private NhanVien nhanVienXacNhan;

    @Column(name = "ngay_dat")
    private LocalDate ngayDat;

    @Column(name = "gio_bat_dau")
    private LocalTime gioBatDau;

    @Column(name = "gio_ket_thuc")
    private LocalTime gioKetThuc;

    @Column(name = "tien_coc")
    private Double tienCoc;

    @Column(name = "trang_thai")
    private String trangThai;

    public LuotDatSan() {}

    // Getters & Setters
    public Long getMaLuotDat() { return maLuotDat; }
    public void setMaLuotDat(Long maLuotDat) { this.maLuotDat = maLuotDat; }

    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }

    public SanBong getSanBong() { return sanBong; }
    public void setSanBong(SanBong sanBong) { this.sanBong = sanBong; }

    public NhanVien getNhanVienXacNhan() { return nhanVienXacNhan; }
    public void setNhanVienXacNhan(NhanVien nhanVienXacNhan) { this.nhanVienXacNhan = nhanVienXacNhan; }

    public LocalDate getNgayDat() { return ngayDat; }
    public void setNgayDat(LocalDate ngayDat) { this.ngayDat = ngayDat; }

    public LocalTime getGioBatDau() { return gioBatDau; }
    public void setGioBatDau(LocalTime gioBatDau) { this.gioBatDau = gioBatDau; }

    public LocalTime getGioKetThuc() { return gioKetThuc; }
    public void setGioKetThuc(LocalTime gioKetThuc) { this.gioKetThuc = gioKetThuc; }

    public Double getTienCoc() { return tienCoc; }
    public void setTienCoc(Double tienCoc) { this.tienCoc = tienCoc; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}