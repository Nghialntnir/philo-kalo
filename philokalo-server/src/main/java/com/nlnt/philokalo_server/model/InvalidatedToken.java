package com.nlnt.philokalo_server.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author nghia
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invalidated_token")
public class InvalidatedToken {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "expiry_time")
    private Date expiryTime;
}
