package me.ikno.documentapi.controllers;

import me.ikno.documentapi.components.AuthenticationUtil;
import me.ikno.documentapi.dtos.CreateDirectoryDTO;
import me.ikno.documentapi.dtos.UpdateDirectoryDTO;
import me.ikno.documentapi.models.DirectoryModel;
import me.ikno.documentapi.services.DirectoryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<DirectoryModel> getDirectoryById(@PathVariable Integer id) {
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

    @PutMapping(value = "/directories/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectoryModel> updateDirectory(@PathVariable Integer id, @RequestBody UpdateDirectoryDTO updateDirectoryBody) {
//        return directoryService.updateDirectory(directoryModel);
//        return ResponseEntity.ok(directoryBody);

        // TODO: Implement this

        return null;
    }

    @PostMapping(value = "/directories", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createDirectory(@RequestBody CreateDirectoryDTO createDirectoryBody) {
        // Pull info from request body
        String name = createDirectoryBody.getDisplayName();
        int parentId = createDirectoryBody.getParentId();

        // Get user id from auth token
        int ownerId = authenticationUtil.getUserId();

        // Try to create directory
        int directoryId = directoryService.createDirectory(name, parentId, ownerId);

        // Check if directory was created
        if(directoryId == -1) { // Invalid parent directory
            return ResponseEntity.badRequest().build();
        }

        // Return directory id
        return ResponseEntity.ok("{\"directoryId\": " + directoryId + "}");
    }
}
