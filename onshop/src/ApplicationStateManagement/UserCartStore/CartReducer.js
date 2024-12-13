import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";
import data from "bootstrap/js/src/dom/data.js";
import {configs} from "@eslint/js";

const cartAdapter = createEntityAdapter();

const initialState = cartAdapter.getInitialState(
    {errorMessage:null, loading:false, success:null,
        CartResponse:{username:"", cartId:"", totalPrice:0.00,cartItemsResponses:[], youMayLikes:[], currentPage:0, totalPages:0, hasMore:true}});

export const addToCart = createAsyncThunk("cart/addToCart",
    async (productData = null, {rejectWithValue,fulfillWithValue }) => {
    try {
        await RequestsConfig.post("/customer/cart/add-to-cart", productData,
            {headers:{'Content-Type':'application/json'}});
        return fulfillWithValue(true);
    }catch (error){
        return rejectWithValue(error.response ? error.response.data : error.data);
    }
});

export const getCartItems = createAsyncThunk("cart/getCart",
    async (productData = null, {rejectWithValue,
        fulfillWithValue }) => {

        const {page, size, userId} = productData;

        try {
            return fulfillWithValue((await RequestsConfig.get(`/customer/cart/${userId}?page=${page}&size=${size}`)).data);
        }catch (error){
            return rejectWithValue(error.response ? error.response.data : error.data);
        }
    });

export const deleteCartItem = createAsyncThunk("cart/deleteItems",
    async (itemIds = [], {fulfillWithValue,rejectWithValue}) => {

        try {
            await RequestsConfig.put(`/customer/cart/remove-item`,itemIds)
            return fulfillWithValue(true);
        }catch (error){
            return rejectWithValue(error.response ? error.response.data : error.data);
        }
    });



const CartReducer = createSlice({
    name:"cart",
    initialState,
    reducers:{},
    extraReducers: builder =>
        builder
            .addCase(addToCart.pending, (state) => {
                state.success = null;
                state.errorMessage = null;
                state.loading = true;
            })
            .addCase(addToCart.fulfilled, (state, action) => {
                state.loading = false;
                state.errorMessage = null;
                state.success = action.payload;
            })
            .addCase(addToCart.rejected, (state, action) => {
                state.errorMessage = action.payload;
                state.loading = false;
                state.success = null;
            })
            .addCase(getCartItems.pending, (state) => {
                state.success = null;
                state.errorMessage = null;
                state.loading = true;
            })
            .addCase(getCartItems.fulfilled, (state, action) => {
                state.loading = false;
                state.errorMessage = null;
                state.success = true;
                state.CartResponse = action.payload;
            })
            .addCase(getCartItems.rejected, (state, action) => {
                state.errorMessage = action.payload;
                state.loading = false;
                state.success = null;
            })
            .addCase(deleteCartItem.pending, (state) => {
                state.success = null;
                state.errorMessage = null;
                state.loading = true;
            })
            .addCase(deleteCartItem.fulfilled, (state, action) => {
                state.loading = false;
                state.errorMessage = null;
                state.success = action.payload;
            })
            .addCase(deleteCartItem.rejected, (state, action) => {
                state.errorMessage = action.payload;
                state.loading = false;
                state.success = null;
            })
});

export  default  CartReducer.reducer;



