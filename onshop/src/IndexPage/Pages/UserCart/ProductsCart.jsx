import "./Styles/ProductsCart.css";
import {Button} from "react-bootstrap";
import {useState} from "react";
import {ImCheckboxChecked, ImCheckboxUnchecked} from "react-icons/im";



const cartProducts = {
    username:"Stephen Muiru",
    cartId:"AS43DF",
    cartItemsResponses:[
        {cartItemId:"WQE34", productName:"Laptop", productId:"ASA101", productPrice:2450.00,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {cartItemId:"WQE34", productName:"Laptop", productId:"ASA101", productPrice:2450.00,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {cartItemId:"WQE34", productName:"Laptop", productId:"ASA101", productPrice:2450.00,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {cartItemId:"WQE34", productName:"Laptop", productId:"ASA101", productPrice:2450.00,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {cartItemId:"WQE34", productName:"Laptop", productId:"ASA101", productPrice:2450.00,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {cartItemId:"WQE34", productName:"Laptop", productId:"ASA101", productPrice:2450.00,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {cartItemId:"WQE34", productName:"Laptop", productId:"ASA101", productPrice:2450.00,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },

    ],
    youMayLikes:[
        {productId:"ASD32", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD32", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD32", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD32", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD32", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD32", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD32", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD32", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD32", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD32", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
        {productId:"ASD32", productName:"Laptop", productPrice:1850.00, rating:4,
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        },
    ],
    currentPage:0,
    totalPages:5,
    hasMore:true
}

const ProductsCart = () => {
    const [selectAllCheck, setSelectAllCheck] = useState(false);


    return (
        <div className={"cart-page"}>
            {/*Page header.*/}
           <div className={"top-buttons"}>
               <div onClick={() => setSelectAllCheck(!selectAllCheck)} className={"select-all-holder"}>
                   {selectAllCheck ?
                       <ImCheckboxChecked className={"select-checked"} /> :
                       <ImCheckboxUnchecked className={"select-unchecked"} />}
                   <span>select all</span>
               </div>
               <Button className={"danger-button delete-order-product"}>Delete</Button>
           </div>

            {/*Product Display.*/}
            <div className={"ordered-products-display"}>

            </div>
        </div>
    );
};

export default ProductsCart;