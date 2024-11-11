package me.ikno.documentapi.repositories;

import me.ikno.documentapi.models.DirectoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectoryRepository extends JpaRepository<DirectoryModel, String>{
    List<DirectoryModel> findByOwnerId(int ownerId);

    DirectoryModel findByIdAndOwnerId(String id, int ownerId);

    // Find subdirectories of a directory (but not the directory itself)
    List<DirectoryModel> findByParentIdAndOwnerIdAndIdNot(String parentId, int ownerId, String id);
}
