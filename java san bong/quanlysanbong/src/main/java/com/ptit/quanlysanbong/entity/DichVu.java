package com.ptit.quanlysanbong.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "DICH_VU")
@Data
public class DichVu {
    @Id
    @Column(name = "Ma_DV", length = 20)
    private String maDV;

    @Column(name = "Ten_DV", nullable = false, length = 100)
    private String tenDV;

    @Column(name = "Don_gia_DV", nullable = false)
    private Double donGiaDV;

    @Column(name = "So_luong_ton", nullable = false)
    private Integer soLuongTon = 0;
}