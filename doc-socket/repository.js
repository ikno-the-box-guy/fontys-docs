import mysql from 'mysql2';

class documentRepository {
    constructor(host, username, password) {
        this.host = host;
        this.username = username;
        this.password = password;
        
        this.pool = mysql.createPool({
            host: this.host,
            user: this.username,
            password: this.password,
            database: 'document_schema',
            waitForConnections: true,
            connectionLimit: 10,
            queueLimit: 0
        });
    }

    async getDocument(id) {
        return new Promise((resolve, reject) => {
            this.pool.query('SELECT * FROM document WHERE document_id = ?', [id], (err, results) => {
                if (err) {
                    reject(err);
                } else {
                    resolve(results[0]);
                }
            });
        });
    }
    
    async saveDocument(id, content) {
        return new Promise((resolve, reject) => {
            this.pool.query('UPDATE document SET content = ? WHERE document_id = ?', [content, id], (err, results) => {
                if (err) {
                    reject(err);
                } else {
                    resolve(results);
                }
            });
        });
    }
}

export default documentRepository;