package me.ikno.documentapi.repositories;

import me.ikno.documentapi.models.DirectoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectoryRepository extends JpaRepository<DirectoryModel, String> {
    List<DirectoryModel> findByOwnerId(int ownerId);

    DirectoryModel findByIdAndOwnerId(String id, int ownerId);

    // Find subdirectories of a directory (but not the directory itself)
    List<DirectoryModel> findByParentIdAndOwnerIdAndIdNot(String parentId, int ownerId, String id);

    // Recursive join to get all parent directories of a directory
    @Query(value = """
        WITH RECURSIVE directory_hierarchy AS (
            SELECT 
                d.directory_id,
                d.display_name,
                d.parent_directory_id,
                d.owner_id
            FROM directory d
            WHERE d.directory_id = :startingDirectoryId
            UNION ALL
            SELECT 
                d.directory_id,
                d.display_name,
                d.parent_directory_id,
                d.owner_id
            FROM directory d
            INNER JOIN directory_hierarchy dh 
                ON d.directory_id = dh.parent_directory_id
            WHERE d.directory_id != dh.directory_id
        )
        SELECT * FROM directory_hierarchy;
    """, nativeQuery = true)
    List<DirectoryModel> findDirectoryHierarchy(@Param("startingDirectoryId") String startingDirectoryId);
}
