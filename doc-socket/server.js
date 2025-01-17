import 'dotenv/config';
import {WebSocket, WebSocketServer} from "ws";
import { setupWSConnection, getYDoc } from 'y-websocket/bin/utils';
import { v4 as uuid } from 'uuid';
import documentRepository from "./repository.js";
import debounce from "debounce";

const repo = new documentRepository(process.env.DB_URL, process.env.DB_USER, process.env.DB_PASS);

const wss = new WebSocketServer({ port: process.env.WS_PORT });

const documentConnections = new Map(); // Mapping of document ids to connected clients
const clientIdMap = new Map(); // Mapping of WebSocket connections to client IDs
const documentYdocs = new Map(); // Mapping of document ids to Yjs documents

wss.on('connection', (ws, req) => {
    const docId = req.url.slice(1);
    
    if (!documentConnections.has(docId)) {
        documentConnections.set(docId, new Set());
    }
    
    const clients = documentConnections.get(docId);
    clients.add(ws);
    
    const clientId = uuid();
    clientIdMap.set(ws, clientId);
    
    const ydoc = getOrCreateYdoc(docId);
    
    setupWSConnection(ws, req, { docName: docId });
    
    const saveDocument = () => {
        const ytext = ydoc.getText('content');
        const content = ytext.toString();
        
        repo.saveDocument(docId, content).catch((err) => {
            console.error('Error saving document:', err);
        });
    }
    
    const debounceSaveDocument = debounce(saveDocument, 1000);
    
    ws.on('message', (message) => {
        for (const client of clients) {
            if (client !== ws && client.readyState === WebSocket.OPEN) {
                client.send(message);
            }
        }
        
        debounceSaveDocument();
    });
    
    ws.on('close', () => {
        handleClientDisconnect(); // Disconnected
    });
    
    ws.on('error', () => {
        handleClientDisconnect(); // Unexpectedly disconnected
    });
    
    const handleClientDisconnect = () => {
        clients.delete(ws);
        clientIdMap.delete(ws);
        
        if (clients.size === 0) {
            // No more clients connected to this document
            console.log(`No more clients connected to document: ${docId}`);
            
            // Save document content to database
            const ytext = ydoc.getText('content');
            const content = ytext.toString();
            
            repo.saveDocument(docId, content).catch((err) => {
                console.error('Error saving document:', err);
            });
            
            // Cleanup
            documentConnections.delete(docId);
            documentYdocs.delete(docId);
        }
    }
});

const getOrCreateYdoc = (docId) => {
    if (!documentYdocs.has(docId)) {
        const ydoc = getYDoc(docId, true);
        const ytext = ydoc.getText('content');
        
        // Load document content from database
        repo.getDocument(docId).then((doc) => {
            // Async so client can complete the handshake and load the page before the document is done loading
            
            ydoc.transact(() => {
                ytext.delete(0, ytext.length);
                ytext.insert(0, doc.content);
            })
        });
        
        documentYdocs.set(docId, ydoc);
    }
    return documentYdocs.get(docId);
}

console.log(`WebSocket server is running on ws://localhost:${process.env.WS_PORT}`);