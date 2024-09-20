package me.ikno.documentapi.controllers;

import io.jsonwebtoken.Claims;
import me.ikno.documentapi.dtos.CreateDirectoryDTO;
import me.ikno.documentapi.dtos.UpdateDirectoryDTO;
import me.ikno.documentapi.models.DirectoryModel;
import me.ikno.documentapi.services.DirectoryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Basic directory crud operations

@RestController
@RequestMapping("api/v1")
public class DirectoryController {
    private final DirectoryService directoryService;

    public DirectoryController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @GetMapping("/directories")
    public ResponseEntity<List<DirectoryModel>> getDirectories() {
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        int userId = Integer.parseInt(claims.get("jti", String.class));

        List<DirectoryModel> directories = directoryService.getDirectories(userId);

        return ResponseEntity.ok(directories);
    }

    @GetMapping(value = "/directories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectoryModel> getDirectoryById(@PathVariable Integer id) {
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        int userId = Integer.parseInt(claims.get("jti", String.class));

        DirectoryModel directory = directoryService.getDirectoryById(id, userId);
        if(directory == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(directory);
    }

    @PutMapping(value = "/directories/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectoryModel> updateDirectory(@PathVariable Integer id, @RequestBody UpdateDirectoryDTO updateDirectoryBody) {
//        return directoryService.updateDirectory(directoryModel);
//        return ResponseEntity.ok(directoryBody);


        return null;
    }

    @PostMapping(value = "/directories", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createDirectory(@RequestBody CreateDirectoryDTO createDirectoryBody) {
        String name = createDirectoryBody.getDisplayName();
        int parentId = createDirectoryBody.getParentId();

        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        int ownerId = Integer.parseInt(claims.get("jti", String.class));

        int directoryId = directoryService.createDirectory(name, parentId, ownerId);

        if(directoryId == -1) { // Invalid parent directory
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok("{\"directoryId\": " + directoryId + "}");
    }
}
