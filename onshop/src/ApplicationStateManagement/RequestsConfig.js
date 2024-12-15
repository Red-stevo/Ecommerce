import axios from "axios";

export const RequestsConfig = axios.create({
    baseURL:"http://192.168.100.26:8080/api/v1",
    withCredentials:true
});

