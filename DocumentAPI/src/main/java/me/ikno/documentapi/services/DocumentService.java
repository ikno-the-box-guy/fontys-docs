package me.ikno.documentapi.services;

import me.ikno.documentapi.components.AuthenticationUtil;
import me.ikno.documentapi.dtos.CreateDocumentDTO;
import me.ikno.documentapi.exceptions.DuplicateDocumentException;
import me.ikno.documentapi.models.DirectoryModel;
import me.ikno.documentapi.models.DocumentModel;
import me.ikno.documentapi.repositories.DirectoryRepository;
import me.ikno.documentapi.repositories.DocumentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static me.ikno.documentapi.utils.IdUtils.compressUuid;

@Service
public class DocumentService {
    private final AuthenticationUtil authenticationUtil;
    private final DocumentRepository documentRepository;
    private final DirectoryRepository directoryRepository;

    public DocumentService(AuthenticationUtil authenticationUtil, DocumentRepository documentRepository, DirectoryRepository directoryRepository) {
        this.authenticationUtil = authenticationUtil;
        this.documentRepository = documentRepository;
        this.directoryRepository = directoryRepository;
    }

    public DocumentModel getDocumentById(String id) {
        // Get user id from auth token
        int userId = authenticationUtil.getUserId();

        // Get document by id
        return documentRepository.findByIdAndOwnerId(id, userId);
    }

    public DocumentModel createDocument(CreateDocumentDTO createDocumentDTO) {
        if(createDocumentDTO.getDisplayName().isBlank()){
            throw new IllegalArgumentException("Display name cannot be blank");
        }

        // Get user id from auth token
        int userId = authenticationUtil.getUserId();

        DirectoryModel parentDirectory = directoryRepository.findByIdAndOwnerId(createDocumentDTO.getParentId(), userId);

        if(parentDirectory == null) {
            throw new IllegalArgumentException("Parent directory not found");
        }

        DocumentModel existingDocument = documentRepository.findByDisplayNameAndParentId(createDocumentDTO.getDisplayName(), createDocumentDTO.getParentId());
        if(existingDocument != null) {
            throw new DuplicateDocumentException("Document with same name already exists in parent directory");
        }

        // Create document
        DocumentModel document = new DocumentModel();
        String documentId = compressUuid(UUID.randomUUID().toString());

        document.setId(documentId);
        document.setDisplayName(createDocumentDTO.getDisplayName());
        document.setContent("# " + createDocumentDTO.getDisplayName());
        document.setParentId(createDocumentDTO.getParentId());
        document.setOwnerId(userId);
        document.setDateCreated(Instant.now());
        document.setDateChanged(Instant.now());

        return documentRepository.save(document);
    }
}
