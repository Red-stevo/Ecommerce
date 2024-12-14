import axios from "axios";

const openCageAxiosConfig = axios.create({
    baseURL:"https://api.opencagedata.com/geocode/v1/json",
    withCredentials:true
})