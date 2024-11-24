import "./Styles/AllOrdersPage.css";


const AllOrdersPage = () => {
    return (
        <div className={"all-orders-page"}>
            <div className={"all-orders-holder"}>
                <div className={"headings"}>
                    <span className={"order-num"}>Order ID</span>
                    <span className={"order-date"}>Date</span>
                    <span className={"order-status"}>Status</span>
                    <span className={"total-amount"}>Total</span>
                </div>
                <div className={"order-contents"}>
                    <span>FDR567NBHY</span>
                    <span>Nov Fri 20 2024</span>
                    <span>Delivered</span>
                    <span>ksh 2300.00</span>
                </div>
            </div>
        </div>
    );
};

export default AllOrdersPage;