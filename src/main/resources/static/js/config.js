// config.js
window.APP_CONFIG = {
    getApiUrl: function() {
        const hostname = window.location.hostname;

        if (hostname === 'localhost' || hostname === '127.0.0.1') {
            return 'http://localhost:8080';
        }

        if (hostname.includes('admin.ssak3.store')) {
            return 'https://admin.ssak3.store';
        }

        return 'https://ssak3.store';
    }
};