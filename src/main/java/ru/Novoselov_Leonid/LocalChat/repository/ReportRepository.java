package ru.Novoselov_Leonid.LocalChat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.Novoselov_Leonid.LocalChat.entity.Report;

@Repository
public interface ReportRepository extends CrudRepository<Report, Long> {
}
