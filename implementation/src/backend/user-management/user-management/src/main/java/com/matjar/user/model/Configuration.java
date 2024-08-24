package com.matjar.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "configuration", schema = "user_management")
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {
    @Id
    @Column(name = "name") private String name;
    @Column(name = "value") private String value;
}
