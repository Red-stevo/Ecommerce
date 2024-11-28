import {configureStore} from "@reduxjs/toolkit";
import productStore from "./ProductStores/productStore.js";
import {newProductReducer} from "./ProductStores/AddProductStore.js";


export const store = configureStore({
    reducer:{
        productReducer:productStore,
        newProductReducer:newProductReducer.reducer,
    }
})