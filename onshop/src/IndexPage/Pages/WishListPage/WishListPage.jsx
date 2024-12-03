import "./Styles/WishListPage.css";
import {Button, Image} from "react-bootstrap";
import {FaTrash} from "react-icons/fa";


const wishListProducts = [
    {
        productImage:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        productPrice:2100, inStock:true, productColor:"brown", productName:"J4 Classic Jordan, tough, durable and comfortable"
    },
    {
        productImage:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        productPrice:2100, inStock:false, productColor:"brown", productName:"J4 Classic Jordan, tough, durable and comfortable"
    },
    {
        productImage:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        productPrice:2100, inStock:true, productColor:"brown", productName:"J4 Classic Jordan, tough, durable and comfortable"
    },
    {
        productImage:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        productPrice:2100, inStock:true, productColor:"brown", productName:"J4 Classic Jordan, tough, durable and comfortable"
    },
    {
        productImage:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        productPrice:2100, inStock:false, productColor:"brown", productName:"J4 Classic Jordan, tough, durable and comfortable"
    },
    {
        productImage:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        productPrice:2100, inStock:true, productColor:"brown", productName:"J4 Classic Jordan, tough, durable and comfortable"
    },
    {
        productImage:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
        productPrice:2100, inStock:true, productColor:"brown", productName:"J4 Classic Jordan, tough, durable and comfortable"
    }
]


const WishListPage = () => {
    return (
        <div className={"wish-list-page"}>
            <div className={"wish-list-page-header"}>
                <span className={"wish-list-header product"} >Product</span>
                <span className={"wish-list-header price"} >Price</span>
                <span className={"wish-list-header status"} >Status</span>
            </div>

            <section className={"wish-list-products"}>

                {wishListProducts && wishListProducts.length > 0 && wishListProducts.
                    map(({productImage,productPrice, inStock, productColor,
                            productName}, index) => (
                    <div key={index} className={`product-details-holder-wish-list ${inStock ? "green": "red"}`}>
                        <Image className={"wish-list-product-image"} src={productImage}/>
                        <div className={"wish-list-product-details"}>

                            <div className={"top-details"}>
                                <span className={"wish-list-product-name"}>{productName}</span>
                                <span className={"wish-list-product-price"}>ksh {productPrice}</span>
                                <span className={"wish-list-product-stock"}>
                                    {inStock ?
                                    <span className={"in-stock"}>In Stock</span>:
                                    <span className={"out-stock"}>Out of Stock</span>}
                                </span>
                            </div>

                            <div className={"down-details"}>
                                <span className={"wish-list-product-color"}>Color : {productColor}</span>
                                <Button className={"app-button add-cart-button"}>Add to Cart</Button>
                            </div>

                        </div>

                        <FaTrash className={"delete-button"}/>
                    </div>
                ))}

            </section>

            <section className={"wish-list-bottom-button"}>
                <button className={"clear-wish-list"}>Clear WishList</button>
                <Button className={"app-button add-all "}>Add All</Button>
            </section>


        </div>
    );
};

export default WishListPage;