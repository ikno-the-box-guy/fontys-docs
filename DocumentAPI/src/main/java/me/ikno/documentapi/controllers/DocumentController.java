package me.ikno.documentapi.controllers;

import me.ikno.documentapi.dtos.CreateDocumentDTO;
import me.ikno.documentapi.models.DocumentModel;
import me.ikno.documentapi.services.DocumentService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<DocumentModel> getDocumentById(String id) {
        DocumentModel document = documentService.getDocumentById(id);

        if (document == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(document);
    }

    @PostMapping("/documents")
    public ResponseEntity<DocumentModel> createDocument(@RequestBody CreateDocumentDTO createDocumentDTO) {
        try {
            DocumentModel document = documentService.createDocument(createDocumentDTO);
            return ResponseEntity.ok(document);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/documents/{id}/download")
    public ResponseEntity<?> downloadDocument(String id) {
        throw new NotImplementedException("Not implemented");
    }

}
