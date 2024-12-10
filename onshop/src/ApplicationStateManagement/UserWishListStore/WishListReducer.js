import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

const wishListAdapter = createEntityAdapter();

const initialState = wishListAdapter.getInitialState({errorMessage:"", loading:"", success:"",});

export const addToWishList = createAsyncThunk("wishList/addToCart",
    async (productData = null, {fulfillWithValue,rejectWithValue}) => {

        const {userId, specificProductId} = productData;
        try {
            await RequestsConfig.post(`/customer/products/add-to-wishlist?specificProductId=${specificProductId}&userId=${userId}`);
            fulfillWithValue(true);
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
});

export  default  WishListReducer.reducer;



