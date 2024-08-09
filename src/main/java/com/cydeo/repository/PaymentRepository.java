package com.cydeo.repository;

import com.cydeo.entity.Payment;
import com.cydeo.enums.Months;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAllByYearAndCompanyId(int year, Long companyId);

    Payment findByMonth(Months month);
}
