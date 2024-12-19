import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

const updateProduct = createEntityAdapter();

const  initialState = updateProduct.getInitialState({
    success:null, loading:false, errorMessage:null, product:{}});

export const getUpdateProducts = createAsyncThunk(
    "update-product/get",
    async (specificProductId= null,
           {fulfillWithValue,
               rejectWithValue
           }) => {

        /*Axios request to save products.*/
        try {
            return fulfillWithValue(
                (await RequestsConfig.get(`/admin/products/get-specific-inventory-product/${specificProductId}`)).data);
        }catch (e){
            return rejectWithValue(e.response.data.message ? e.response.data.message : e.response.data);
        }
    }
);

export const updateProducts = createAsyncThunk(
    "update-product/update",
    async (data= null,
           {fulfillWithValue,
               rejectWithValue,
               dispatch
           }) => {

        const {productId, formData} = data;
        /*Axios request to save products.*/
        try {
            await RequestsConfig.put(`/admin/products/update-product`, formData, {headers:{
                'Content-Type':'multipart/form-data'}});

            await dispatch(getUpdateProducts(productId));
            return fulfillWithValue(true);
        }catch (e){
            return rejectWithValue(e.response.data.message ? e.response.data.message : e.response.data);
        }
    }
);


export const deleteImage = createAsyncThunk(
    "update-product/delete",
    async (imageUrl= null,
           {fulfillWithValue,
               rejectWithValue,
               dispatch
           }) => {


        /*Axios request to save products.*/
        try {
            await RequestsConfig.delete(`/admin/products/delete-product-image?image=${imageUrl}`);
            return fulfillWithValue(true);
        }catch (e){
            return rejectWithValue(e.response.data.message ? e.response.data.message : e.response.data);
        }
    }
);



const ProductUpdateReducer = createSlice(
    {
        name:"update-product",
        initialState,
        reducers:{},
        extraReducers: builder => {
            builder
                .addCase(getUpdateProducts.pending, (state) => {
                    state.loading = true;
                    state.errorMessage = null;
                    state.success = null;
                })
                .addCase(getUpdateProducts.fulfilled, (state, action) => {
                    state.success = true;
                    state.loading = false;
                    state.errorMessage = null;
                    state.product = action.payload;
                })
                .addCase(getUpdateProducts.rejected, (state, action) => {
                    state.success = null;
                    state.loading = false;
                    state.errorMessage = action.payload ? action.payload : "Error Getting Update Products.";
                })
                .addCase(updateProducts.pending, (state) => {
                    state.loading = true;
                    state.errorMessage = null;
                    state.success = null;
                })
                .addCase(updateProducts.fulfilled, (state, action) => {
                    state.success = action.payload;
                    state.loading = false;
                    state.errorMessage = null;
                })
                .addCase(updateProducts.rejected, (state, action) => {
                    state.success = null;
                    state.loading = false;
                    state.errorMessage = action.payload ? action.payload : "Error  Updating Products.";
                })
                .addCase(deleteImage.pending, (state) => {
                    state.loading = true;
                    state.errorMessage = null;
                    state.success = null;
                })
                .addCase(deleteImage.fulfilled, (state, action) => {
                    state.success = action.payload;
                    state.loading = false;
                    state.errorMessage = null;
                })
                .addCase(deleteImage.rejected, (state, action) => {
                    state.success = null;
                    state.loading = false;
                    state.errorMessage = action.payload ? action.payload : "Error  Updating Products.";
                })
        }
    }
);

export default ProductUpdateReducer.reducer;
