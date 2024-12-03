import axios from "axios";

export const RequestsConfig = axios.create({
    baseURL:"http://192.168.43.63:8080/api/v1",
    withCredentials:true,
    headers: {
        'Content-Type': 'multipart/form-data',
    }
})