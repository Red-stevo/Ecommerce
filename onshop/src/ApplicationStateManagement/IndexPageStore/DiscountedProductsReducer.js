import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

const DiscountAdapter = createEntityAdapter();

const  initialState = DiscountAdapter.getInitialState({
    success:null,
    loading:false,
    errorMessage:null,
    discountProducts:[],
})



export const getDiscountedProduct =
    createAsyncThunk("discounted-products/get-products", async (_,
           {fulfillWithValue,
               rejectWithValue
           }) => {

        /*Axios request to save category.*/
        try {
            return fulfillWithValue((await RequestsConfig.get(`/open/products/discounted?size=6`)).data);
        }catch (e){
            return rejectWithValue(e.response.data.message ? e.response.data.message : e.response.data);
        }
    }
);





const DiscountedProductsReducer = createSlice(
    {
        name:"discounted-products",
        initialState,
        reducers:{},
        extraReducers: builder => {
            builder
                .addCase(getDiscountedProduct.pending, (state) => {
                    state.loading = true;
                    state.success = null;
                    state.errorMessage = null;
                })
                .addCase(getDiscountedProduct.fulfilled, (state, action) => {
                    state.discountProducts = action.payload;
                    state.loading = false;
                    state.errorMessage = null;
                    state.success = true;
                })
                .addCase(getDiscountedProduct.rejected, (state, action) => {
                    state.success = null;
                    state.loading = false;
                    state.errorMessage = action.payload ? action.payload : "Error fetching Discounted Products.";
                })
        }
    }
);

export default DiscountedProductsReducer.reducer;