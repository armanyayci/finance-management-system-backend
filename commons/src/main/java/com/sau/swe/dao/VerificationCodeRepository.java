package com.sau.swe.dao;

import com.sau.swe.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    
    @Query("SELECT vc FROM VerificationCode vc WHERE vc.userId = :userId AND vc.code = :code AND vc.isUsed = false AND vc.verificationType = :type")
    Optional<VerificationCode> findByUserIdAndCodeAndNotUsedAndType(@Param("userId") Long userId, 
                                                                   @Param("code") String code, 
                                                                   @Param("type") VerificationCode.VerificationType type);
    
    @Query("SELECT vc FROM VerificationCode vc WHERE vc.userId = :userId AND vc.verificationType = :type AND vc.isUsed = false ORDER BY vc.createdAt DESC")
    Optional<VerificationCode> findLatestByUserIdAndType(@Param("userId") Long userId, 
                                                        @Param("type") VerificationCode.VerificationType type);
} 