import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "../RequestsConfig.js";

const newCategoryAdapter = createEntityAdapter();

const  initialState = newCategoryAdapter.getInitialState({
    success:null,
    loading:false,
    errorMessage:null,
    categories:[],
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
            await RequestsConfig.post(`/admin/products/create-category?filenames=${categoryData.categoryName}`, files,
                {headers:{'Content-Type': 'application/x-www-form-urlencoded'}});
            return fulfillWithValue(true);
        }catch (e){
            return rejectWithValue(e.response.data.message ? e.response.data.message : e.response.data);
        }
    }
);

export const getCategories = createAsyncThunk("new-category/get-categories",
    async (_,
           {fulfillWithValue,
               rejectWithValue
           }) => {

        /*Axios request to save category.*/
        try {
            return fulfillWithValue((await RequestsConfig.get(`/admin/products/categories`)).data);
        }catch (e){
            return rejectWithValue(e.response.data.message ? e.response.data.message : e.response.data);
        }
    }
);


export const putCategories = createAsyncThunk("new-category/put-categories",
    async (updateCategoryData = null,
           {fulfillWithValue,
               rejectWithValue
           }) => {

    const formData = new FormData();

    formData.append("fileUploads", updateCategoryData.categoryIcon);


        /*Axios request to save category.*/
        try {
            return fulfillWithValue(
                (await RequestsConfig.put(
`/admin/products/update-categories?categoryId=${updateCategoryData.categoryId}&categoryName=${updateCategoryData.categoryName}`
                    , formData, {headers:{'Content-Type':'application/x-www-form-urlencoded'}})).data);
        }catch (e){
            return rejectWithValue(e.response.data.message ? e.response.data.message : e.response.data);
        }
    }
);



export const deleteCategories = createAsyncThunk("new-category/delete-categories",
    async (categoryId = null,
           {fulfillWithValue,
               rejectWithValue
           }) => {

        /*Axios request to save category.*/
        try {
            await RequestsConfig.delete(`/admin/products/delete-category?categoryId=${categoryId}`);
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
                    state.success = null;
                })
                .addCase(postCategory.fulfilled, (state) => {
                    state.success = true;
                    state.loading = false;
                })
                .addCase(postCategory.rejected, (state, action) => {
                    state.success = null;
                    state.loading = false;
                    state.errorMessage = action.payload ? action.payload : "Error Posting Products.";
                })
                .addCase(getCategories.pending, (state) => {
                    state.loading = true;
                    state.success = null;
                    state.errorMessage = null;
                })
                .addCase(getCategories.fulfilled, (state, action) => {
                    state.categories = action.payload;
                    state.loading = false;
                    state.errorMessage = null;
                })
                .addCase(getCategories.rejected, (state, action) => {
                    state.success = null;
                    state.loading = false;
                    state.errorMessage = action.payload ? action.payload : "Error fetching Products.";
                })
                .addCase(putCategories.pending, (state) => {
                    state.loading = true;
                    state.success = null;
                    state.errorMessage = null;
                })
                .addCase(putCategories.fulfilled, (state) => {
                    state.success = true;
                    state.loading = false;
                    state.errorMessage = null;
                })
                .addCase(putCategories.rejected, (state, action) => {
                    state.success = null;
                    state.loading = false;
                    state.errorMessage = action.payload ? action.payload : "Error updating Products.";
                })
                .addCase(deleteCategories.pending, (state) => {
                    state.loading = true;
                    state.success = null;
                    state.errorMessage = null;
                })
                .addCase(deleteCategories.fulfilled, (state) => {
                    state.success = true;
                    state.loading = false;
                    state.errorMessage = null;
                })
                .addCase(deleteCategories.rejected, (state, action) => {
                    state.success = null;
                    state.loading = false;
                    state.errorMessage = action.payload ? action.payload : "Error Deleting Products.";
                });
        }
    }
);

