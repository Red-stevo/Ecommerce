import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";
import {updateProfileImage} from "../UserProfileStore/UserProfileReducer.js";

const updateProduct = createEntityAdapter();

const  initialState = updateProduct.getInitialState({
    success:null, loading:false, errorMessage:null, product:{}});

export const getUpdateProducts = createAsyncThunk(
    "update-product/get-product",
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
                    state.products = action.payload;
                })
                .addCase(getUpdateProducts.rejected, (state, action) => {
                    state.success = null;
                    state.loading = false;
                    state.errorMessage = action.payload ? action.payload : "Error Getting Update Products.";
                })
        }
    }
);

export default ProductUpdateReducer.reducer;


