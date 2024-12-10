import {configureStore} from "@reduxjs/toolkit";
import productStore from "./ProductStores/productStore.js";
import SearchProducts from "./ProductStores/SearchProducts.js";
import CategoriesReducer from "./CatetegoriesStore/CategoriesReducer.js";
import newProductReducer from "./ProductStores/newProductReducer.js";
import CartReducer from "./UserCartStore/CartReducer.js";


export const store = configureStore({
    reducer:{
        productReducer:productStore,
        newProductReducer:newProductReducer,
        CategoriesReducer:CategoriesReducer,
        SearchProducts:SearchProducts,
        CartReducer:CartReducer,
    }
})