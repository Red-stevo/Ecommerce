import "./Styles/WishListPage.css";
import {Button, Image} from "react-bootstrap";
import {FaTrash} from "react-icons/fa";
import {useSelector} from "react-redux";
import Loader from "../../../Loading/Loader.jsx";


const WishListPage = () => {
    const {loading, success, wishListProducts, errorMessage} = useSelector(state => state.WishListReducer);


    return (
        <div className={"wish-list-page"}>
            <div className={"wish-list-page-header"}>
                <span className={"wish-list-header product"}>Product</span>
                <span className={"wish-list-header price"}>Price</span>
                <span className={"wish-list-header status"}>Status</span>
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
                <Button className={"app-button add-all "}>Add All to Cart</Button>
            </section>

            {loading && <Loader />}
        </div>
    );
};

export default WishListPage;