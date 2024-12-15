import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

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
            state.error = action.payload ? action.payload : "Error Fetching Location";
        })
});


export default  ordersStore.reducer;