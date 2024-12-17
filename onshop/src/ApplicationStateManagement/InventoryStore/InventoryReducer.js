import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

const inventoryAdapter = createEntityAdapter();

const initialState = inventoryAdapter.getInitialState({
    error:"", loading:false, success:null, InventoryResponse:{}, page:{}
});


export const fetchInventory = createAsyncThunk("inventory/fetchProducts",
    async (_, {
        fulfillWithValue,
        rejectWithValue}) => {

        try {
            const res = (await RequestsConfig.get(`/admin/products/show-inventory`)).data;

            const data = {
                InventoryResponseList: res._embedded.inventoryResponseList, page:res.page
            }


            return fulfillWithValue(data);
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
            state.InventoryResponse = action.payload.InventoryResponseList;
            state.page = action.payload.page;
        })
        .addCase(fetchInventory.rejected, (state, action) => {
            state.loading = false;
            state.success = false;
            state.error = action.payload ? action.payload : "Error Fetching Products";
        })
});


export default  InventoryReducer.reducer;
