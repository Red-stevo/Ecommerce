import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

const newCategoryAdapter = createEntityAdapter();

const  initialState = newCategoryAdapter.getInitialState({
    success:null,
    loading:false,
    errorMessage:null
})

export const postCategory = createAsyncThunk("new-category/create",
    async (categoryData =  null,
           {fulfillWithValue,
               rejectWithValue
           }) => {

        const files = new FormData();
        files.append("files", categoryData.file);

        /*Axios request to save category.*/
        try {
            await RequestsConfig.post(`/products/create-category?filenames=${categoryData.categoryName}`, files,
                {headers:{'Content-Type': 'application/x-www-form-urlencoded'}});
            return fulfillWithValue(true);
        }catch (e){
            return rejectWithValue(e.response.data.message ? e.response.data.message : e.response.data);
        }
    }
);



export const CategoriesReducer = createSlice(
    {
        name:"new-category",
        initialState,
        reducers:{},
        extraReducers: builder => {
            builder
                .addCase(postCategory.pending, (state) => {
                    state.loading = true;
                })

                .addCase(postCategory.fulfilled, (state) => {
                    state.success = true;
                    state.loading = false;
                })
                .addCase(postCategory.rejected, (state, action) => {
                    state.success = null;
                    state.loading = false;
                    state.errorMessage = action.payload ? action.payload : "Error Posting Products.";
                });
        }
    }
);


