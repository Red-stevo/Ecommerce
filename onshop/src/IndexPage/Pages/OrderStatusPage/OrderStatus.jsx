import "./Styles/OrderStatus.css";
import {AiOutlineFileProtect} from "react-icons/ai";
import {TbTruckDelivery} from "react-icons/tb";
import {CiDeliveryTruck} from "react-icons/ci";
import {LuPackageCheck} from "react-icons/lu";
import {useEffect, useRef, useState} from "react";
import {Image} from "react-bootstrap";
import {useDispatch, useSelector} from "react-redux";
import {
    cancelOrderItem,
    getOrderStatus
} from "../../../ApplicationStateManagement/OrderStatusStore/OrderStatusReducer.js";

const shippingStatus = {

    orderTrackingProducts : [
        {productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
            productName:"J4 cactus jack sneakers", productPrice:2340.0},
        {productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
            productName:"J4 cactus jack sneakers", productPrice:2340.0},
        {productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
            productName:"J4 cactus jack sneakers", productPrice:2340.0},
        {productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
            productName:"J4 cactus jack sneakers", productPrice:2340.0},
        {productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
            productName:"J4 cactus jack sneakers", productPrice:2340.0},
        {productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
            productName:"J4 cactus jack sneakers", productPrice:2340.0},
        {productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
            productName:"J4 cactus jack sneakers", productPrice:2340.0},
        {productImageUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.WCCq2nZelTZuFIRbJF7AuAHaEK%26pid%3DApi&f=1&ipt=536de08d8441cea809d6267004fecd429bb7f1c6492547d25bf244e3d597bbdd&ipo=images",
            productName:"J4 cactus jack sneakers", productPrice:2340.0},
    ],
    orderId:"34DF4T",
    status:2    // PLACED_ORDER DELIVERED  TRANSIT SIGNED
};


const statusList = [
    { statusName:"Order placed", statusIcon:<AiOutlineFileProtect /> },
    { statusName:"In transit", statusIcon:<TbTruckDelivery /> },
    { statusName:"Delivered", statusIcon:<CiDeliveryTruck /> },
    { statusName:"Signed", statusIcon:<LuPackageCheck /> },
]


const OrderStatus = () => {
    const { shippingStatus, loading, success, errorMessage} =
        useSelector(status => status.OrderStatusReducer);
    const {orderId, status, orderTrackingProducts} = shippingStatus;
    const stepRef = useRef([]);
    const [margins,setMargins] = useState({marginsLeft:0, marginRight:0});
    const  [calcProgressBarWidth, setCalcProgressBarWidth] = useState(() => {
        if(status >= statusList.length) return 100;
        else return status / (statusList.length - 1)  * 100;});
    const dispatch = useDispatch();


    /*Track window resize*/
    const [windowWidth, setWindowWidth] = useState(window.innerWidth);

    useEffect(() => {
        const updateWidth = () => setWindowWidth(window.innerWidth);
        window.addEventListener("resize", updateWidth);
        return () => window.removeEventListener("resize", updateWidth);
    }, []);

    /*UPDATE the left and right margins for the progress bar.*/
    useEffect(() => {
        setMargins( {
            marginsLeft:stepRef.current[0].offsetWidth/2,
            marginRight: stepRef.current[statusList.length - 1].offsetWidth/2
        });
    }, [stepRef, calcProgressBarWidth, window.innerWidth]);

    const handleCancelOrder = (productId) => {
        const data = {userId:"12a8bb23-0d41-4118-bda0-0390f382814b", orderItemId:productId};
        dispatch(cancelOrderItem(data));
    }

    useEffect(() => {

        const userId = "12a8bb23-0d41-4118-bda0-0390f382814b";
        const getOrderItems = () => {
            dispatch(getOrderStatus(userId));
        }

        getOrderItems();
    }, [])

    return (
        <div className={"order-status-page"}>
            <section className={"header-section"}>
                <span className={"order-page-title"}>Order Status.</span>
                <span className={"status-order-id"}>ID #{orderId}</span>
            </section>

            <section className={"order-steps"}>
                {statusList && statusList.map(({statusName, statusIcon}, index) => (
                    <div className={"order-status-step"} key={index}
                        ref={ (el) => (stepRef.current[index] = el)}>
                        <span className={"status-icon"}>{statusIcon}</span>
                        <span className={"status-page-name"}>{statusName}</span>
                        <span className={`status-index ${index === status && " active "} 
                        ${index < status && " done " } ${index > status && " in-line "}`}>
                            {status && status > index ? <>&#10003;</> : index + 1 }
                        </span>
                    </div>
                ))}


                <div className={"order-progress"}
                     style={{
                         width:`calc(100% - ${margins.marginsLeft + margins.marginRight}px)`,
                         marginLeft:`${margins.marginsLeft}px`, marginRight:`${margins.marginRight}px`}}>

                    <div className={"order-progress-bar"}
                         style={{width:`${calcProgressBarWidth}%`}} />

                </div>

            </section>

            <section className={"ordered-products-section"}>

                {orderTrackingProducts && orderTrackingProducts.length > 0 &&
                    orderTrackingProducts
                        .map(({productImageUrl, productName, productPrice, productId}, index) => (
                    <div key={index} className={"product-image-details-holder"}>
                        <Image className={"ordered-product-image"} src={productImageUrl} />
                        <div className={"ordered-product-details"}>
                            <span className={"ordered-product-name"}>{productName}</span>
                            <span className={"ordered-product-price"}>
                               <>ksh {productPrice}</>
                                <button className={"cancel-order"} onClick={() => handleCancelOrder(productId)}>
                                    cancel
                                </button>
                            </span>
                        </div>
                    </div>
                ))}

            </section>

        </div>
    );
};

export default OrderStatus;