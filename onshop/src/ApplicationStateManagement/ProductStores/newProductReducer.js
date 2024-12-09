import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

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
                });
        }
    }
);

export default newProductReducer.reducer;


