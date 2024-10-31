package me.ikno.documentapi.controllers;

import me.ikno.documentapi.models.DocumentModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class DocumentController {
    public ResponseEntity<DocumentModel> getDocuments() {
        return null;
    }
}
