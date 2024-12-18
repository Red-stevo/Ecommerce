import "./Styles/AllOrdersPage.css";
import {useEffect, useState} from "react";
import {CiFilter} from "react-icons/ci";
import {Button} from "react-bootstrap";
import {useNavigate} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {getOrders} from "../../../ApplicationStateManagement/OdersStore/ordersStore.js";

const orders = [
    {orderId:"FDR567NBHY", orderDate:"Sep Fri 13 2024", orderStatus:"CANCELED", orderTotal:2500.00},
    {orderId:"FDR56EWBZY", orderDate:"Feb Mon 1 2024", orderStatus:"UNDELIVERED", orderTotal:2500.00},
    {orderId:"FDR567N5RY", orderDate:"Feb Mon 1 2024", orderStatus:"UNDELIVERED", orderTotal:2500.00},
    {orderId:"FDR567NBQY", orderDate:"Dec Fri 20 2024", orderStatus:"DELIVERED", orderTotal:2500.00},
    {orderId:"FDR567NBBY", orderDate:"Nov Fri 19 2024", orderStatus:"DELIVERED", orderTotal:2500.00},
    {orderId:"FDR56EWBBY", orderDate:"Nov Fri 19 2024", orderStatus:"DELIVERED", orderTotal:2500.00},
    {orderId:"FDR567NBZY", orderDate:"Feb Mon 1 2024", orderStatus:"UNDELIVERED", orderTotal:2500.00},
    {orderId:"FDR567NBFY", orderDate:"Jan Fri 30 2024", orderStatus:"CANCELED", orderTotal:2500.00},
    {orderId:"FDRAA7NBBY", orderDate:"Nov Fri 19 2024", orderStatus:"CANCELED", orderTotal:2500.00},
    {orderId:"FDR5KLNBBY", orderDate:"Nov Fri 19 2024", orderStatus:"DELIVERED", orderTotal:2500.00},
    {orderId:"FDMN67NBBY", orderDate:"Nov Fri 19 2024", orderStatus:"SHIPPING", orderTotal:2500.00},
    {orderId:"FDR517NBBY", orderDate:"Nov Fri 19 2024", orderStatus:"DELIVERED", orderTotal:2500.00},
    {orderId:"FDR501NBBY", orderDate:"Nov Fri 19 2024", orderStatus:"SHIPPING", orderTotal:2500.00},
    {orderId:"FDR56CXBBY", orderDate:"Nov Fri 19 2024", orderStatus:"SHIPPING", orderTotal:2500.00},
    {orderId:"FDR5OPNBBY", orderDate:"Nov Fri 19 2024", orderStatus:"SHIPPING", orderTotal:2500.00},
    {orderId:"FDOT67NBBY", orderDate:"Nov Fri 19 2024", orderStatus:"DELIVERED", orderTotal:2500.00},
    {orderId:"FDR567NODM", orderDate:"Nov Fri 19 2024", orderStatus:"SHIPPING", orderTotal:2500.00},
    {orderId:"FPNU67NBBY", orderDate:"Nov Fri 19 2024", orderStatus:"DELIVERED", orderTotal:2500.00},
    {orderId:"FDR5UDABBY", orderDate:"Nov Fri 19 2024", orderStatus:"UNDELIVERED", orderTotal:2500.00},
]

const statusList = ["UNDELIVERED", "SHIPPING","DELIVERED","CANCELED", "ALL"];

const AllOrdersPage = () => {
    const [orderDisplay, setOrderDisplay] = useState([]);
    const [statusPointer, setStatusPointer] = useState(0);
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const {orders} = useSelector(state => state.ordersStore);


    useEffect(() => {
        dispatch(getOrders())
    }, []);

    useEffect(() => {
        if (orders && statusPointer <= 3)
            setOrderDisplay(() => orders.filter((order) => (order.orderStatus === statusList[statusPointer])));
        else
            setOrderDisplay([...orders]);
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
        </div>
    );
};

export default AllOrdersPage;