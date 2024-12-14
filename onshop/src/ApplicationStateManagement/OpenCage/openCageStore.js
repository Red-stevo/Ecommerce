import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {openCageAxiosConfig} from "./OpenCageAxiosConfig.js";

const openCageAdapter = createEntityAdapter();


const initialState = openCageAdapter.getInitialState({
    error:"",results:[], loading:false, success:null
});


const fetchLocation = createAsyncThunk("location/fetch",
    async (query, {fulfillWithValue,
        rejectWithValue}) => {

    try {
        return fulfillWithValue((await openCageAxiosConfig.get(`/json?q=${query}`)).data);
    }catch (error){
        return rejectWithValue(error.response.data.message ? error.response.data.message : error.response.data);
    }
})


const openCageReducer = createSlice({
    name:"location",
    initialState,
    reducers:{},
    extraReducers:builder => builder
        .addCase(fetchLocation.pending, (state) => {
            state.loading = true;
            state.success = null;
            state.error = null;
        })
        .addCase(fetchLocation.fulfilled, (state, action) => {
            state.loading = false;
            state.success = true;
            state.error = null;
            state.results = [...action.payload];
        })
        .addCase(fetchLocation.rejected, (state, action) => {
            state.loading = false;
            state.success = false;
            state.error = action.payload ? action.payload : "Error Fetching Location";
            state.results = [];
        })
});