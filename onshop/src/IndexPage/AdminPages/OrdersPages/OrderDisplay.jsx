import "./Styles/OrderDisplay.css";
import {FaRegUser} from "react-icons/fa";


const orderDetails = {
    username:"Stephen Muiru",
    orderId:"AS43D4FFT6YT0P",
    orderStatus:"UNDELIVERED",
    items:[]
}



const OrderDisplay = () => {
    return (
        <div className={"order-display-page"}>
           <div className={"user-icon-name"}><FaRegUser /></div>
        </div>
    );
};

export default OrderDisplay;