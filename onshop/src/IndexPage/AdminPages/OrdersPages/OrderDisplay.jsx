import "./Styles/OrderDisplay.css";
import {FaRegUser} from "react-icons/fa";
import {Button, Image} from "react-bootstrap";
import {LiaTimesSolid} from "react-icons/lia";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {updateOrderStatus} from "../../../ApplicationStateManagement/OdersStore/ordersStore.js";
import {getOrderInfo} from "../../../ApplicationStateManagement/OrderInfoStore/OrderInfoReducer.js";
import Loader from "../../../Loading/Loader.jsx";

const statusList = ["UNDELIVERED", "TRANSIT","DELIVERED", "SIGNED"];

const OrderDisplay = () => {
    const {orderDetails, error, loading} = useSelector(state => state.OrderInfoReducer);
    const navigate = useNavigate();
    const [currentStatus, setCurrentStatus] =
        useState(orderDetails.orderStatus?orderDetails.orderStatus:"UNDELIVERED");
    const dispatch = useDispatch();
    const { orderid} = useParams();


    useEffect(() => {
            dispatch(getOrderInfo(orderid));
    }, []);

    const handleStatusUpdate = () => {
        if (statusList.indexOf(currentStatus) <= statusList.length - 2 )
            setCurrentStatus(statusList[statusList.indexOf(currentStatus) + 1]);
    }

    useEffect(() => {
        const data = {orderId:orderid, status:currentStatus}
        dispatch(updateOrderStatus(data));
    }, [currentStatus]);


    return (
        <div className={"order-display-page"}>

           <div className={"username-display"}><FaRegUser className={"user-icon"} />StevoM</div>

            <div className={"order-title"}>Order Details</div>


            <div className={"order-status-num"}>
                <span className={"order-num"}>ID <span className={"order-number"}>#{orderDetails.orderNumber}</span></span>

                <Button className={"order-status-button app-button"} onClick={handleStatusUpdate}>
                    {currentStatus}
                </Button>
            </div>


            <section className={"order-section-1"}>

                <div className={"order-section-products"}>
                    <div className={"product-order-details"}>
                        <span className={"order-products-item"} >Items</span>
                        <span className={"order-products-quantity"} >Quantity</span>
                        <span className={"order-products-price"} >Price</span>
                        <span className={"order-products-total-price"} >Total Price</span>
                    </div>

                    { orderDetails && orderDetails.itemsList && orderDetails.itemsList.length > 0 &&
                        orderDetails.itemsList.map(({productName, productPrice,productId,
                                                       productImageUrl, totalPrice,
                                                       quantity, canceled}, index) => (

                        <div className={`order-products ${canceled ? "canceled-order": "not-canceled-order"}`} key={index}
                        onClick={() => navigate(`${productId}`)}>
                            <span className={"product-name"} title={productName}>
                                <Image src={productImageUrl}  className={"product-image-order"}/>
                                {productName.length > 10 ? productName.substring(0, 10)+"...": productName}
                            </span>
                            <span className={"product-quantity"} >
                                <LiaTimesSolid />
                                {quantity}
                            </span>
                            <span className={"product-price"}>
                                ksh {productPrice}
                            </span>
                            <span className={"product-total-price"} >
                                ksh {totalPrice}
                            </span>
                        </div>))}

                </div>

                <div className={"order-summary"}>
                    <div className={"title-holder-order-summary"}>
                        <span className={"order-date-single"}>Date Created </span>
                        <span className={"order-time"}>Order Time </span>
                        <span className={"order-sub-total"}>Sub Total</span>
                        <span className={"order-delivery"}>Delivery Fee</span>
                        <span className={"order-total"}>Total</span>
                    </div>
                    {orderDetails.orderSummary &&
                    <div className={"value-holder-order-summary"}>
                        <span className={"order-date-value"}>{orderDetails.orderSummary.orderDate}</span>
                        <span className={"order-time-value"}>{orderDetails.orderSummary.orderTime} HRS</span>
                        <span className={"order-sub-total-value"}>ksh {orderDetails.orderSummary.orderTotal}</span>
                        <span className={"order-delivery-value"}>ksh {orderDetails.orderSummary.deliveryFee}</span>
                        <span className={"order-total-value"}> ksh {orderDetails.totalCharges}</span>
                    </div>}
                </div>

            </section>

            {orderDetails.customerDetails &&
                <section className={"user-details-section"}>

                <div className={"customer-order-details"}>
                    <span className={"customer-order-details-title"}>Customer Order Details</span>

                    <div className={"customer-details"}>
                        <span className={"title"}>Customer Name </span>
                        <span>{orderDetails.customerDetails.customerName}</span>
                    </div>
                    <div className={"customer-details"}>
                        <span className={"title"}>Phone Number </span><
                        span>{orderDetails.customerDetails.customerPhone}</span>
                    </div>

                    <div className={"customer-details"}>
                        <span className={"title"}>Email</span><
                        span>{orderDetails.customerDetails.customerEmail}</span>
                    </div>

                    <div className={"customer-details"}>
                        <span className={"title"}>Order Number </span>
                        <span>{orderDetails.orderNumber}</span>
                    </div>
                    <div className={"customer-details"}>
                        <span className={"title"}>Number of Items </span>
                        <span>{orderDetails.customerDetails.numberOfItemsOrdered}</span>
                    </div>

                </div>

                <div className={"address-details"}>
                    <span className={"customer-order-details-title address-title"}>Delivery Address</span>
                    <span className={"address"}>{orderDetails.address}</span>
                </div>

            </section>
            }

            {loading && <Loader />}
        </div>
    );
};

export default OrderDisplay;