package com.kym.dto;

import org.springframework.web.multipart.MultipartFile;

public record StatementLoadRequestDTO(MultipartFile uploadedStatement,
                                      String accountName,
                                      String accountNumber,
                                      String statementType,
                                      String bankCode,
                                      String originalFileName) {
}
