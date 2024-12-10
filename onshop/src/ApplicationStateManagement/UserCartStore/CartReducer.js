import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

const cartAdapter = createEntityAdapter();

const initialState = cartAdapter.getInitialState({errorMessage:"", loading:"", success:"",});

export const addToCart = createAsyncThunk("cart/addToCart",
    async (productId,
           {fulfillWithValue
        ,rejectWithValue}) => {
    try {
        await RequestsConfig.post("");
        fulfillWithValue(true);
    }catch (error){
        return rejectWithValue(error.response ? error.response.data : error.data);
    }
});



const CartReducer = createSlice({
    name:"cart",
    initialState,
    reducers:{},
    extraReducers: builder =>
        builder
            .addCase(addToCart.pending, (state) => {
                state.success = null;
                state.errorMessage = null;
                state.loading = true;
            })
            .addCase(addToCart.fulfilled, (state, action) => {
                state.loading = false;
                state.errorMessage = null;
                state.success = action.payload;
            })
            .addCase(addToCart.rejected, (state, action) => {
                state.errorMessage = action.payload;
                state.loading = false;
                state.success = null;
            })
});

export  default  CartReducer.reducer;



