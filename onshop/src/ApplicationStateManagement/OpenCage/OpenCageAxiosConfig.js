import axios from "axios";

export const openCageAxiosConfig = axios.create({
    baseURL:"https://api.opencagedata.com/geocode/v1",
    withCredentials:true,
    params:{key:"e741cfd4f40f4bfc83aae92c687dc9d9", no_annotations: 1}
});