package ru.Novoselov_Leonid.LocalChat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.Novoselov_Leonid.LocalChat.entity.File;

import java.util.List;

@RepositoryRestResource(path = "files")
public interface FileRepository extends CrudRepository<File, Long> {
    List<File> findByMessageId(Long messageId);
}
