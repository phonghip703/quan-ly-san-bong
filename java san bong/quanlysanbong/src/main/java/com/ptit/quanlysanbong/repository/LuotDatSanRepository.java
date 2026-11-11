package com.ptit.quanlysanbong.repository;

import com.ptit.quanlysanbong.entity.LuotDatSan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface LuotDatSanRepository extends JpaRepository<LuotDatSan, Long> {
    
    List<LuotDatSan> findBySanBongMaSanAndNgayDatAndGioBatDau(String maSan, LocalDate ngayDat, LocalTime gioBatDau);

    List<LuotDatSan> findByKhachHang_CccdCmtOrderByNgayDatDesc(String cccdCmt);

    // Sắp xếp lượt đặt sân mới nhất (Mã #DS lớn nhất) lên đầu bảng
    List<LuotDatSan> findAllByOrderByMaLuotDatDesc();
}