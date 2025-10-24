// src/environments/environment.ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8888' // Gateway URL
};

// src/environments/environment.prod.ts
export const environmentProd = {
  production: true,
  apiUrl: 'https://your-production-gateway-url.com'
};
