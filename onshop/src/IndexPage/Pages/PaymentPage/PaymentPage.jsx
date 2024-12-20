import "./Styles/PaymentPage.css";
import {CiEdit} from "react-icons/ci";
import {PiMapPinAreaFill} from "react-icons/pi";
import mpesaIcon from "./../../../assets/mpesaPaymentIcon.svg";
import {Image, InputGroup} from "react-bootstrap";
import {useEffect, useState} from "react";
import {TbMoneybag} from "react-icons/tb";
import EditLocationModal from "./Components/EditLocationModal.jsx";
import {useDispatch, useSelector} from "react-redux";
import {getPaymentDetails} from "../../../ApplicationStateManagement/PaymentStore/PaymentReducer.js";
import Loader from "../../../Loading/Loader.jsx";
import ProductDisplayComponent from "./Components/ProductDisplayComponent.jsx";

const PaymentPage = () => {
    const {paymentDetails, loading, error} = useSelector(state => state.PaymentReducer);
    const {username, phoneNumber, location, products, productsAmount, shippingCost} = paymentDetails;
    const [productCount, setProductsCount] = useState(0);
    const [modalShow, setModalShow] = useState(false);
    const dispatch = useDispatch();


    useEffect(() => {
        const userId = "bfda";
        dispatch(getPaymentDetails(userId));
    }, []);


    useEffect(() => {
        const setItemsCount = () => {
            setProductsCount(() => 0);
            products.forEach(({productCount}) =>{
                setProductsCount(prevState => prevState + productCount)
            });}
        if (products) setItemsCount();
    }, [products]);


    return (
        <div className={"payment-page"}>

            <section className={"payment-page-location-section"}>

                <div className={"payment-page-location-section-title"}>
                    <span className={"payment-page-location-section-shipping"} >Shipping Information</span>
                    <button className={"payment-page-location-section-edit"} onClick={() => setModalShow(true)}>
                        <CiEdit/> Edit
                    </button>
                </div>

                <div className={"payment-page-location-section-address"}>
                    <PiMapPinAreaFill className={"payment-page-location-section-icon"} />
                    <div className={"payment-page-location-section-phone-location"}>
                        <span className={"payment-page-location-section-phone"}>{username} {phoneNumber}</span>
                        <span className={"payment-page-location-section-location"}>Location {location}</span>
                    </div>
                </div>

            </section>


            <section className={"payment-page-pay-method-section"}>

                <span className={"payment-page-pay-method-section-title"}>Payment Method</span>

                <div className={"payment-page-pay-method-section-number-icon"}>

                    <div className={"payment-page-pay-method-section-icon"}>
                        <Image  src={mpesaIcon} className={"payment-page-pay-method-section-mpesa-icon"}/>
                        <span className={"payment-page-pay-method-section-icon-text"}>
                            Via Gambi Stores Collections
                        </span>
                    </div>

                    <InputGroup className={" payment-page-pay-method-section-number"}>
                        <InputGroup.Text id="basic-addon1">+254</InputGroup.Text>
                        <input className={"form-control payment-page-pay-method-section-number-input"}
                               minLength={9} maxLength={9} type={"tel"}
                            placeholder="Phone Number" aria-label="Phone Number" aria-describedby="basic-addon1"/>
                    </InputGroup>

                </div>

            </section>


            <section className={"payment-page-products-section"}>

                <span className={"payment-page-products-section-product-title-count"}>
                    Product List ( {productCount} Items)
                </span>

                <div className={"payment-page-products-section-products"}>
                    {products && products.map((
                            {productCount, productId, productImage, productName, productPrice}, index) =>  (
                                <ProductDisplayComponent
                                    productImage={productImage} productCount={productCount}
                                    productId={productId} productName={productName} productPrice={productPrice}
                                    key={index}/>
                        )
                    )}
                </div>

            </section>


            <section className={"payment-page-price-section"}>
                <span className={"payment-page-price-section-title"}>Total</span>

                <div className={"payment-page-price-section-prices-summary"}>

                    <div className={"payment-page-price-section-prices-summary-holder"}>
                        <span className={"payment-page-price-section-product-price"}>
                            {products && products.length > 1 ? "Products " : "Product "}
                            Amount : <span>ksh {productsAmount}</span>
                        </span>
                        <span className={"payment-page-price-section-shipping-price"}>
                            Shipping Fee : <span>+ ksh {shippingCost}</span>
                        </span>
                        <span className={"payment-page-price-section-total-price"}>
                            Payment Amount : <span>ksh {shippingCost + productsAmount}</span>
                        </span>
                    </div>

                    <button className={"payment-page-price-section-pay-button"}>
                        <TbMoneybag className={"payment-page-price-section-money-bag"} />
                        PAY NOW
                    </button>
                </div>


            </section>


            <EditLocationModal show={modalShow} onHide={() => setModalShow(false)}  />

            {loading && <Loader />}
        </div>
    );
};

export default PaymentPage;


