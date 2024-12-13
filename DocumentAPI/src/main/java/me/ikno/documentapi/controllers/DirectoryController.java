package me.ikno.documentapi.controllers;

import me.ikno.documentapi.components.AuthenticationUtil;
import me.ikno.documentapi.dtos.CreateDirectoryDTO;
import me.ikno.documentapi.dtos.UpdateDirectoryDTO;
import me.ikno.documentapi.models.DirectoryModel;
import me.ikno.documentapi.services.DirectoryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

// Basic directory crud operations

@RestController
@RequestMapping("api/v1")
public class DirectoryController {
    private final DirectoryService directoryService;
    private final AuthenticationUtil authenticationUtil;

    public DirectoryController(DirectoryService directoryService, AuthenticationUtil authenticationUtil) {
        this.directoryService = directoryService;
        this.authenticationUtil = authenticationUtil;
    }

    @GetMapping("/directories")
    public ResponseEntity<List<DirectoryModel>> getDirectories() {
        // Get user id from auth token
        int userId = authenticationUtil.getUserId();

        // Get directories for user
        List<DirectoryModel> directories = directoryService.getDirectories(userId);

        // Return directories
        return ResponseEntity.ok(directories);
    }

    @GetMapping(value = "/directories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectoryModel> getDirectoryById(@PathVariable String id) {
        // Get user id from auth token
        int userId = authenticationUtil.getUserId();

        // Get directory by id
        DirectoryModel directory = directoryService.getDirectoryById(id, userId);

        // Check if directory was found (also null if user is not owner)
        if(directory == null) {
            return ResponseEntity.notFound().build();
        }

        // Return directory
        return ResponseEntity.ok(directory);
    }

    @PostMapping(value = "/directories", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectoryModel> createDirectory(@RequestBody CreateDirectoryDTO createDirectoryBody) {
        // Pull info from request body
        String name = createDirectoryBody.getDisplayName();
        String parentId = createDirectoryBody.getParentId();

        // Get user id from auth token
        int ownerId = authenticationUtil.getUserId();

        // Try to create directory
        DirectoryModel directoryModel = directoryService.createDirectory(name, parentId, ownerId);

        // Check if directory was created
        if(directoryModel == null) { // Invalid parent directory
            return ResponseEntity.badRequest().build();
        }

        // Return directory id
        return ResponseEntity.ok(directoryModel);
    }

    @GetMapping(value = "/directories/{id}/hierarchy", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DirectoryModel>> getDirectoryHierarchy(@PathVariable String id) {
        // Get user id from auth token
        int userId = authenticationUtil.getUserId();

        // Get directory hierarchy
        List<DirectoryModel> directoryHierarchy = directoryService.getDirectoryHierarchy(id, userId);

        // Check if directory was found (also null if user is not owner)
        if(directoryHierarchy == null) {
            return ResponseEntity.notFound().build();
        }

        // Return directory hierarchy
        return ResponseEntity.ok(directoryHierarchy);
    }

    // For API access only
    @PostMapping(value = "/directories/root/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createRootDirectory(@PathVariable Integer id) {
        return ResponseEntity.ok(directoryService.createRootDirectory(id));
    }
}
