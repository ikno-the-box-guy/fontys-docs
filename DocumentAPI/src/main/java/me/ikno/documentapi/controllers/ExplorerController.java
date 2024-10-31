package me.ikno.documentapi.controllers;

import me.ikno.documentapi.components.AuthenticationUtil;
import me.ikno.documentapi.models.DirectoryModel;
import me.ikno.documentapi.models.DocumentModel;
import me.ikno.documentapi.services.ExplorerService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1")
public class ExplorerController {
    private final AuthenticationUtil authenticationUtil;
    private final ExplorerService explorerService;

    public ExplorerController(AuthenticationUtil authenticationUtil, ExplorerService explorerService) {
        this.authenticationUtil = authenticationUtil;
        this.explorerService = explorerService;
    }

    @GetMapping(value = "/explorer/{directoryId}/directories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DirectoryModel>> getSubdirectories(@PathVariable String directoryId) {
        // Get user id from auth token
        int userId = authenticationUtil.getUserId();

        // Get subdirectories from parent directory
        List<DirectoryModel> directories = explorerService.getSubdirectories(directoryId, userId);

        // Return directories
        return ResponseEntity.ok(directories);
    }

    @GetMapping(value = "/explorer/{directoryId}/documents", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DocumentModel>> getDocuments(@PathVariable String directoryId) {
        // Get user id from auth token
        int userId = authenticationUtil.getUserId();

        // Get documents from directory
        List<DocumentModel> documents = explorerService.getDocuments(directoryId, userId);

        // Return documents
        return ResponseEntity.ok(documents);
    }
}
