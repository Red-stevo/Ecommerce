import {configureStore} from "@reduxjs/toolkit";
import productStore from "./ProductStores/productStore.js";
import SearchProducts from "./ProductStores/SearchProducts.js";
import CategoriesReducer from "./CategoriesStore/CategoriesReducer.js";
import newProductReducer from "./ProductStores/newProductReducer.js";
import CartReducer from "./UserCartStore/CartReducer.js";
import WishListReducer from "./UserWishListStore/WishListReducer.js";
import OrderStatusReducer from "./OrderStatusStore/OrderStatusReducer.js";
import openCageReducer from "./OpenCage/openCageReducer.js";
import UserProfileReducer from "./UserProfileStore/UserProfileReducer.js";
import ordersStore from "./OdersStore/ordersStore.js";


export const store = configureStore({
    reducer:{
        productReducer:productStore,
        newProductReducer:newProductReducer,
        CategoriesReducer:CategoriesReducer,
        SearchProducts:SearchProducts,
        CartReducer:CartReducer,
        WishListReducer:WishListReducer,
        OrderStatusReducer:OrderStatusReducer,
        openCageReducer:openCageReducer,
        UserProfileReducer:UserProfileReducer,
        ordersStore:ordersStore
    }
})