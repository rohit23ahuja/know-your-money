package com.kym.repository;

import com.kym.entity.StatementCell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatementCellRepository extends JpaRepository<StatementCell, Long> {

    @Query("""
            select c from StatementCell c
            where c.rowIndex between :dataStartRowIndex and :dataEndRowIndex
            and c.statementFileId = :statementFileId
            order by c.rowIndex, c.colIndex
            """)
    List<StatementCell> findStatementCellsInRowRange(@Param("statementFileId") Long statementFileId,
                                                     @Param("dataStartRowIndex") Integer dataStartRowIndex,
                                                     @Param("dataEndRowIndex") Integer dataEndRowIndex);

    List<StatementCell> findByStatementFileId(Long statementFileId);
}
