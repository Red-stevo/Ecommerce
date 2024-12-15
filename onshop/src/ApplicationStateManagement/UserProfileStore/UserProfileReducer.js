import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {openCageAxiosConfig} from "./OpenCageAxiosConfig.js";
import {RequestsConfig} from "../RequestsConfig.js";

const userProfileAdapter = createEntityAdapter();


const initialState = userProfileAdapter.getInitialState({
    error:"", loading:false, success:null
});


export const getUserProfile = createAsyncThunk("userProfile/getUserProfile",
    async (query = null, {
        fulfillWithValue,
        rejectWithValue}) => {

        try {
            return fulfillWithValue(((await RequestsConfig.get("admin/orders/all")).data));
        }catch (error){
            return rejectWithValue(error.response.data.message ? error.response.data.message : error.response.data);
        }
    })


const UserProfileReducer = createSlice({
    name:"userProfile",
    initialState,
    reducers:{},
    extraReducers:builder => builder
        .addCase(getUserProfile.pending, (state) => {
            state.loading = true;
            state.success = null;
            state.error = null;
        })
        .addCase(getUserProfile.fulfilled, (state, action) => {
            state.loading = false;
            state.success = true;
            state.error = null;
            state.pagedModel = [...action.payload];
        })
        .addCase(getUserProfile.rejected, (state, action) => {
            state.loading = false;
            state.success = false;
            state.error = action.payload ? action.payload : "Error Fetching Location";
            state.results = [];
        })
});


export default  UserProfileReducer.reducer;