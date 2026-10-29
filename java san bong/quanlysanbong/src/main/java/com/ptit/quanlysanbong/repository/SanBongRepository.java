package com.ptit.quanlysanbong.repository;

import com.ptit.quanlysanbong.entity.SanBong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SanBongRepository extends JpaRepository<SanBong, String> {
}