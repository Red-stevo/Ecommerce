import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";


const paymentAdapter = createEntityAdapter();


const initialState = paymentAdapter.getInitialState({
    error:"",loading:false, success:null
});


export const getPaymentDetails = createAsyncThunk("location/fetch",
    async (query = null, {fulfillWithValue,
        rejectWithValue}) => {

        try {
            return fulfillWithValue((await openCageAxiosConfig.get(``)).data);
        }catch (error){
            return rejectWithValue(error.response.data.message ? error.response.data.message : error.response.data);
        }
    })


const PaymentReducer = createSlice({
    name:"payment",
    initialState,
    reducers:{},
    extraReducers:builder => builder
        .addCase(getPaymentDetails.pending, (state) => {
            state.loading = true;
            state.success = null;
            state.error = null;
        })
        .addCase(getPaymentDetails.fulfilled, (state, action) => {
            state.loading = false;
            state.success = true;
            state.error = null;
        })
        .addCase(getPaymentDetails.rejected, (state, action) => {
            state.loading = false;
            state.success = false;
            state.error = action.payload ? action.payload : "Error Fetching Payment Details";
        })
});


export default  PaymentReducer.reducer;