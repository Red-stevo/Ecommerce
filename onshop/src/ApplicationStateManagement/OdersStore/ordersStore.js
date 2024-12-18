import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";
import data from "bootstrap/js/src/dom/data.js";

const ordersAdapter = createEntityAdapter();


const initialState = ordersAdapter.getInitialState({
    error:"", loading:false, success:null, pagedModel:{}
});


export const getOrders = createAsyncThunk("orders/getOrders",
    async (_, {
        fulfillWithValue,
        rejectWithValue}) => {

        try {
            return fulfillWithValue(((await RequestsConfig.get("admin/orders/all")).data));
        }catch (error){
            return rejectWithValue(error.response.data.message ? error.response.data.message : error.response.data);
        }
    })

export const updateOrderStatus = createAsyncThunk("orders/updateStatus",
    async (data = null, {
        fulfillWithValue,
        rejectWithValue}) => {


    const {orderId, status} = data;
        try {
            await RequestsConfig.put(`admin/orders/update-shipping-status/${orderId}`, status,
                {headers:{"Content-Type":"application/json"}});
            return fulfillWithValue(true);
        }catch (error){
            return rejectWithValue(error.response.data.message ? error.response.data.message : error.response.data);
        }
    });


const ordersStore = createSlice({
    name:"orders",
    initialState,
    reducers:{},
    extraReducers:builder => builder
        .addCase(getOrders.pending, (state) => {
            state.loading = true;
            state.success = null;
            state.error = null;
        })
        .addCase(getOrders.fulfilled, (state, action) => {
            state.loading = false;
            state.success = true;
            state.error = null;
            state.pagedModel = action.payload;
        })
        .addCase(getOrders.rejected, (state, action) => {
            state.loading = false;
            state.success = false;
            state.error = action.payload ? action.payload : "Error Getting Orders!";
        })
        .addCase(updateOrderStatus.pending, (state) => {
            state.loading = true;
            state.success = null;
            state.error = null;
        })
        .addCase(updateOrderStatus.fulfilled, (state, action) => {
            state.loading = false;
            state.success = action.payload;
            state.error = null;
        })
        .addCase(updateOrderStatus.rejected, (state, action) => {
            state.loading = false;
            state.success = false;
            state.error = action.payload ? action.payload : "Error Updating Order Status!";
        })
});


export default  ordersStore.reducer;