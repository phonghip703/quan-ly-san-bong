package com.ptit.quanlysanbong.repository;

import com.ptit.quanlysanbong.entity.LuotDatSan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface LuotDatSanRepository extends JpaRepository<LuotDatSan, Long> {
    
    // Tìm các lượt đặt sân trùng Mã Sân, Ngày Đặt và Giờ Bắt Đầu
    List<LuotDatSan> findBySanBongMaSanAndNgayDatAndGioBatDau(String maSan, LocalDate ngayDat, LocalTime gioBatDau);
}