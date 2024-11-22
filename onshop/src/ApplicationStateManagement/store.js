import {configureStore} from "@reduxjs/toolkit";
import productStore from "./productStore.js";

export const store = configureStore({
    reducer:{
        productReducer:productStore,
    }
})