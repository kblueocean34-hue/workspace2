package com.samsung.mes.security;

import com.samsung.mes.member.dto.SalesOrderRequest;

import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;

public class RequestValidator {

    public static void validate(SalesOrderRequest req) {
        if (req == null) throw new IllegalArgumentException("요청 데이터가 없습니다");

        if (req.getOrderDate() == null)
            throw new IllegalArgumentException("수주일자는 필수입니다");

        if (isBlank(req.getCustomerCode()))
            throw new IllegalArgumentException("거래처코드는 필수입니다");

        if (isBlank(req.getCustomerName()))
            throw new IllegalArgumentException("거래처명은 필수입니다");

        if (isBlank(req.getItemCode()))
            throw new IllegalArgumentException("품목코드는 필수입니다");

        if (isBlank(req.getItemName()))
            throw new IllegalArgumentException("품목명은 필수입니다");

        if (req.getOrderQty() == null || req.getOrderQty() <= 0)
            throw new IllegalArgumentException("수주수량은 1 이상이어야 합니다");

        BigDecimal price = req.getPrice();
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("단가는 0보다 커야 합니다");
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}

