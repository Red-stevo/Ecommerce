import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

const productStoreAdapter = createEntityAdapter();

export const getProductDetails = createAsyncThunk("product/get-product",
    async (productId = null, {fulfillWithValue,rejectWithValue}) => {
        try {
            return fulfillWithValue((await RequestsConfig.get(`open/products/get/${productId}`)).data);
        }catch (error){
            return rejectWithValue(error.response ? error.response.data : error.data);
        }
    });

const initialState = productStoreAdapter.getInitialState({
    product:{},
    errorMessage:null,
    loading:false,
    success:null
});


const productStore = createSlice({
    name:"product",
    initialState,
    reducers:{},
    extraReducers:builder => {
     builder
         .addCase(getProductDetails.pending, (state) => {
            state.loading = true;
            state.errorMessage = null;
            state.success = null;
        })
         .addCase(getProductDetails.fulfilled, (state, action) => {
             state.product = action.payload;
             state.errorMessage = null;
             state.success = true;
             state.loading = false;
         })
         .addCase(getProductDetails.rejected, (state, action) => {
             state.errorMessage = action.payload;
             state.loading = false;
             state.success = null;
         })
    }
});


export default productStore.reducer;
