import {configureStore, createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";
const OrderStatusAdapter = createEntityAdapter();

const initialState = OrderStatusAdapter
    .getInitialState({errorMessage:null, loading:false, success:null, shippingStatus:{}});


export const getOrderStatus = createAsyncThunk("orderStatus/getOrderStatus",
    async (userId = null, {fulfillWithValue,rejectWithValue}) => {
        try {
            return fulfillWithValue((await RequestsConfig.get(`/costumer/orders/get-order-status?userId=${userId}`)).data);
        } catch (error) {
            return rejectWithValue(error.response ? error.response.data : error.data);
        }
    });

export const makeOrder = createAsyncThunk("orderStatus/make-order",
    async (orderData = null, {fulfillWithValue,rejectWithValue}) => {

        const {userId, request} = orderData;
        try {
            await RequestsConfig.post(`/costumer/orders/make-order?userId=${userId}`, request ,
                {headers:{"Content-Type":'application/json'}})
            return fulfillWithValue("Order Added successfully");
        } catch (error) {
            return rejectWithValue(error.response ? error.response.data : error.data);
        }
    });


export const cancelOrderItem = createAsyncThunk("orderStatus/cancelItem",
    async (data = null, {fulfillWithValue,rejectWithValue}) => {

    const {userId, orderItemId} = data;
        try {
            await RequestsConfig.put(`/costumer/orders/cancel-order-item?userId=${userId}&orderItemId=${orderItemId}`, _,
                    {headers:{"Content-Type": "application/x-www-form-urlencoded"}});
            return fulfillWithValue(true);
        } catch (error) {
            return rejectWithValue(error.response ? error.response.data : error.data);
        }
    });

const OrderStatusReducer = createSlice({
    name:"orderStatus",
    initialState,
    reducers:{},
    extraReducers: builder =>
        builder
            .addCase(getOrderStatus.pending, (state) => {
                state.success = null;
                state.errorMessage = null;
                state.loading = true;
            })
            .addCase(getOrderStatus.fulfilled, (state, action) => {
                state.loading = false;
                state.errorMessage = null;
                state.success = true;
                state.shippingStatus = {...action.payload}
            })
            .addCase(getOrderStatus.rejected, (state, action) => {
                state.errorMessage = action.payload;
                state.loading = false;
                state.success = null;
            })
            .addCase(cancelOrderItem.pending, (state) => {
                state.success = null;
                state.errorMessage = null;
                state.loading = true;
            })
            .addCase(cancelOrderItem.fulfilled, (state, action) => {
                state.loading = false;
                state.errorMessage = null;
                state.success = action.payload;
            })
            .addCase(cancelOrderItem.rejected, (state, action) => {
                state.errorMessage = action.payload;
                state.loading = false;
                state.success = null;
            })
            .addCase(makeOrder.pending, (state) => {
                state.success = null;
                state.errorMessage = null;
                state.loading = true;
            })
            .addCase(makeOrder.fulfilled, (state, action) => {
                state.loading = false;
                state.errorMessage = null;
                state.success = action.payload;
            })
            .addCase(makeOrder.rejected, (state, action) => {
                state.errorMessage = action.payload;
                state.loading = false;
                state.success = null;
            })
});

export  default  OrderStatusReducer.reducer;



