import "./Styles/OrderDisplay.css";
import {FaRegUser} from "react-icons/fa";
import {Button, Image} from "react-bootstrap";
import {LiaTimesSolid} from "react-icons/lia";
import {useNavigate} from "react-router-dom";
import {useState} from "react";


const orderDetails = {
    orderId:"AS43D4",
    orderStatus:"UNDELIVERED",
    itemList:[
        {
            productName:" Phones",
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.cMIXOtmddqLPOyM69nGcJAHaHa%26pid%3DApi&f=1&ipt=043a92d19939a4ad6ab5e3d8943bababb57551b371f9259371891c1db6dfc3a4&ipo=images",
            productPrice:2300,
            quantity:5,
            totalPrice:23500,
            canceled:false,
            productId:"26SQSW"
        },
        {
            productName:"Wireless Head Phones",
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.cMIXOtmddqLPOyM69nGcJAHaHa%26pid%3DApi&f=1&ipt=043a92d19939a4ad6ab5e3d8943bababb57551b371f9259371891c1db6dfc3a4&ipo=images",
            productPrice:2300,
            quantity:5,
            totalPrice:23500,
            canceled:false,
            productId:"25SQSW"
        },
        {
            productName:"Wireless Head Phones",
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.qr01Z40JyywlFtAb_wlmTQHaHa%26pid%3DApi&f=1&ipt=953ffc87e7155ed3e9410b9e960d4b74ca53ae0b2f5f9aca8b1e295f0a548e3a&ipo=images",            productPrice:2300,
            quantity:5,
            totalPrice:23500,
            canceled:true,
            productId:"24SQSW"
        },
        {
            productName:"Wireless EarPhones",
            productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.WBz2dxmx1MgxVaqFbLUUvAHaHa%26pid%3DApi&f=1&ipt=e7db5985690855750a2ed6c05963b9f7495dd675ed343c3b62c5ae429c635c93&ipo=images",
            productPrice:2300,
            quantity:5,
            totalPrice:23500,
            canceled:false,
            productId:"23SQSW"
        },
    ],
    customerDetails:{
        customerName:"Mirowe Bob",
        customerEmail:"mirowebob@gmail.com",
        customerPhone:"+2549444068",
        numberOfItemsOrdered:6,
        ordersNumber:"AS43D4FFT6YT0P",
    },
    totalCharges:23500,
    address:"Nairobi, Kenya",
    orderNumber:"AS43D4FFT6YT0P",
    orderSummary:{
        orderDate:"Nov Feb 20 2024",
        orderTime:"10:30",
        orderTotal:23500,
        deliveryFee:150,
    }
}

const statusList = ["UNDELIVERED", "SHIPPING","DELIVERED","CANCELED", "ALL"];


const OrderDisplay = () => {
    const {orderId, orderNumber, orderSummary, orderStatus, address,
        itemList, totalCharges, customerDetails} = orderDetails;
    const {orderDate, orderTime, orderTotal, deliveryFee} = orderSummary;
    const  {numberOfItemsOrdered, ordersNumber, customerEmail, customerName, customerPhone
    } = customerDetails;
    const navigate = useNavigate();
    const [currentStatus, setCurrentStatus] = useState(orderStatus ? orderStatus : "UNDELIVERED");

    const handleStatusUpdate = () => {

        if (statusList.indexOf(currentStatus) <= statusList.length - 2 )
            setCurrentStatus(statusList[statusList.indexOf(currentStatus) + 1]);

    }



    return (
        <div className={"order-display-page"}>

           <div className={"username-display"}><FaRegUser className={"user-icon"} />StevoM</div>

            <div className={"order-title"}>Order Details</div>


            <div className={"order-status-num"}>
                <span className={"order-num"}>ID <span className={"order-number"}>#{orderId}</span></span>

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

                    {itemList.length > 0 && itemList.map(({productName, productPrice,productId,
                                                              productImageUrl, totalPrice,
                                                              quantity, canceled}, index) => (

                        <div className={`order-products ${canceled ? "canceled-order": "not-canceled-order"}`} key={index}
                        onClick={() => navigate(`/admin/orders/${orderId}/${productId}`)}>
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

                    <div className={"value-holder-order-summary"}>
                        <span className={"order-date-value"}>{orderDate}</span>
                        <span className={"order-time-value"}>{orderTime} HRS</span>
                        <span className={"order-sub-total-value"}>ksh {orderTotal}</span>
                        <span className={"order-delivery-value"}>ksh {deliveryFee}</span>
                        <span className={"order-total-value"}> ksh {totalCharges}</span>
                    </div>
                </div>

            </section>


            <section className={"user-details-section"}>

                <div className={"customer-order-details"}>
                    <span className={"customer-order-details-title"}>Customer Order Details</span>

                    <div className={"customer-details"}>
                        <span className={"title"}>Customer Name </span>
                        <span>{customerName}</span>
                    </div>
                    <div className={"customer-details"}>
                        <span className={"title"}>Phone Number </span><
                        span>{customerPhone}</span>
                    </div>

                    <div className={"customer-details"}>
                        <span className={"title"}>Email</span><
                        span>{customerEmail}</span>
                    </div>

                    <div className={"customer-details"}>
                        <span className={"title"}>Order Number </span>
                        <span>{ordersNumber}</span>
                    </div>
                    <div className={"customer-details"}>
                        <span className={"title"}>Number of Items </span>
                        <span>{numberOfItemsOrdered}</span>
                    </div>

                </div>

                <div className={"address-details"}>
                    <span className={"customer-order-details-title address-title"}>Delivery Address</span>
                    <span className={"address"}>{address}</span>
                </div>

            </section>

        </div>
    );
};

export default OrderDisplay;