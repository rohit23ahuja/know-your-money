package com.kym.repository;

import com.kym.entity.StatementFile;
import com.kym.projection.StatementYearMonthView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StatementFileRepository extends JpaRepository<StatementFile, Long> {

    List<StatementFile> findByStatementYearMonthAndStatementType(LocalDate statementYearMonth, String statementType);

    @Query("select f.id AS id, f.statementYearMonth AS statementYearMonth from StatementFile f order by f.statementYearMonth desc")
    List<StatementYearMonthView> findAllStatementYearMonthsDesc();

}
