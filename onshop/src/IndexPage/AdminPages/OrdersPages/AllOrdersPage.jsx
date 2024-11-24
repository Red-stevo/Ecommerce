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
                    <span className={"text"}>FDR567NBHY</span>
                    <span className={"text"}>Nov Fri 20 2024</span>
                    <span className={"text"}>Delivered</span>
                    <span className={"cost-text text"}>ksh 2300.00</span>
                </div>
            </div>
        </div>
    );
};

export default AllOrdersPage;