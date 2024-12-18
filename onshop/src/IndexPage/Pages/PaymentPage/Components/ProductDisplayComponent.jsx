import {Image} from "react-bootstrap";
import {RxTriangleDown, RxTriangleUp} from "react-icons/rx";
import {useEffect, useState} from "react";
import {useDispatch} from "react-redux";
import {updateOrderQuantity} from "../../../../ApplicationStateManagement/PaymentStore/PaymentReducer.js";


const ProductDisplayComponent = ({productId, productImage, productName, productPrice, productCount}) => {
    const [productQuantity, setProductQuantity] = useState(productCount ? productCount : 1);
    const dispatch = useDispatch();


    useEffect(() => {
        const data = {productId, quantity:productQuantity}
        dispatch(updateOrderQuantity(data));
    }, [productQuantity]);

    return (
        <div key={productId} className={"payment-page-products-section-product"}>
            <Image src={productImage}
                   className={"payment-page-products-section-product-image"}/>

            <span className={"payment-page-products-section-product-name"}>{productName}</span>

            <div className={"payment-page-products-section-product-price-count"}>
                <span className={"payment-page-products-section-product-price"}>
                    ksh {productPrice}
                </span>
                <span className={"payment-page-products-section-product-count"}>
                    <span className={"quantity-toggle-section"}>
                        <RxTriangleUp onClick={() => setProductQuantity(prevState => prevState +1)}/>
                        <span className={"quantity"}>{productQuantity}</span>
                        <RxTriangleDown onClick={() => setProductQuantity(prevState =>{
                            if (prevState > 1) return prevState - 1; else return 1; })}/>
                    </span>
                </span>
            </div>

        </div>
    );
};

export default ProductDisplayComponent;