import "./Styles/WishListPage.css";
import {Button, Image} from "react-bootstrap";
import {FaTrash} from "react-icons/fa";
import {useDispatch, useSelector} from "react-redux";
import Loader from "../../../Loading/Loader.jsx";
import {useEffect} from "react";
import {
    clearWishList,
    deleteWishList,
    getWishList, removeWishListItems
} from "../../../ApplicationStateManagement/UserWishListStore/WishListReducer.js";
import {addToCart} from "../../../ApplicationStateManagement/UserCartStore/CartReducer.js";


const WishListPage = () => {
    const dispatch = useDispatch();
    const {loading, wishListProducts, errorMessage} = useSelector(state => state.WishListReducer);


    useEffect(() => {
        const userId = "bfda";
        dispatch(getWishList(userId));
    }, []);


    /*Delete an Items in the wish List*/
    const handleDeleteItem = (productId) => {
        const userId = "bfda";
        const data = {userId, specificProductIds:productId};
        dispatch(deleteWishList(data));

        dispatch(removeWishListItems(productId));
    }

    /*Clear the Whole Wish List.*/
    const handleDeleteWishList = () => {
        const userId = "bfda";
        const data = {userId, specificProductIds:""};
        dispatch(deleteWishList(data));

        dispatch(clearWishList());
    }

    const handleAddAllToWishList = () => {
        let items = [];
        const userId = "bfda";
        wishListProducts.forEach(({specificProductId}) => items = [...items, {specificProductId, quantity:1}]);
        const data = {userId, items};
        dispatch(addToCart(data));

        dispatch(clearWishList());
    }

    const handleAddToCart = (specificProductId) => {
        const items = [{specificProductId, quantity:1}];
        const userId = "bfda";

        const data = {userId, items};
        dispatch(addToCart(data));

        dispatch(removeWishListItems(specificProductId));
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
                                <Button className={"app-button add-cart-button"}
                                        onClick={() => handleAddToCart(specificProductId)}>
                                    Add to Cart
                                </Button>
                            </div>
                        </div>
                        <FaTrash className={"delete-button"} onClick={() => handleDeleteItem(specificProductId)}/>
                    </div>))}

            </section>

            <section className={"wish-list-bottom-button"}>
                <button className={"clear-wish-list"} onClick={handleDeleteWishList}>Clear WishList</button>
                <Button className={"app-button add-all "} onClick={handleAddAllToWishList}>Add All to Cart</Button>
            </section>

            {loading && <Loader />}
        </div>
    );
};

export default WishListPage;