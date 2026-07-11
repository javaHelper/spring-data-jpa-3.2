package com.example.service.repository;

import com.example.service.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.Year;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Find audit logs by entity type
    @Query("SELECT a FROM AuditLog a WHERE a.entityType = :entityType ORDER BY a.changeTimestamp DESC")
    List<AuditLog> findByEntityType(@Param("entityType") String entityType);

    // Find audit logs after a specific Instant
    @Query("SELECT a FROM AuditLog a WHERE a.changeTimestamp > :afterTime ORDER BY a.changeTimestamp DESC")
    List<AuditLog> findAuditLogsAfter(@Param("afterTime") Instant afterTime);

    // Find audit logs between two Instants
    @Query("SELECT a FROM AuditLog a WHERE a.changeTimestamp BETWEEN :startTime AND :endTime ORDER BY a.changeTimestamp DESC")
    List<AuditLog> findAuditLogsBetween(@Param("startTime") Instant startTime, @Param("endTime") Instant endTime);

    // Find audit logs for specific year
    @Query("SELECT a FROM AuditLog a WHERE a.auditYear = :year ORDER BY a.changeTimestamp DESC")
    List<AuditLog> findByAuditYear(@Param("year") Year year);

    // Find audit logs by action
    @Query("SELECT a FROM AuditLog a WHERE a.action = :action ORDER BY a.changeTimestamp DESC")
    List<AuditLog> findByAction(@Param("action") String action);

    // Find audit logs by entity and action
    @Query("SELECT a FROM AuditLog a WHERE a.entityType = :entityType AND a.action = :action ORDER BY a.changeTimestamp DESC")
    List<AuditLog> findByEntityTypeAndAction(@Param("entityType") String entityType, @Param("action") String action);

    // Find audit logs changed by specific user
    @Query("SELECT a FROM AuditLog a WHERE a.changedBy = :changedBy ORDER BY a.changeTimestamp DESC")
    List<AuditLog> findByChangedBy(@Param("changedBy") String changedBy);
}