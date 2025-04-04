package com.Kernighan.TerminKalender.repository;

import com.Kernighan.TerminKalender.model.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public
interface SystemLogRepository extends JpaRepository<SystemLog, Long> {
    List<SystemLog> findAllByOrderByTimestampDesc();
}