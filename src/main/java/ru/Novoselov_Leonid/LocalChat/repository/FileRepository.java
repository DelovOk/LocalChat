package ru.Novoselov_Leonid.LocalChat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.Novoselov_Leonid.LocalChat.entity.File;

import java.util.List;

public interface FileRepository extends CrudRepository<File, Long> {
    List<File> findByMessageId(Long messageId);
}
