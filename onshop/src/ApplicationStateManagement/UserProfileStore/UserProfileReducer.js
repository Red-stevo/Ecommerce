import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

const userProfileAdapter = createEntityAdapter();


const initialState = userProfileAdapter.getInitialState({
    error:"", loading:false, success:null, userProfileDetails:{}
});


export const getUserProfile = createAsyncThunk("userProfile/getUserProfile",
    async (userId = null, {
        fulfillWithValue,
        rejectWithValue}) => {

        try {
            return fulfillWithValue(((await RequestsConfig.get(`/customer/profile/${userId}`)).data));
        }catch (error){
            return rejectWithValue(error.response.data.message ? error.response.data.message : error.response.data);
        }
    })


export const UpdateEmail = createAsyncThunk("userProfile/updateEmail",
    async (data = null, {
        fulfillWithValue,
        rejectWithValue}) => {

        const {userId, email} = data;

        try {
            await RequestsConfig.put(`/customer/profile/update/email/${userId}`, email,
                {headers:{"Content-Type": "application/json"} });
            return fulfillWithValue(true);
        }catch (error){
            return rejectWithValue(error.response.data.message ? error.response.data.message : error.response.data);
        }
    });

export const updateUserData = createAsyncThunk("userProfile/updateData",
    async (data = null, {
        fulfillWithValue,
        rejectWithValue}) => {

        try {
            await RequestsConfig.put(`/customer/profile/update`, data, {headers:{"Content-Type": "application/json"}})
            return fulfillWithValue(true);
        }catch (error){
            return rejectWithValue(error.response.data.message ? error.response.data.message : error.response.data);
        }
    });

export const updateProfileImage = createAsyncThunk("userProfile/updateData",
    async (data = null, {
        fulfillWithValue,
        rejectWithValue}) => {

    const {userId, upload} = data;
        try {
            await RequestsConfig.put(`customer/profile/image/update/{{userId}}`, data, {headers:{"Content-Type": "application/json"}})
            return fulfillWithValue(true);
        }catch (error){
            return rejectWithValue(error.response.data.message ? error.response.data.message : error.response.data);
        }
    });



const UserProfileReducer = createSlice({
    name:"userProfile",
    initialState,
    reducers:{
        updateUserEmail:(state, action) => {
            state.userProfileDetails.email = action.payload;
        },
        updateUserState:(state, action) => {
            state.userProfileDetails = { ...state.userProfileDetails, ...action.payload};
        }
    },
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
            state.userProfileDetails = action.payload;
        })
        .addCase(getUserProfile.rejected, (state, action) => {
            state.loading = false;
            state.success = false;
            state.error = action.payload ? action.payload : "Error Fetching User Profile";
        })
        .addCase(UpdateEmail.pending, (state) => {
            state.loading = true;
            state.success = null;
            state.error = null;
        })
        .addCase(UpdateEmail.fulfilled, (state, action) => {
            state.loading = false;
            state.success = action.payload;
            state.error = null;
        })
        .addCase(UpdateEmail.rejected, (state, action) => {
            state.loading = false;
            state.success = false;
            state.error = action.payload ? action.payload : "Error Updating the Email.";
        })
        .addCase(updateUserData.pending, (state) => {
            state.loading = true;
            state.success = null;
            state.error = null;
        })
        .addCase(updateUserData.fulfilled, (state, action) => {
            state.loading = false;
            state.success = action.payload;
            state.error = null;
        })
        .addCase(updateUserData.rejected, (state, action) => {
            state.loading = false;
            state.success = false;
            state.error = action.payload ? action.payload : "Error Updating the User Data.";
        })
});


export default  UserProfileReducer.reducer;

export const { updateUserEmail, updateUserState
} = UserProfileReducer.actions;