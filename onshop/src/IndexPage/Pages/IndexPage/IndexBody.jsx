import DiscountSection from "../Components/IndexBody/DiscountSection.jsx";
import "./../../Styles/IndexBody.css";
import NewProducts from "../Components/IndexBody/NewProducts.jsx";

const IndexBody = () => {
    return (
        <div className={"index-body"}>
            <DiscountSection />
            <NewProducts />
        </div>
    );
};

export default IndexBody;