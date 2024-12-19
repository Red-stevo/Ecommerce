import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";


const SingleProductAdapter = createEntityAdapter();

const initialState = SingleProductAdapter.getInitialState(
    {errorMessage:null, loading:false, success:null, productDetails:{}});

export const getSingleProduct = createAsyncThunk("order/getSingleProduct",
    async (orderItemId = null, {rejectWithValue,fulfillWithValue }) => {
        try {
            return fulfillWithValue((await RequestsConfig.get(`/costumer/orders/specific-order-item/${orderItemId}`)).data);
        }catch (error){
            return rejectWithValue(error.response ? error.response.data : error.data);
        }
    });


const SingleProductReducer = createSlice({
    name:"order",
    initialState,
    reducers:{},
    extraReducers: builder =>
        builder
            .addCase(getSingleProduct.pending, (state) => {
                state.success = null;
                state.errorMessage = null;
                state.loading = true;
            })
            .addCase(getSingleProduct.fulfilled, (state, action) => {
                state.loading = false;
                state.errorMessage = null;
                state.success = true;
                state.productDetails = action.payload;
            })
            .addCase(getSingleProduct.rejected, (state, action) => {
                state.errorMessage = action.payload;
                state.loading = false;
                state.success = null;
            })
});

export  default  SingleProductReducer.reducer;

