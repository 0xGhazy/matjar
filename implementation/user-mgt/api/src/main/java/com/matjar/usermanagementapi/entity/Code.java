package com.matjar.usermanagementapi.entity;

import com.matjar.usermanagementapi.enums.CodeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "code", schema = "user_management")
@Entity
public class Code {

    @Id
    private String id;
    private String code;
    private String email;
    @Enumerated(EnumType.STRING)
    private CodeStatus status;
    @Column(name = "valid_from")
    private LocalDateTime validFrom;
    @Column(name = "valid_to")
    private LocalDateTime validTo;
    @Column(name = "usage_time")
    private LocalDateTime usageTime;

    @Override
    public String toString() {
        return "Code{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", validFrom=" + validFrom +
                ", validTo=" + validTo +
                '}';
    }

}
