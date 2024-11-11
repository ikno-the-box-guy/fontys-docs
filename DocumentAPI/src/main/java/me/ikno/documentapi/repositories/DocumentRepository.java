package me.ikno.documentapi.repositories;

import me.ikno.documentapi.models.DocumentModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<DocumentModel, String> {
    DocumentModel findByIdAndOwnerId(String id, int ownerId);
    List<DocumentModel> findByParentIdAndOwnerId(String parentId, int ownerId);
    DocumentModel findByDisplayNameAndParentId(String displayName, String parentId);
}
