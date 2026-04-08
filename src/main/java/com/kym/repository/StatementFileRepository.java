package com.kym.repository;

import com.kym.entity.StatementFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatementFileRepository extends JpaRepository<StatementFile, Long> {

}
