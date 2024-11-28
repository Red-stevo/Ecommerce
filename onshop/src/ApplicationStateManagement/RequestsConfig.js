import axios from "axios";

export const RequestsConfig = axios.create({
    baseURL:"http://localhost:8080/api/v1",
    withCredentials:true,
})