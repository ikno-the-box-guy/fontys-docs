import axios from "axios";
import { router } from "../router.ts";

export const documentApi = axios.create({
    baseURL: import.meta.env.VITE_DOC_API_URL,
    withCredentials: true,
});

export const authApi = axios.create({
    baseURL: import.meta.env.VITE_AUTH_API_URL,
    withCredentials: true,
});

documentApi.interceptors.response.use((response) => {
    return response;
}, (error) => {
    // TODO: Check if it isn't just a role problem or something
    if (error.response.status === 401 || error.response.status === 403) {
        router.push("/login").then(r => r);
    }

    return error;
});