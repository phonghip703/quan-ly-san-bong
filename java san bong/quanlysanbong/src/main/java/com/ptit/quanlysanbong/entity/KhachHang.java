package com.ptit.quanlysanbong.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "khach_hang")
public class KhachHang {

    @Id
    @Column(name = "cccd_cmt")
    private String cccdCmt;

    @Column(name = "ten_doi_bong")
    private String tenDoiBong;

    @Column(name = "ho_tendem")
    private String hoTendem;

    @Column(name = "ten")
    private String ten;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "diem_uy_tin")
    private Integer diemUyTin;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    public KhachHang() {}

    // Getters & Setters
    public String getCccdCmt() { return cccdCmt; }
    public void setCccdCmt(String cccdCmt) { this.cccdCmt = cccdCmt; }

    public String getTenDoiBong() { return tenDoiBong; }
    public void setTenDoiBong(String tenDoiBong) { this.tenDoiBong = tenDoiBong; }

    public String getHoTendem() { return hoTendem; }
    public void setHoTendem(String hoTendem) { this.hoTendem = hoTendem; }

    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public Integer getDiemUyTin() { return diemUyTin; }
    public void setDiemUyTin(Integer diemUyTin) { this.diemUyTin = diemUyTin; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}