import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";


const paymentAdapter = createEntityAdapter();


const initialState = paymentAdapter.getInitialState({
    error:"",loading:false, success:null, paymentDetails:{}
});


export const getPaymentDetails = createAsyncThunk("location/fetch",
    async (userId = null, {fulfillWithValue,
        rejectWithValue}) => {

        try {
            return fulfillWithValue((await RequestsConfig.get(`/costumer/orders/payment-details/${userId}`)).data);
        }catch (error){
            return rejectWithValue(error.response.data.message ? error.response.data.message : error.response.data);
        }
    })


const PaymentReducer = createSlice({
    name:"payment",
    initialState,
    reducers:{},
    extraReducers:builder => builder
        .addCase(getPaymentDetails.pending, (state) => {
            state.loading = true;
            state.success = null;
            state.error = null;
        })
        .addCase(getPaymentDetails.fulfilled, (state, action) => {
            state.loading = false;
            state.success = true;
            state.error = null;
            state.paymentDetails = action.payload;
        })
        .addCase(getPaymentDetails.rejected, (state, action) => {
            state.loading = false;
            state.success = false;
            state.error = action.payload ? action.payload : "Error Fetching Payment Details";
        })
});


export default  PaymentReducer.reducer;