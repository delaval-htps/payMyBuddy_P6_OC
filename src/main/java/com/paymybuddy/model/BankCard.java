package com.paymybuddy.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class that represents a bnak card's of connected user.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankCard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String cardNumber;

    @Column
    private int cardCode;

    @Column
    private String expirationDate;

    @OneToOne(mappedBy = "bankCard")
    private User user;
}
