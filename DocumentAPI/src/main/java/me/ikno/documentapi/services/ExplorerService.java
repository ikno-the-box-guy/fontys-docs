package me.ikno.documentapi.services;

import me.ikno.documentapi.models.DirectoryModel;
import me.ikno.documentapi.models.DocumentModel;
import me.ikno.documentapi.repositories.DirectoryRepository;
import me.ikno.documentapi.repositories.DocumentRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ExplorerService {

    private final DirectoryRepository directoryRepository;
    private final DocumentRepository documentRepository;

    public ExplorerService(DirectoryRepository directoryRepository, DocumentRepository documentRepository) {
        this.directoryRepository = directoryRepository;
        this.documentRepository = documentRepository;
    }

    public List<DirectoryModel> getSubdirectories(String parentId, int ownerId) {
        return directoryRepository.findByParentIdAndOwnerIdAndIdNot(parentId, ownerId, parentId);
    }

    public List<DocumentModel> getDocuments(String directoryId, int ownerId) {
        return documentRepository.findByParentIdAndOwnerId(directoryId, ownerId);
    }
}
