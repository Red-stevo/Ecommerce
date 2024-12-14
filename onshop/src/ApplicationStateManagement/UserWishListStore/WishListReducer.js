import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

const wishListAdapter = createEntityAdapter();

const initialState = wishListAdapter
    .getInitialState({errorMessage:"", loading:"", success:"",wishListProducts:[]});

export const addToWishList = createAsyncThunk("wishlist/addToWishList",
    async (productData = null, {fulfillWithValue,rejectWithValue}) => {

        const {userId, specificationId} = productData;


        try {
            await RequestsConfig
                .post(`/customer/products/add-to-wishlist?specificProductId=${specificationId}&userId=${userId}`);
            return fulfillWithValue(true);
        } catch (error) {
            return rejectWithValue(error.response ? error.response.data : error.data);
        }
    });

export const deleteWishList = createAsyncThunk("wishList/deleteWishList",
    async (data = null, {fulfillWithValue,rejectWithValue}) => {
        const specificProductIds = data.specificProductId;

        try {
            await RequestsConfig.get(`/customer/products/delete-wishlist/${data.userId}`, specificProductIds);
            return fulfillWithValue(true);
        } catch (error) {
            return rejectWithValue(error.response ? error.response.data : error.data);
        }
    });


export const getWishList = createAsyncThunk("wishList/getWishList",
    async (userId = null, {fulfillWithValue,rejectWithValue}) => {
        try {
            return fulfillWithValue(await RequestsConfig.get(`/customer/products/show-wishlist/${userId}`));
        } catch (error) {
            return rejectWithValue(error.response ? error.response.data : error.data);
        }
    });


const WishListReducer = createSlice({
    name:"wishList",
    initialState,
    reducers:{},
    extraReducers: builder =>
        builder
            .addCase(addToWishList.pending, (state) => {
                state.success = null;
                state.errorMessage = null;
                state.loading = true;
            })
            .addCase(addToWishList.fulfilled, (state, action) => {
                state.loading = false;
                state.errorMessage = null;
                state.success = action.payload;
            })
            .addCase(addToWishList.rejected, (state, action) => {
                state.errorMessage = action.payload;
                state.loading = false;
                state.success = null;
            })
            .addCase(getWishList.pending, (state) => {
                state.success = null;
                state.errorMessage = null;
                state.loading = true;
            })
            .addCase(getWishList.fulfilled, (state, action) => {
                state.loading = false;
                state.errorMessage = null;
                state.success = true;
                state.wishListProducts = [...action.payload];
            })
            .addCase(getWishList.rejected, (state, action) => {
                state.errorMessage = action.payload;
                state.loading = false;
                state.success = null;
            })
            .addCase(deleteWishList.pending, (state) => {
                state.success = null;
                state.errorMessage = null;
                state.loading = true;
            })
            .addCase(deleteWishList.fulfilled, (state, action) => {
                state.loading = false;
                state.errorMessage = null;
                state.success = action.payload;
            })
            .addCase(deleteWishList.rejected, (state, action) => {
                state.errorMessage = action.payload;
                state.loading = false;
                state.success = null;
            })
});

export  default  WishListReducer.reducer;



