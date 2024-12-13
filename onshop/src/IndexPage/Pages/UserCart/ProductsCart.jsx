import "./Styles/ProductsCart.css";
import {Button, Image} from "react-bootstrap";
import {useEffect, useState} from "react";
import {ImCheckboxChecked, ImCheckboxUnchecked} from "react-icons/im";
import CartProduct from "./Components/CartProduct.jsx";
import {FaMoneyBill1Wave} from "react-icons/fa6";
import {PiArrowFatLeftThin, PiArrowFatLineLeftThin, PiArrowFatLinesLeftThin} from "react-icons/pi";
import StarRating from "../ProductsDisplayPage/Components/StarRating.jsx";
import {useNavigate} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {deleteCartItem, getCartItems, removeItems
} from "../../../ApplicationStateManagement/UserCartStore/CartReducer.js";
import noCartImage from "./../../../assets/NoCartItems.png";
import Loader from "../../../Loading/Loader.jsx";
import ErrorModal from "../../ErrorModal.jsx";


const ProductsCart = () => {
    const [checkIcon, setCheckIcon] = useState(false);
    const {
        username, cartId, cartItemsResponses, currentPage,
        totalPages, hasMore, youMayLikes, totalPrice,
    } = useSelector(state => state.CartReducer.CartResponse);
    const {loading} = useSelector(state => state.CartReducer);
    const [index, setIndex] = useState(0);
    const [selectedProducts, setSelectedProducts] = useState([]);
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [page, setPage] = useState(currentPage ? currentPage : 0);
    const [modalShow, setModalShow] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const cartData = {page, size: 1, userId: "b69eb7ae-d567-45b8-a6a0-92c7f243874f"}
        dispatch(getCartItems(cartData));
    }, [page]);


    useEffect(() => {
        if (selectedProducts.length !== cartItemsResponses.length) setCheckIcon(false);
        else setCheckIcon(true);
    }, [selectedProducts, cartItemsResponses]);


    useEffect(() => {
        setTimeout(() => {
            if (index === 0)
                setIndex(1);
            else if (index === 1)
                setIndex(2);
            else
                setIndex(0);

        }, 500);
    }, [index]);

    const handleDeselection = () => {

        setCheckIcon(false);
        setSelectedProducts(() => []);
    }


    const handleDeleteCart = () => {

        dispatch(deleteCartItem(selectedProducts));

        /*Remove the delete elements.*/
        dispatch(removeItems(selectedProducts));

        /*Clear the selected cart items*/
        setSelectedProducts(() => []);
    }

    useEffect(() => {
        if (error) {
            setModalShow(true);

            setTimeout(() => {
                setModalShow(false);
                setError(null);
            }, 2000);
        }
    }, [error]);


    const handleOrder = () => {
        if (selectedProducts.length === 0){
            setError("No Selected Item!");
            return;
        }
    }


    if (!cartItemsResponses || cartItemsResponses.length === 0) {
        return (
            <div className={"no-cart-items"}>
                <span className={"title-empty-list"}>Oops! Your Cart is Empty.</span>
                <Image src={noCartImage} className={"no-cart-items-image"}/>
            </div>
        );
    }


    return (
        <div className={"cart-page"}>
            {/*Page header.*/}
            <div className={"top-buttons"}>
                <div className={"select-all-holder"}>
                    {checkIcon ?
                        <ImCheckboxChecked className={"select-checked"} onClick={handleDeselection}/> :
                        <ImCheckboxUnchecked className={"select-unchecked"} onClick={() => setCheckIcon(true)}/>}
                    <span>select all</span>
                </div>
                <Button disabled={selectedProducts.length === 0}
                        className={"danger-button delete-order-product"} onClick={handleDeleteCart}>
                    Delete
                </Button>
            </div>

            {/*Product Display.*/}
            <div className={"ordered-products-display"}>
                {cartItemsResponses && cartItemsResponses.map(
                    ({
                         cartItemId, productPrice, productName, productImageUrl,
                         color, inStock, count
                     }, index) => (
                        <CartProduct productPrice={productPrice} productName={productName} inStock={inStock}
                                     setSelectedProducts={setSelectedProducts} count={count} color={color}
                                     selectAllCheck={checkIcon} unCheckAll={selectedProducts} cartId={cartId}
                                     id={cartItemId} productImageUrl={productImageUrl} key={index}/>
                    ))}


                {/*Price display*/}
                <div className={"total-price-shop"}>
                    <div className={"total-price"}><span className={"total"}>Total</span>ksh {totalPrice}
                    </div>

                    <div className={"button-shop"}>
                        <span className={"continue-shopping"} title={"Continue Shopping"}>
                            <PiArrowFatLeftThin title={"Continue Shopping"}
                                                className={`${index === 0 ? "unhidden-arrow" : "hidden-arrow"}`}/>
                            <PiArrowFatLineLeftThin title={"Continue Shopping"}
                                                    className={`${index === 1 ? "unhidden-arrow" : "hidden-arrow"}`}/>
                            <PiArrowFatLinesLeftThin title={"Continue Shopping"}
                                                     className={`${index === 2 ? "unhidden-arrow" : "hidden-arrow"}`}/>

                        </span>
                        <button className={"checkout-button"} onClick={handleOrder}>
                            <FaMoneyBill1Wave className={"money-bill"}/>Check out
                        </button>
                    </div>
                </div>
            </div>

            {/*You may also like*/}
            <section className={"you-may-also-like-section"}>
                <span className={"you-may-like-header"}>You May Also Like</span>

                <div className={"you-may-like-products"}>
                    {youMayLikes && youMayLikes.map((
                        {
                            productId, productPrice, productName,
                            productImageUrl, rating
                        }, index) => (
                        <div key={index} className={"you-may-like-product"}
                             onClick={() => navigate(`/home/product/${productId}`)}>
                            <Image className={"you-may-like-product-image"} src={productImageUrl}/>
                            <span title={productName}>
                                        {productName.substring(0, 12)} {productName.length > 12 && "..."}
                                    </span>
                            <span className={"price"}>
                                        ksh {productPrice}
                                    </span>
                            <StarRating value={rating} active={true}/>
                        </div>
                    ))}
                </div>

            </section>
            <div className={`load-more-button-holder ${!hasMore && " hide "}`}>
                <Button onClick={() => setPage(prevState => prevState + 1)}
                        className={"app-button load-more-button"}>Load More</Button>
            </div>

            {loading && <Loader/>}
            <ErrorModal errormessage={error} show={modalShow} onHide={() => setModalShow(false)}/>
        </div>);
}

export default ProductsCart;