package com.ptit.quanlysanbong.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "SAN_BONG")
@Data
public class SanBong {
    @Id
    @Column(name = "Ma_san", length = 20)
    private String maSan;

    @Column(name = "Ten_san", nullable = false, length = 50)
    private String tenSan;

    @Column(name = "Loai_san", nullable = false, length = 20)
    private String loaiSan;

    @Column(name = "Don_gia_gio_thuong", nullable = false)
    private Double donGiaGioThuong;

    @Column(name = "Don_gia_gio_cao_diem", nullable = false)
    private Double donGiaGioCaoDiem;
}