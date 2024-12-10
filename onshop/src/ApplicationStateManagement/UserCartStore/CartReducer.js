import {createAsyncThunk, createEntityAdapter, createReducer, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

const cartAdapter = createEntityAdapter();



const addToCart = createAsyncThunk("cart/addToCart",
    async (productId, {fulfillWithValue
        ,rejectWithValue}) => {


    try {
        await RequestsConfig.post("")
        fulfillWithValue(true);
    }catch (e){
        return rejectWithValue(error.response ? error.response.data : error.data);
    }

});

const initialState = cartAdapter.getInitialState({

});


const CartReducer = createSlice({
    name:"cart",
    initialState,
})