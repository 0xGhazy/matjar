package com.matjar.usermanagementapi.repository;

import com.matjar.usermanagementapi.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends JpaRepository<Code, String> {

    Code findCodeByEmail(String email);

    @Modifying(clearAutomatically = true)
    @Query(value ="DELETE FROM user_management.code " +
            "WHERE email = :email;",
            nativeQuery = true)
    int inactivateOldActivationCodes(@Param("email") String email);


}
