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
    getOrderStatus, removeOrder
} from "../../../ApplicationStateManagement/OrderStatusStore/OrderStatusReducer.js";
import Loader from "../../../Loading/Loader.jsx";



const statusList = [
    { statusName:"Order placed", statusIcon:<AiOutlineFileProtect /> },
    { statusName:"In transit", statusIcon:<TbTruckDelivery /> },
    { statusName:"Delivered", statusIcon:<CiDeliveryTruck /> },
    { statusName:"Signed", statusIcon:<LuPackageCheck /> },
]


const OrderStatus = () => {
    const { shippingStatus, loading, errorMessage} = useSelector(status => status.OrderStatusReducer);
    const {orderId, status, products} = shippingStatus;
    const stepRef = useRef([]);
    const [margins,setMargins] = useState({marginsLeft:0, marginRight:0});
    const  [calcProgressBarWidth] = useState(() => {
        if(status >= statusList.length) return 100;
        else return status / (statusList.length - 1)  * 100;});
    const dispatch = useDispatch();


    /*Track window resize*/
    const [_, setWindowWidth] = useState(window.innerWidth);

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
        const data = {userId:"bfda", orderItemId:productId};
        dispatch(cancelOrderItem(data));

        dispatch(removeOrder(productId));
    }

    useEffect(() => {

        const userId = "bfda";
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
                     style={{width:`calc(100% - ${margins.marginsLeft + margins.marginRight}px)`,
                         marginLeft:`${margins.marginsLeft}px`, marginRight:`${margins.marginRight}px`}}>
                    <div className={"order-progress-bar"} style={{width:`${calcProgressBarWidth}%`}} />
                </div>

            </section>

            <section className={"ordered-products-section"}>

                {products && products.length > 0 &&
                    products
                        .map(({productImageUrl, productName, productPrice, specificProductId}, index) => (
                    <div key={index} className={"product-image-details-holder"}>
                        <Image className={"ordered-product-image"} src={productImageUrl} />
                        <div className={"ordered-product-details"}>
                            <span className={"ordered-product-name"}>{productName}</span>
                            <span className={"ordered-product-price"}>
                               <>ksh {productPrice}</>
                                <button className={"cancel-order"} onClick={() => handleCancelOrder(specificProductId)}>
                                    cancel
                                </button>
                            </span>
                        </div>
                    </div>
                ))}

            </section>
            {loading && <Loader />}
        </div>
    );
};

export default OrderStatus;