package me.ikno.documentapi.repositories;

import me.ikno.documentapi.models.DirectoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectoryRepository extends JpaRepository<DirectoryModel, Integer>{
    List<DirectoryModel> findByOwnerId(int ownerId);
}
