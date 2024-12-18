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
import InventoryReducer from "./InventoryStore/InventoryReducer.js";
import PaymentReducer from "./PaymentStore/PaymentReducer.js";
import OrderInfoReducer from "./OrderInfoStore/OrderInfoReducer.js";
import ProductUpdateReducer from "./ProductUpdateStore/ProductUpdateReducer.js";


export const store = configureStore({
    reducer:{
        productReducer:productStore, newProductReducer, CategoriesReducer,
        SearchProducts, CartReducer, WishListReducer, OrderStatusReducer,
        openCageReducer, UserProfileReducer, ordersStore, InventoryReducer,
        PaymentReducer, OrderInfoReducer, ProductUpdateReducer
    }
})