import "./Styles/SingleProductDisplay.css";
import {Image} from "react-bootstrap";
import {useParams} from "react-router-dom";
import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {getSingleProduct} from "../../../ApplicationStateManagement/SingleProductStore/SingleProductReducer.js";
import Loader from "../../../Loading/Loader.jsx";


const SingleProductDisplay = () => {
    const {productDetails, loading, errorMessage} = useSelector(state => state.SingleProductReducer);
    const dispatch = useDispatch();
    const {productId} = useParams();

    useEffect(() => {
        dispatch(getSingleProduct(productId));
    }, []);


    return (
        <div className={"single-order-product-page"}>
                {productDetails.cancel ?
                    <span className={"canceled"}>CANCELED!</span> :
                    <span className={"active-product"}>ACTIVE</span>
                }
            {productDetails &&

            <section className={"order-product-details"}>

                <Image className={"ordered-product-image-url"} src={productDetails.productImageUrl}/>

                <div className={"ordered-product-details-section"}>

                    <span className={"single-order-product-name"}>{productDetails.productName}</span>
                    <span className={"single-order-product-description"}>{productDetails.productDescription}</span>

                    <div className={"quantity-proportion-variety"}>
                        <span
                            className={"single-order-product-quantity"}>Quantity : {productDetails.quantity}</span>
                        <span
                            className={"single-order-product-proportion"}>Proportion : {productDetails.proportion}</span>
                        <span
                            className={"single-order-product-variety"}>Variety : {productDetails.variety}</span>
                    </div>

                </div>

            </section>
            }

            {loading && <Loader />}
        </div>
    );
};

export default SingleProductDisplay;