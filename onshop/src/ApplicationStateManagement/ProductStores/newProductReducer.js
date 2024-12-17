import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";
import {updateProfileImage} from "../UserProfileStore/UserProfileReducer.js";

const newProductAdapter = createEntityAdapter();

const  initialState = newProductAdapter.getInitialState({
    success:null,
    loading:false,
    errorMessage:null
});

export const postProduct = createAsyncThunk("new-product/create",
    async (productData= null,
     {
         fulfillWithValue,
         rejectWithValue
     }) => {

        /*Axios request to save products.*/
        try {

            const productCreationRequest = new FormData();

            // Append JSON data
            productCreationRequest.append('productData', JSON.stringify({
                productName: productData.productName,
                productDescription: productData.productDescription,
                categoryName: productData.categoryName,
                productCreatedDetails: productData.productCreatedDetails.map(detail => ({
                    color: detail.color,
                    size: detail.size,
                    productPrice: detail.productPrice,
                    discount: detail.discount,
                    count: detail.count
                }))
            }));

            // Append each file
            productData.productCreatedDetails.forEach(detail => {
                detail.productUrls.forEach(file => {
                    productCreationRequest.append('files', file);
                });
            });



            await RequestsConfig.post("/admin/products/post", productCreationRequest, {headers:{
                    'Content-Type': 'multipart/form-data',
                }});
            return fulfillWithValue(true);
        }catch (e){
            return rejectWithValue(e.response.data.message ? e.response.data.message : e.response.data);
        }
    }
);


export const deleteProduct = createAsyncThunk("new-product/delete-product",
    async (productId= null, {
               fulfillWithValue,
               rejectWithValue
           }) => {

        /*Axios request to save products.*/
        try {
            await RequestsConfig.delete(`/admin/products/delete-product?productId=${productId}`);
            return fulfillWithValue(true);
        }catch (e){
            return rejectWithValue(e.response.data.message ? e.response.data.message : e.response.data);
        }
    });



const newProductReducer = createSlice(
    {
        name:"new-product",
        initialState,
        reducers:{},
        extraReducers: builder => {
            builder
            .addCase(postProduct.pending, (state) => {
                state.loading = true;
            })
            .addCase(postProduct.fulfilled, (state) => {
                state.success = true;
                state.loading = false;
            })
            .addCase(postProduct.rejected, (state, action) => {
                state.success = null;
                state.loading = false;
                state.errorMessage = action.payload ? action.payload : "Error Posting Products.";
            })
            .addCase(deleteProduct.pending, (state) => {
                state.loading = true;
                state.success = null;
                state.error = null;
            })
            .addCase(deleteProduct.fulfilled, (state, action) => {
                state.loading = false;
                state.success = action.payload;
                state.error = null;
            })
            .addCase(deleteProduct.rejected, (state, action) => {
                state.loading = false;
                state.success = false;
                state.error = action.payload ? action.payload : "Error Deleting Product.";
            })
        }
    }
);

export default newProductReducer.reducer;


