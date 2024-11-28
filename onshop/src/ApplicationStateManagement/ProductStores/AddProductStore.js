import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

const newProductAdapter = createEntityAdapter();

const  initialState = newProductAdapter.getInitialState({
    success:null,
    loading:false,
    errorMessage:null
})

export const postProduct = createAsyncThunk("new-product/create",
    async (data=null,
     {
         fulfillWithValue,
        rejectWithValue
     }) => {

        /*Axios request to save products.*/
        try {
            const response = await RequestsConfig.post("", data);
            return fulfillWithValue(true);
        }catch (e){
            return rejectWithValue(e.response.data.message ? e.response.data.message : e.response.data);
        }
    }
);



export const newProductReducer = createSlice(
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


