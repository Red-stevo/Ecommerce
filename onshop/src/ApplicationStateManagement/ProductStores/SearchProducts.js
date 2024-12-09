import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

const productsAdapter = createEntityAdapter();

export const queryProducts = createAsyncThunk("products/query-products",
    async (data = null, {fulfillWithValue,rejectWithValue}) => {

    try {
            return fulfillWithValue(((await RequestsConfig.get(
                    `/open/products/search?query=${encodeURIComponent(data.query)}&page=0&size=12`)).data));
        }catch (error){
            return rejectWithValue(error.response ? error.response.data : error.data);
        }
    });

const initialState = productsAdapter.getInitialState({
    product:{},
    error:null,
    status:null,
});


const SearchProducts = createSlice({
    name:"products",
    initialState,
    reducers:{},
    extraReducers:builder => {
        builder
            .addCase(queryProducts.pending, (state) => {
                state.status = "loading"
                state.error = null;
            })
            .addCase(queryProducts.fulfilled, (state, action) => {
                state.product = action.payload;
                state.status = "fulfilled";
                state.error = null;
            })
            .addCase(queryProducts.rejected, (state, action) => {
                state.error = action.payload;
                state.status = "failed";
            })
    }
});


export default SearchProducts.reducer;
