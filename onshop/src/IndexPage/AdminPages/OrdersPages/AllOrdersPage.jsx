import "./Styles/AllOrdersPage.css";
import {useEffect, useState} from "react";
import {CiFilter} from "react-icons/ci";
import {Button} from "react-bootstrap";
import {useNavigate} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {getOrders} from "../../../ApplicationStateManagement/OdersStore/ordersStore.js";
import Loader from "../../../Loading/Loader.jsx";

const statusList = ["UNDELIVERED", "SHIPPING","DELIVERED","CANCELED", "ALL"];

const AllOrdersPage = () => {
    const {orders, page, loading, error} = useSelector(state => state.ordersStore);
    const [orderDisplay, setOrderDisplay] = useState([]);
    const [statusPointer, setStatusPointer] = useState(0);
    const navigate = useNavigate();
    const dispatch = useDispatch();



    useEffect(() => {
        dispatch(getOrders())
    }, []);

    useEffect(() => {
        if (orders && statusPointer <= 3)
            setOrderDisplay(() => orders.filter((order) => (order.orderStatus === statusList[statusPointer])));
        else if (orders)
            setOrderDisplay([...orders]);

        console.log("Orders -> ",orders);
    }, [statusPointer, orders]);


    const  handleFilter = ()  => {
        if (statusPointer >= 4) setStatusPointer(0);
        else setStatusPointer(prevState => prevState + 1);
    }


    return (
        <div className={"all-orders-page"}>
            <div className={"all-orders-holder"}>
                <div className={"headings"}>
                    <span className={"order-num"}>Order ID</span>
                    <span className={"order-date"}>Date</span>
                    <span className={"order-status"}>Status</span>
                    <span className={"total-amount"}>Total</span>
                </div>

                <div className={"filter-component"}>
                    <span>{statusList[statusPointer]}</span>
                    <CiFilter className={"filter-icon"} onClick={handleFilter}/>
                </div>

                {orderDisplay.length > 0 && orderDisplay.map(({orderId, orderDate, orderStatus, orderTotal}) => (
                    <div className={"order-contents"} key={orderId} onClick={() => navigate(`/admin/orders/${orderId}`)}>
                        <span className={"text"}>{orderId}</span>
                        <span className={"text"}>{orderDate}</span>
                        <span className={"text"}>{orderStatus}</span>
                        <span className={"cost-text text"}>ksh {orderTotal}</span>
                    </div>
                ))}
            </div>

            <div className={"load-more-orders"}><Button className={"app-button"}>Load More</Button></div>

            {loading && <Loader />}
        </div>
    );
};

export default AllOrdersPage;