import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

const productsAdapter = createEntityAdapter();

export const queryProducts = createAsyncThunk("products/query-products",
    async (data = null, {fulfillWithValue,rejectWithValue}) => {
        const {query, currentPage} = data;
    try {
        const response = (await RequestsConfig
            .get(`/open/products/search?query=${encodeURIComponent(query)}&page=${currentPage}&size=12`)).data;

        const responseData = {
            products:response._embedded.productsPageResponseList,
            page:response.page
        }

        return fulfillWithValue(responseData);
    }catch (error){
            return rejectWithValue(error.response ? error.response.data : error.data);
    }
    });

const initialState = productsAdapter.getInitialState({
    products:[],
    error:null,
    status:null,
    page:{},
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
                state.products = [...action.payload.products];
                state.page = action.payload.page;
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
