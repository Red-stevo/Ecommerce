import {createAsyncThunk, createEntityAdapter, createSlice} from "@reduxjs/toolkit";
import {RequestsConfig} from "./RequestsConfig.js";

const product = {
    productName:"J4 cactus jack sneakers",
    productDescription:"The J4 Cactus Jack sneakers blend iconic Jordan 4 craftsmanship with Travis Scott's signature touch. Made from premium suede, they boast a striking colorway, bold Cactus Jack branding, and subtle details inspired by the rapper's vision. Combining superior comfort, durable construction, and a sleek design, these sneakers are perfect for sneakerheads, streetwear enthusiasts, or anyone seeking a bold, standout look. A true collector's item that effortlessly merges performance with head-turning style.",
    products : [{
        productId:0,
        productNewPrice:230,
        productOldPrice:130,
        productColor:"grey",
        productSize:"40",
        productImages:[
            "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.7EcpxSawDjrNMtY53rDrAgHaFM%26pid%3DApi&f=1&ipt=9f959aef6492e368dc6b49385e7c74124b91770fd9b48834b926f267f1e04132&ipo=images",
            "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.-AQdZKkdXgCCbMqJ-vSGYAHaIN%26pid%3DApi&f=1&ipt=23a4f9f5763ecce0456e62e6664b54a9824efa05a4366dc83e99df5c6224e8d4&ipo=images",
            "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.0UPNkBno4Uby2FP56Hb9rAHaIV%26pid%3DApi&f=1&ipt=501e5371d19c5c439afa88ff690122cb224bd45103634926a29eba75220be374&ipo=images",
        ],
        productCount:6,
    },
   {
       productId:1,
       productNewPrice:340,
       productOldPrice:300,
       productColor:"blue",
       productSize:"42",
       productImages:
           [
               "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.rGgqnuAANMoJmnI1rB0nCAHaFM%26pid%3DApi&f=1&ipt=499a5baa5cb904ac23f0741e93dfcff3b7b853b43f3447a8cc54a92c90072ee8&ipo=images",
               "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.N6ni7ZrAAE3pfCMJx_6VDgHaFS%26pid%3DApi&f=1&ipt=8a16784e1cb9c4936276a9441c5eac634b8fc53287a0408480a9e40ef23c19c4&ipo=images",
               "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.3u89GR6ixms3rLugoYU8EQHaFS%26pid%3DApi&f=1&ipt=9db5989876d669511a1a32a9dd5f387ac05da13e20264ae51fc1d6442dddf8b0&ipo=images",
               "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.Wv4ZqaYugCpjSVCfKCglmQHaJQ%26pid%3DApi&f=1&ipt=fb7a0ea83a51bd9f0b7a45292f04cc0bbf8876e0c6ac1c5247c756d5b4412e94&ipo=images",
               "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.NRzuufyYKGbUgrdABMmzbwHaE8%26pid%3DApi&f=1&ipt=b7259b746eae27adeb57ab09c332203e11d4664830cff19710130a33606f635e&ipo=images",],
       productCount:5
   },
   {
       productId:2,
       productNewPrice:400,
       productOldPrice:300,
       productColor:"blue",
       productSize:"40",
       productImages:[
           "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.N6ni7ZrAAE3pfCMJx_6VDgHaFS%26pid%3DApi&f=1&ipt=8a16784e1cb9c4936276a9441c5eac634b8fc53287a0408480a9e40ef23c19c4&ipo=images",
           "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.3u89GR6ixms3rLugoYU8EQHaFS%26pid%3DApi&f=1&ipt=9db5989876d669511a1a32a9dd5f387ac05da13e20264ae51fc1d6442dddf8b0&ipo=images",
           "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.Wv4ZqaYugCpjSVCfKCglmQHaJQ%26pid%3DApi&f=1&ipt=fb7a0ea83a51bd9f0b7a45292f04cc0bbf8876e0c6ac1c5247c756d5b4412e94&ipo=images",
           "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.NRzuufyYKGbUgrdABMmzbwHaE8%26pid%3DApi&f=1&ipt=b7259b746eae27adeb57ab09c332203e11d4664830cff19710130a33606f635e&ipo=images",
       ],
       productCount:15
   },
   {
       productId:3,
       productNewPrice:340,
       productOldPrice:300,
       productColor:"brown",
       productSize:"43",
       productImages:[
           "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.HyMNmmOjAf7ZL-NfSvwgwgHaE0%26pid%3DApi&f=1&ipt=cc72685fa3c4739244236a5a0c015f87f95b62565d638773fead08e27d0d99f8&ipo=images",
           "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.zw0ChX82_xO2LEJ3ykEWHgHaHa%26pid%3DApi&f=1&ipt=9db89c60d00626d8a65cae27815ee8fccdb96b66c2a5e56a86abeb20f7374823&ipo=images",
           "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.PrMI_Pi-MzzwU6YOFnCicgAAAA%26pid%3DApi&f=1&ipt=836ff1012584d730602ad6e849b5aae7b22c673212ffe0ba64b9f9fbf66297f5&ipo=images",
           "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.yZkw_lvAsz4GJH-he7gqmwHaFM%26pid%3DApi&f=1&ipt=4bf0e3a987a998dc2dbe6ffe37389e6d7ce1c7b1885de0ed5c51f19da2acdc2c&ipo=images",
       ],
       productCount:5,
   },
   {
       productId:4,
       productNewPrice:400,
       productOldPrice:300,
       productColor:"yellow",
       productSize:"38",
       productImages:[
           "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.s4ngUC0bNlUblbz3LxHkAgHaE_%26pid%3DApi&f=1&ipt=e7f21b8371e44f6c989fad4a3432291016c19ece97992da6ee1000dd20062e45&ipo=images",
           "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.Aut9jh1P5PrLQnoN_HXUigHaHV%26pid%3DApi&f=1&ipt=fa9d9592b0eddc3afb50d88aa7cc7f3fe615dc3b30aa11aba87d96665963ae6b&ipo=images",
           "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.s4ngUC0bNlUblbz3LxHkAgHaE_%26pid%3DApi&f=1&ipt=e7f21b8371e44f6c989fad4a3432291016c19ece97992da6ee1000dd20062e45&ipo=images",
       ],
       productCount:15,
   },
    {
        productId:2,
        productNewPrice:400,
        productOldPrice:300,
        productColor:"blue",
        productSize:"41",
        productImages:[
            "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.N6ni7ZrAAE3pfCMJx_6VDgHaFS%26pid%3DApi&f=1&ipt=8a16784e1cb9c4936276a9441c5eac634b8fc53287a0408480a9e40ef23c19c4&ipo=images",
            "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.3u89GR6ixms3rLugoYU8EQHaFS%26pid%3DApi&f=1&ipt=9db5989876d669511a1a32a9dd5f387ac05da13e20264ae51fc1d6442dddf8b0&ipo=images",
            "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.Wv4ZqaYugCpjSVCfKCglmQHaJQ%26pid%3DApi&f=1&ipt=fb7a0ea83a51bd9f0b7a45292f04cc0bbf8876e0c6ac1c5247c756d5b4412e94&ipo=images",
            "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.NRzuufyYKGbUgrdABMmzbwHaE8%26pid%3DApi&f=1&ipt=b7259b746eae27adeb57ab09c332203e11d4664830cff19710130a33606f635e&ipo=images",
        ],
        productCount:15
    },
    {
        productId:4,
        productNewPrice:400,
        productOldPrice:300,
        productColor:"yellow",
        productSize:"41",
        productImages:[
            "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.s4ngUC0bNlUblbz3LxHkAgHaE_%26pid%3DApi&f=1&ipt=e7f21b8371e44f6c989fad4a3432291016c19ece97992da6ee1000dd20062e45&ipo=images",
            "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.Aut9jh1P5PrLQnoN_HXUigHaHV%26pid%3DApi&f=1&ipt=fa9d9592b0eddc3afb50d88aa7cc7f3fe615dc3b30aa11aba87d96665963ae6b&ipo=images",
            "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.s4ngUC0bNlUblbz3LxHkAgHaE_%26pid%3DApi&f=1&ipt=e7f21b8371e44f6c989fad4a3432291016c19ece97992da6ee1000dd20062e45&ipo=images",
        ],
        productCount:15,
    },
        {
            productId:4,
            productNewPrice:400,
            productOldPrice:300,
            productColor:"yellow",
            productSize:"43",
            productImages:[
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.s4ngUC0bNlUblbz3LxHkAgHaE_%26pid%3DApi&f=1&ipt=e7f21b8371e44f6c989fad4a3432291016c19ece97992da6ee1000dd20062e45&ipo=images",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.Aut9jh1P5PrLQnoN_HXUigHaHV%26pid%3DApi&f=1&ipt=fa9d9592b0eddc3afb50d88aa7cc7f3fe615dc3b30aa11aba87d96665963ae6b&ipo=images",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.s4ngUC0bNlUblbz3LxHkAgHaE_%26pid%3DApi&f=1&ipt=e7f21b8371e44f6c989fad4a3432291016c19ece97992da6ee1000dd20062e45&ipo=images",
            ],
            productCount:15,
        },
        {
            productId:3,
            productNewPrice:340,
            productOldPrice:300,
            productColor:"brown",
            productSize:"41",
            productImages:[
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.HyMNmmOjAf7ZL-NfSvwgwgHaE0%26pid%3DApi&f=1&ipt=cc72685fa3c4739244236a5a0c015f87f95b62565d638773fead08e27d0d99f8&ipo=images",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.zw0ChX82_xO2LEJ3ykEWHgHaHa%26pid%3DApi&f=1&ipt=9db89c60d00626d8a65cae27815ee8fccdb96b66c2a5e56a86abeb20f7374823&ipo=images",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.PrMI_Pi-MzzwU6YOFnCicgAAAA%26pid%3DApi&f=1&ipt=836ff1012584d730602ad6e849b5aae7b22c673212ffe0ba64b9f9fbf66297f5&ipo=images",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.yZkw_lvAsz4GJH-he7gqmwHaFM%26pid%3DApi&f=1&ipt=4bf0e3a987a998dc2dbe6ffe37389e6d7ce1c7b1885de0ed5c51f19da2acdc2c&ipo=images",
            ],
            productCount:5,
        }
],

relatedProducts:[],
productReviews:[]
}





const productStoreAdapter = createEntityAdapter({selectId:product => product.productId});

export const getProductDetails = createAsyncThunk("product/get-product",
    async (productId = null, {fulfillWithValue,rejectWithValue}) => {
        try {
            return fulfillWithValue([]);
        }catch (error){
            return rejectWithValue(error.response ? error.response.data : error.data);
        }
    });

const initialState = productStoreAdapter.getInitialState({
    product,
    error:null,
    status:null,
});


const productStore = createSlice({
    name:"product",
    initialState,
    reducers:{},
    extraReducers:builder => {
     builder
         .addCase(getProductDetails.pending, (state) => {
            state.status = "loading"
            state.error = null;
        })
         .addCase(getProductDetails.fulfilled, (state, action) => {
             productStoreAdapter.setAll(state, action.payload.data);
             state.status = "fulfilled";
             state.error = null;
         })
         .addCase(getProductDetails.rejected, (state, action) => {
             state.error = action.payload;
             state.status = "failed";
         })
    }
});


export default productStore.reducer;
