import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

const cartAdapter = createEntityAdapter();

const initialState = cartAdapter.getInitialState(
    {
        errorMessage:"",
        loading:"",
        success:"",
        CartResponse:{username:"", cartId:"", cartItemsResponses:[], youMayLikes:[], currentPage:0, totalPages:0,
            hasMore:true}
    });

export const addToCart = createAsyncThunk("cart/addToCart",
    async (productData = null,
           {fulfillWithValue
        ,rejectWithValue}) => {
    try {
        await RequestsConfig.post("/customer/cart/add-to-cart", productData,
            {headers:{'Content-Type':'application/json'}});
        fulfillWithValue(true);
    }catch (error){
        return rejectWithValue(error.response ? error.response.data : error.data);
    }
});

export const getCartItems = createAsyncThunk("cart/getCart",
    async (productData = null, {fulfillWithValue,rejectWithValue}) => {

        const {page, size, userId} = productData;

        try {
            fulfillWithValue((await RequestsConfig.post(`/customer/cart/${userId}?page=${page}&size=${size}`)).data);
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
                state.CartResponse.username=action.payload.username;
                state.CartResponse.cartId=action.payload.cartId;
                state.CartResponse.cartItemsResponses=action.payload.cartItemsResponses;
                state.CartResponse.youMayLikes=[...state.CartResponse.youMayLikes,...action.payload.youMayLikes];
                state.CartResponse.currentPage=action.payload.currentPage;
                state.CartResponse.totalPages=action.payload.totalPages;
                state.CartResponse.hasMore=action.payload.hasMore;
            })
            .addCase(getCartItems.rejected, (state, action) => {
                state.errorMessage = action.payload;
                state.loading = false;
                state.success = null;
            })
});

export  default  CartReducer.reducer;



