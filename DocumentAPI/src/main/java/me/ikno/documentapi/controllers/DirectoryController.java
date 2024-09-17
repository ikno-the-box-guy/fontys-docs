package me.ikno.documentapi.controllers;

import me.ikno.documentapi.bodies.directory.CreateDirectoryBody;
import me.ikno.documentapi.bodies.directory.UpdateDirectoryBody;
import me.ikno.documentapi.models.DirectoryModel;
import me.ikno.documentapi.services.DirectoryService;
import me.ikno.documentapi.services.JwtService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Basic directory crud operations

@RestController
@RequestMapping("api/v1")
public class DirectoryController {
    private final DirectoryService directoryService;
    private final JwtService jwtService;

    public DirectoryController(DirectoryService directoryService, JwtService jwtService) {
        this.directoryService = directoryService;
        this.jwtService = jwtService;
    }

    @GetMapping("/directories")
    public ResponseEntity<List<DirectoryModel>> getDirectories() {
        return ResponseEntity.ok(directoryService.getDirectories());
    }

    @GetMapping(value = "/directories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectoryModel> getDirectoryById(@PathVariable Integer id) {
        return ResponseEntity.ok(directoryService.getDirectoryById(id));
    }

    @PutMapping(value = "/directories/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectoryModel> updateDirectory(@PathVariable Integer id, @PathVariable UpdateDirectoryBody updateDirectoryBody) {
//        return directoryService.updateDirectory(directoryModel);
//        return ResponseEntity.ok(directoryBody);

        return null;
    }

    @PostMapping(value = "/directories", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> createDirectory(@RequestBody CreateDirectoryBody createDirectoryBody, @RequestHeader(name = "Authorization") String auth) {
        String name = createDirectoryBody.getDisplayName();
        int parentId = createDirectoryBody.getParentId();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null) {
            return ResponseEntity.status(401).build();
        }

        if(authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }

        String token = auth.substring(7);
        String user = jwtService.extractEmail(token);

//        int directoryId = directoryService.createDirectory(name, parentId, ownerId);

        System.out.println(authentication);

        return ResponseEntity.ok(0);
    }
}
