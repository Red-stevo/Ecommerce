import "./Styles/WishListPage.css";
import {Button, Image} from "react-bootstrap";
import {FaTrash} from "react-icons/fa";
import {useDispatch, useSelector} from "react-redux";
import Loader from "../../../Loading/Loader.jsx";
import {useEffect} from "react";
import {deleteWishList, getWishList} from "../../../ApplicationStateManagement/UserWishListStore/WishListReducer.js";


const WishListPage = () => {
    const dispatch = useDispatch();
    const {loading, success, wishListProducts, errorMessage} = useSelector(state => state.WishListReducer);


    useEffect(() => {
        const userId = "12a8bb23-0d41-4118-bda0-0390f382814b";
        dispatch(getWishList(userId));
    }, []);


    /*Delete an Items in the wish List*/
    const handleDeleteItem = (productId) => {
        const userId = "12a8bb23-0d41-4118-bda0-0390f382814b";
        const data = {userId, specificProductIds:productId};
        dispatch(deleteWishList(data));
    }

    /*Clear the Whole Wish List.*/
    const handleDeleteWishList = () => {
        const userId = "12a8bb23-0d41-4118-bda0-0390f382814b";
        const data = {userId, specificProductIds:""};
        dispatch(deleteWishList(data));
    }


    return (
        <div className={"wish-list-page"}>
            <div className={"wish-list-page-header"}>
                <span className={"wish-list-header product"}>Product</span>
                <span className={"wish-list-header price"}>Price</span>
                <span className={"wish-list-header status"}>Status</span>
            </div>

            <section className={"wish-list-products"}>

                {wishListProducts && wishListProducts.length > 0 && wishListProducts.
                    map(({imageUrl, price, inStock, productColor, productName, specificProductId}, index) => (
                    <div key={index} className={`product-details-holder-wish-list ${inStock ? "green": "red"}`}>
                        <Image className={"wish-list-product-image"} src={imageUrl}/>
                        <div className={"wish-list-product-details"}>

                            <div className={"top-details"}>
                                <span className={"wish-list-product-name"}>{productName}</span>
                                <span className={"wish-list-product-price"}>ksh {price}</span>
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

                        <FaTrash className={"delete-button"} onClick={() => handleDeleteItem(specificProductId)}/>
                    </div>
                ))}

            </section>

            <section className={"wish-list-bottom-button"}>
                <button className={"clear-wish-list"} onClick={handleDeleteWishList}>Clear WishList</button>
                <Button className={"app-button add-all "}>Add All to Cart</Button>
            </section>

            {loading && <Loader />}
        </div>
    );
};

export default WishListPage;