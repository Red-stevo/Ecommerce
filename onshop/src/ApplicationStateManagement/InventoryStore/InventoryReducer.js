import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

const inventoryAdapter = createEntityAdapter();


const initialState = inventoryAdapter.getInitialState({
    error:"", loading:false, success:null, data:{}
});


export const fetchInventory = createAsyncThunk("inventory/fetchProducts",
    async (query = null, {
        fulfillWithValue,
        rejectWithValue}) => {

        try {
            return fulfillWithValue((await RequestsConfig.get(`/admin/products/show-inventory`)).data);
        }catch (error){
            return rejectWithValue(error.response.data.message ? error.response.data.message : error.response.data);
        }
    })


const InventoryReducer = createSlice({
    name:"inventory",
    initialState,
    reducers:{},
    extraReducers:builder => builder
        .addCase(fetchInventory.pending, (state) => {
            state.loading = true;
            state.success = null;
            state.error = null;
        })
        .addCase(fetchInventory.fulfilled, (state, action) => {
            state.loading = false;
            state.success = true;
            state.error = null;
            state.data = action.payload;
        })
        .addCase(fetchInventory.rejected, (state, action) => {
            state.loading = false;
            state.success = false;
            state.error = action.payload ? action.payload : "Error Fetching Location";
        })
});


export default  InventoryReducer.reducer;