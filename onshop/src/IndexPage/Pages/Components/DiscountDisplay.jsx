import {TiStarburst} from "react-icons/ti";

const DiscountDisplay = ({newPrice, oldPrice }) => {
    return (
        <div className={"prices-discounted-products"}>
            <TiStarburst className={"discount-starburst"}/>
            <div className={"text-on-offer"}>
                <span className={"new-discounted-price"}>Now</span>
                <span className={"new-discounted-price"}>KSH {newPrice} Only</span>
                <span className={"old-product-price"}>KSH {oldPrice}</span>
            </div>
        </div>
    );
};

export default DiscountDisplay;