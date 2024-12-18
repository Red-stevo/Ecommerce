import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

const orderInfoAdapter = createEntityAdapter();


const initialState = orderInfoAdapter.getInitialState({
    error:"", loading:false, success:null, orderDetails:{}
});


export const getOrderInfo = createAsyncThunk("orderInfo/getOrderInfo",
    async (orderId = null, {
        fulfillWithValue,
        rejectWithValue}) => {

        try {
            return fulfillWithValue(((await RequestsConfig.get(`costumer/orders/${orderId}`)).data));
        }catch (error){
            return rejectWithValue(error.response.data.message ? error.response.data.message : error.response.data);
        }
    })



const OrderInfoReducer = createSlice({
    name:"orders",
    initialState,
    reducers:{},
    extraReducers:builder => builder
        .addCase(getOrderInfo.pending, (state) => {
            state.loading = true;
            state.success = null;
            state.error = null;
        })
        .addCase(getOrderInfo.fulfilled, (state, action) => {
            state.loading = false;
            state.success = true;
            state.error = null;
            state.orderDetails = action.payload;
        })
        .addCase(getOrderInfo.rejected, (state, action) => {
            state.loading = false;
            state.success = false;
            state.error = action.payload ? action.payload : "Error Getting Order Details!";
        })

});


export default  OrderInfoReducer.reducer;