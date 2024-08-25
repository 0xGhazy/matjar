package com.matjar.user.repository;

import com.matjar.user.model.ActivationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ActivationCodeRepository extends JpaRepository<ActivationCode, String> {

    @Query(name = "findActivationCodeByEmail",
            value = "SELECT * FROM user_management.activation_code WHERE email = :providedEmail AND :currentDate BETWEEN valid_from AND valid_to",
            nativeQuery = true)
    Optional<ActivationCode> findActivationCode(@Param("providedEmail") String providedEmail, @Param("currentDate") LocalDateTime currentDate);

    @Query(name = "updateActivationCodeStatusById",
            value = "UPDATE user_management.activation_code SET status = :status WHERE id = :id",
            nativeQuery = true)
    void updateActivationCodeStatusById(@Param("id") String id, @Param("status") String status);

}





