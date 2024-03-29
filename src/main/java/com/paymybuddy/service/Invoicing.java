package com.paymybuddy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.paymybuddy.model.Invoice;

/**
 * Interface for invoicing.
 */
@Service
public interface Invoicing {
    public void sendInvoices(List<Invoice> invoices);
}
